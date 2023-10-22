/*
 * Copyright (c) 2023.
 */

/*
 * Copyright (c) 2023.
 */

package com.max.billing.helper

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.max.billing.data.ProductDetail
import com.max.billing.data.PurchaseInfo
import com.max.billing.extension.formatMaxDate
import com.max.billing.extension.getDurationFromId
import com.max.billing.interfaces.OnConnectionListener
import com.max.billing.interfaces.OnPurchaseListener
import com.max.billing.provider.InAppProvider
import com.max.billing.provider.SubscriptionProvider
import com.max.billing.types.BillingState
import com.max.billing.types.BillingType
import com.max.billing.types.PlanType
import com.max.billing.types.State.getBillingState
import com.max.billing.types.State.setBillingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

@Suppress("unused")
abstract class BillingHelper(private val context: Context) : KoinComponent {

    private val inAppProvider: InAppProvider by inject()
    private val subscriptionProvider: SubscriptionProvider by inject()

    private var onConnectionListener: OnConnectionListener? = null
    private var onPurchaseListener: OnPurchaseListener? = null
    private var purchaseDetail: PurchaseInfo? = null

    @JvmField
    protected var checkForSubscription = false
    private var isPurchasedFound = false

    private val _productDetailList = ArrayList<ProductDetail>()
    private val productDetailList: List<ProductDetail> get() = _productDetailList.toList()

    private val _productDetailsLiveData = MutableLiveData<List<ProductDetail>>()
    val productDetailsLiveData: LiveData<List<ProductDetail>> = _productDetailsLiveData


    /* ------------------------------------------------ Initializations ------------------------------------------------ */

    private val billingClient by lazy {
        BillingClient.newBuilder(context).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()
    }

    /* ------------------------------------------------ Establish Connection ------------------------------------------------ */
    abstract fun setCheckForSubscription(isCheckRequired: Boolean)

    abstract fun startConnection(
        productIdsList: ArrayList<String>,
        onConnectionListener: OnConnectionListener
    )

    protected fun startBillingConnection(
        productIdsList: ArrayList<String>,
        onConnectionListener: OnConnectionListener
    ) {
        this.onConnectionListener = onConnectionListener
        if (productIdsList.isEmpty()) {
            setBillingState(BillingState.EMPTY_PRODUCT_ID_LIST)
            onConnectionListener.onConnectionResult(
                false,
                BillingState.EMPTY_PRODUCT_ID_LIST.message
            )
            return
        }
        inAppProvider.addProducts(productIdsList)
        setBillingState(BillingState.CONNECTION_ESTABLISHING)

        if (billingClient.isReady) {
            setBillingState(BillingState.CONNECTION_ALREADY_ESTABLISHING)
            proceedBilling()
            return
        }

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                setBillingState(BillingState.CONNECTION_DISCONNECTED)
                Handler(Looper.getMainLooper()).post {
                    onConnectionListener.onConnectionResult(
                        false,
                        BillingState.CONNECTION_DISCONNECTED.message
                    )
                }
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                val isBillingReady =
                    billingResult.responseCode == BillingClient.BillingResponseCode.OK
                if (isBillingReady) {
                    proceedBilling()
                } else {
                    setBillingState(BillingState.CONNECTION_FAILED)
                    onConnectionListener.onConnectionResult(false, billingResult.debugMessage)
                }
            }
        })
    }

    private fun proceedBilling() {
        setBillingState(BillingState.CONNECTION_ESTABLISHED)
        getInAppOldPurchases()
        queryForAvailableInAppProducts()
        queryForAvailableSubProducts()
        Handler(Looper.getMainLooper()).post {
            onConnectionListener?.onConnectionResult(
                true,
                BillingState.CONNECTION_ESTABLISHED.message
            )
        }
    }

    private fun getPurchaseTime(purchaseTime: Long): String {
        return formatMaxDate().format(Date(purchaseTime))
    }

    private fun getInAppOldPurchases() = CoroutineScope(Dispatchers.Main).launch {
        setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_INAPP_FETCHING)
        val queryPurchasesParams =
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)
                .build()
        billingClient.queryPurchasesAsync(queryPurchasesParams) { _, purchases ->
            Log.d(
                TAG,
                " --------------------------- old purchase (In-App)  --------------------------- "
            )
            Log.d(TAG, "getInAppOldPurchases: Object: $purchases")
            purchases.forEach { purchase ->
                Log.d(TAG, "getInAppOldPurchases: Object: $purchase")
                Log.d(TAG, "getInAppOldPurchases: Products: ${purchase.products}")
                Log.d(TAG, "getInAppOldPurchases: Original JSON: ${purchase.originalJson}")
                Log.d(TAG, "getInAppOldPurchases: Developer Payload: ${purchase.developerPayload}")

                if (purchase.products.isEmpty()) {
                    setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_INAPP_NOT_FOUND)
                    checkForSubscriptionIfAvailable()
                    return@forEach
                }

                // getting the  single  product-id of every purchase in the list = sku
                val compareSKU = purchase.products[0]

                if (purchase.isAcknowledged) {
                    inAppProvider.productSKUs().forEach {
                        if (it.contains(compareSKU, true)) {
                            isPurchasedFound = true
                            purchaseDetail =
                                inAppProvider.getPurchaseDetail(purchase)
                            setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_INAPP_OWNED)
                        }
                    }
                    checkForSubscriptionIfAvailable()
                } else {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        for (i in 0 until inAppProvider.productSKUs().size) {
                            setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_INAPP_OWNED_BUT_NOT_ACKNOWLEDGE)

                            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.purchaseToken)
                                .build()

                            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult ->
                                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK || purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                    setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_INAPP_OWNED_AND_ACKNOWLEDGE)
                                    purchaseDetail = inAppProvider.getPurchaseDetail(
                                        purchase
                                    )
                                    isPurchasedFound = true
                                } else {
                                    setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_INAPP_OWNED_AND_FAILED_TO_ACKNOWLEDGE)
                                }
                                checkForSubscriptionIfAvailable()
                            }
                        }
                    } else {
                        checkForSubscriptionIfAvailable()
                    }
                }
            }
            if (purchases.isEmpty()) {
                setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_INAPP_NOT_FOUND)
                checkForSubscriptionIfAvailable()
            }
        }
    }

    private fun checkForSubscriptionIfAvailable() {
        if (!checkForSubscription || isPurchasedFound) {
            CoroutineScope(Dispatchers.Main).launch {
                onConnectionListener?.onOldPurchaseResult(isPurchasedFound, purchaseDetail)
            }
            if (isPurchasedFound) return
        }
        getSubscriptionOldPurchases()
    }

    private fun getSubscriptionOldPurchases() = CoroutineScope(Dispatchers.Main).launch {
        setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_SUB_FETCHING)
        val queryPurchasesParams =
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build()

        billingClient.queryPurchasesAsync(queryPurchasesParams) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { purchase ->
                    if (purchase.products.isEmpty()) {
                        setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_SUB_NOT_FOUND)
                        return@forEach
                    }

                    // getting the  single  product-id of every purchase in the list = sku
                    val compareSKU = purchase.products[0]

                    if (purchase.isAcknowledged) {
                        for (i in 0 until subscriptionProvider.productIdsList(planType = PlanType.BASIC()).size) {
                            if (subscriptionProvider.productIdsList(planType = PlanType.BASIC())[i].get().id
                                    .contains(compareSKU)
                            ) {
                                setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_SUB_OWNED)
                                isPurchasedFound = true
                                purchaseDetail =
                                    subscriptionProvider.getPurchaseDetail(purchase)
                                calculateResult()
                                return@forEach
                            }
                        }
                    } else {
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_SUB_OWNED_BUT_NOT_ACKNOWLEDGE)
                            for (i in 0 until subscriptionProvider.productIdsList(planType = PlanType.BASIC()).size) {
                                val acknowledgePurchaseParams =
                                    AcknowledgePurchaseParams.newBuilder()
                                        .setPurchaseToken(purchase.purchaseToken)
                                        .build()
                                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult ->
                                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK || purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                        setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_SUB_OWNED_AND_ACKNOWLEDGE)
                                        isPurchasedFound = true
                                        purchaseDetail = subscriptionProvider.getPurchaseDetail(
                                            purchase
                                        )
                                    } else {
                                        setBillingState(BillingState.CONSOLE_OLD_PRODUCTS_SUB_OWNED_AND_FAILED_TO_ACKNOWLEDGE)
                                    }
                                    calculateResult()
                                }
                            }
                        } else {
                            calculateResult()
                        }
                    }
                }
                if (purchases.isEmpty())
                    calculateResult()
            }
        }
    }

    private fun calculateResult() {
        CoroutineScope(Dispatchers.Main).launch {
            onConnectionListener?.onOldPurchaseResult(isPurchasedFound, purchaseDetail)
        }
    }

    private fun queryForAvailableInAppProducts() = CoroutineScope(Dispatchers.Main).launch {
        setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_FETCHING)
        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(
                QueryProductDetailsParams.newBuilder()
                    .setProductList(inAppProvider.getProductList()).build()
            )
        }

        if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_FETCHED_SUCCESSFULLY)
            if (productDetailsResult.productDetailsList.isNullOrEmpty()) {
                setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_NOT_EXIST)
            } else {
                productDetailsResult.productDetailsList?.forEach {
                    val item = ProductDetail(
                        id = it.productId,
                        currency = it.oneTimePurchaseOfferDetails?.priceCurrencyCode.toString(),
                        price = it.oneTimePurchaseOfferDetails?.formattedPrice.toString()
                            .removeSuffix(".00"),
                        billingType = BillingType.INAPP(),
                        amountMicros = it.oneTimePurchaseOfferDetails?.priceAmountMicros,
                        duration = getDurationFromId(it.productId)
                    )
                    _productDetailList.add(item)
                    _productDetailsLiveData.postValue(productDetailList)
                }
                inAppProvider.setProductDetail(ArrayList(productDetailsResult.productDetailsList!!))
                setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_AVAILABLE)
            }
        } else {
            setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_FETCHING_FAILED)
        }
    }

    private fun queryForAvailableSubProducts() = CoroutineScope(Dispatchers.Main).launch {
        setBillingState(BillingState.CONSOLE_PRODUCTS_SUB_FETCHING)
        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(
                QueryProductDetailsParams.newBuilder()
                    .setProductList(subscriptionProvider.getProductList(planType = PlanType.BASIC()))
                    .build()
            )
        }
        // Process the result.
        if (productDetailsResult.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            setBillingState(BillingState.CONSOLE_PRODUCTS_SUB_FETCHED_SUCCESSFULLY)
            if (productDetailsResult.productDetailsList.isNullOrEmpty()) {
                setBillingState(BillingState.CONSOLE_PRODUCTS_SUB_NOT_EXIST)
            } else {
                productDetailsResult.productDetailsList?.forEach { productDetails ->
                    if (!productDetails.subscriptionOfferDetails.isNullOrEmpty()) {
                        val purchaseList =
                            productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList

                        val item = ProductDetail(
                            id = productDetails.productId,
                            duration = getDurationFromId(productDetails.productId),
                            billingType = BillingType.SUBSCRIPTION(),
                        )

                        purchaseList.forEach { temp ->
                            if (temp.priceAmountMicros == 0L) {
                                item.trial = true
                            } else {
                                item.currency = temp.priceCurrencyCode
                                item.price = temp.formattedPrice.removeSuffix(".00")
                                item.amountMicros = temp.priceAmountMicros
                            }
                        }
                        _productDetailList.add(item)
                    }
                }
                _productDetailsLiveData.postValue(productDetailList)
                subscriptionProvider.setProductDetailsList(ArrayList(productDetailsResult.productDetailsList!!))
                setBillingState(BillingState.CONSOLE_PRODUCTS_SUB_AVAILABLE)
            }
        } else {
            setBillingState(BillingState.CONSOLE_PRODUCTS_SUB_FETCHING_FAILED)
        }
    }

    protected fun purchaseInApp(activity: Activity?, onPurchaseListener: OnPurchaseListener) {
        this.onPurchaseListener = onPurchaseListener
        if (checkValidationsInApp(activity)) return

        inAppProvider.getProductDetail()?.let { productDetail ->
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetail)
                    .build()
            )
            val billingFlowParams =
                BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList)
                    .build()

            // Launch the billing flow
            val billingResult = billingClient.launchBillingFlow(activity!!, billingFlowParams)

            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> setBillingState(BillingState.LAUNCHING_FLOW_INVOCATION_SUCCESSFULLY)
                BillingClient.BillingResponseCode.USER_CANCELED -> setBillingState(BillingState.LAUNCHING_FLOW_INVOCATION_USER_CANCELLED)
                else -> setBillingState(BillingState.LAUNCHING_FLOW_INVOCATION_EXCEPTION_FOUND)
            }
        } ?: run {
            setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_NOT_EXIST)
        }
    }

    private fun checkValidationsInApp(activity: Activity?): Boolean {
        if (activity == null) {
            setBillingState(BillingState.ACTIVITY_REFERENCE_NOT_FOUND)
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (getBillingState() == BillingState.EMPTY_PRODUCT_ID_LIST) {
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (getBillingState() == BillingState.NO_INTERNET_CONNECTION) {
            if (isInternetConnected && onConnectionListener != null) {
                startBillingConnection(
                    productIdsList = inAppProvider.productSKUs(),
                    onConnectionListener!!
                )
                return true
            }
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }


        if (getBillingState() == BillingState.CONNECTION_FAILED || getBillingState() == BillingState.CONNECTION_DISCONNECTED || getBillingState() == BillingState.CONNECTION_ESTABLISHING) {
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (getBillingState() == BillingState.CONSOLE_PRODUCTS_IN_APP_FETCHING || getBillingState() == BillingState.CONSOLE_PRODUCTS_IN_APP_FETCHING_FAILED) {
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (inAppProvider.getProductDetail() == null) {
            setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_NOT_EXIST)
        }

        if (getBillingState() == BillingState.CONSOLE_PRODUCTS_IN_APP_NOT_EXIST) {
            onPurchaseListener?.onPurchaseResult(
                false,
                BillingState.CONSOLE_PRODUCTS_IN_APP_NOT_EXIST.message
            )
            return true
        }

        inAppProvider.productSKUs().forEach { id ->
            inAppProvider.getProductLists().forEach { productDetails ->
                if (id != productDetails.productId) {
                    setBillingState(BillingState.CONSOLE_PRODUCTS_IN_APP_NOT_FOUND)
                    onPurchaseListener?.onPurchaseResult(
                        false,
                        BillingState.CONSOLE_PRODUCTS_IN_APP_NOT_FOUND.message
                    )
                    return true
                }
            }
        }

        if (billingClient.isFeatureSupported(BillingClient.FeatureType.PRODUCT_DETAILS).responseCode != BillingClient.BillingResponseCode.OK) {
            setBillingState(BillingState.FEATURE_NOT_SUPPORTED)
            return true
        }
        return false
    }

    protected fun purchaseSub(
        activity: Activity?,
        subscriptionPlans: String,
        onPurchaseListener: OnPurchaseListener
    ) {
        Log.d(TAG, "purchaseSub: in")
        if (checkValidationsSub(activity)) return

        this.onPurchaseListener = onPurchaseListener

        Log.d(
            TAG,
            "purchaseSub: Starting: ${subscriptionProvider.getProductList(planType = PlanType.BASIC())}"
        )

        var prodDetails: ProductDetails? = null

        subscriptionProvider.getProductListDetails().forEach { productDetails ->
            prodDetails = productDetails
        }

        Log.d(TAG, "purchaseSub: prodDetails : $prodDetails")

        if (prodDetails == null) {
            setBillingState(BillingState.CONSOLE_PRODUCTS_SUB_NOT_FOUND)
            return
        }

        // Retrieve all offers the user is eligible for.
        val offers = prodDetails!!.subscriptionOfferDetails?.let {
            retrieveEligibleOffers(offerDetails = it, tag = subscriptionPlans)
        }

        //  Get the offer id token of the lowest priced offer.
        val offerToken = offers?.let { leastPricedOfferToken(it) }

        offerToken?.let { token ->
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(prodDetails!!)
                    .setOfferToken(token).build()
            )
            val billingFlowParams =
                BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList)
                    .build()

            // Launch the billing flow
            val billingResult = billingClient.launchBillingFlow(activity!!, billingFlowParams)

            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> setBillingState(BillingState.LAUNCHING_FLOW_INVOCATION_SUCCESSFULLY)
                BillingClient.BillingResponseCode.USER_CANCELED -> setBillingState(BillingState.LAUNCHING_FLOW_INVOCATION_USER_CANCELLED)
                else -> setBillingState(BillingState.LAUNCHING_FLOW_INVOCATION_EXCEPTION_FOUND)
            }
        }
    }

    private fun checkValidationsSub(activity: Activity?): Boolean {
        if (activity == null) {
            setBillingState(BillingState.ACTIVITY_REFERENCE_NOT_FOUND)
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (getBillingState() == BillingState.NO_INTERNET_CONNECTION) {
            if (isInternetConnected && onConnectionListener != null) {
                startBillingConnection(
                    productIdsList = inAppProvider.productSKUs(),
                    onConnectionListener!!
                )
                return true
            }
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (getBillingState() == BillingState.CONNECTION_FAILED || getBillingState() == BillingState.CONNECTION_DISCONNECTED || getBillingState() == BillingState.CONNECTION_ESTABLISHING) {
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (getBillingState() == BillingState.CONSOLE_PRODUCTS_SUB_FETCHING || getBillingState() == BillingState.CONSOLE_PRODUCTS_SUB_FETCHING_FAILED) {
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
            return true
        }

        if (getBillingState() == BillingState.CONSOLE_PRODUCTS_SUB_NOT_EXIST) {
            onPurchaseListener?.onPurchaseResult(
                false,
                BillingState.CONSOLE_PRODUCTS_SUB_NOT_EXIST.message
            )
            return true
        }

        if (subscriptionProvider.getProductListDetails().isEmpty()) {
            setBillingState(BillingState.CONSOLE_PRODUCTS_SUB_NOT_FOUND)
            onPurchaseListener?.onPurchaseResult(
                false,
                BillingState.CONSOLE_PRODUCTS_SUB_NOT_FOUND.message
            )
            return true
        }

        if (billingClient.isFeatureSupported(BillingClient.FeatureType.PRODUCT_DETAILS).responseCode != BillingClient.BillingResponseCode.OK) {
            setBillingState(BillingState.FEATURE_NOT_SUPPORTED)
            return true
        }
        return false
    }

    /**
     * Retrieves all eligible base plans and offers using tags from ProductDetails.
     *
     * @param offerDetails offerDetails from a ProductDetails returned by the library.
     * @param tag string representing tags associated with offers and base plans.
     *
     * @return the eligible offers and base plans in a list.
     *
     */
    private fun retrieveEligibleOffers(
        offerDetails: MutableList<ProductDetails.SubscriptionOfferDetails>,
        tag: String
    ): List<ProductDetails.SubscriptionOfferDetails> {
        val eligibleOffers = emptyList<ProductDetails.SubscriptionOfferDetails>().toMutableList()
        offerDetails.forEach { offerDetail ->
            if (offerDetail.offerTags.contains(tag)) {
                eligibleOffers.add(offerDetail)
            }
        }
        return eligibleOffers
    }

    /**
     * Calculates the lowest priced offer amongst all eligible offers.
     * In this implementation the lowest price of all offers' pricing phases is returned.
     * It's possible the logic can be implemented differently.
     * For example, the lowest average price in terms of month could be returned instead.
     *
     * @param offerDetails List of of eligible offers and base plans.
     *
     * @return the offer id token of the lowest priced offer.
     *
     */
    private fun leastPricedOfferToken(offerDetails: List<ProductDetails.SubscriptionOfferDetails>): String {
        var offerToken = String()
        var leastPricedOffer: ProductDetails.SubscriptionOfferDetails
        var lowestPrice = Int.MAX_VALUE

        if (offerDetails.isNotEmpty()) {
            for (offer in offerDetails) {
                for (price in offer.pricingPhases.pricingPhaseList) {
                    if (price.priceAmountMicros < lowestPrice) {
                        lowestPrice = price.priceAmountMicros.toInt()
                        leastPricedOffer = offer
                        offerToken = leastPricedOffer.offerToken
                    }
                }
            }
        }
        return offerToken
    }

    /* --------------------------------------------------- Purchase Response  --------------------------------------------------- */

    private val purchasesUpdatedListener: PurchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult: BillingResult, purchaseMutableList: MutableList<Purchase>? ->
            Log.d(TAG, "purchasesUpdatedListener: $purchaseMutableList")

            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    setBillingState(BillingState.PURCHASED_SUCCESSFULLY)
                    handlePurchase(purchaseMutableList)
                    return@PurchasesUpdatedListener
                }

                BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {}
                BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {}
                BillingClient.BillingResponseCode.ERROR -> setBillingState(BillingState.PURCHASING_ERROR)
                BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED -> {}
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    setBillingState(BillingState.PURCHASING_ALREADY_OWNED)
                    onPurchaseListener?.onPurchaseResult(
                        true,
                        BillingState.PURCHASING_ALREADY_OWNED.message
                    )
                    return@PurchasesUpdatedListener
                }

                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {}
                BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {}
                BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {}
                BillingClient.BillingResponseCode.SERVICE_TIMEOUT -> {}
                BillingClient.BillingResponseCode.USER_CANCELED -> setBillingState(BillingState.PURCHASING_USER_CANCELLED)
            }
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
        }

    private fun handlePurchase(purchases: MutableList<Purchase>?) =
        CoroutineScope(Dispatchers.Main).launch {
            purchases?.forEach { purchase ->
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (purchase.isAcknowledged) {
                        setBillingState(BillingState.PURCHASED_SUCCESSFULLY)
                        onPurchaseListener?.onPurchaseResult(
                            true,
                            BillingState.PURCHASED_SUCCESSFULLY.message
                        )
                    } else {
                        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken).build()
                        withContext(Dispatchers.IO) {
                            billingClient.acknowledgePurchase(
                                acknowledgePurchaseParams,
                                acknowledgePurchaseResponseListener
                            )
                        }
                    }
                    return@launch
                } else {
                    setBillingState(BillingState.PURCHASING_FAILURE)
                }
            } ?: kotlin.run {
                setBillingState(BillingState.PURCHASING_USER_CANCELLED)
            }
            onPurchaseListener?.onPurchaseResult(false, getBillingState().message)
        }

    private val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener {
        if (it.responseCode == BillingClient.BillingResponseCode.OK) {
            setBillingState(BillingState.PURCHASED_SUCCESSFULLY)
            CoroutineScope(Dispatchers.Main).launch {
                onPurchaseListener?.onPurchaseResult(
                    true,
                    BillingState.PURCHASED_SUCCESSFULLY.message
                )
            }
            Log.d(TAG, "acknowledgePurchaseResponseListener: Acknowledged successfully")
        } else {
            Log.d(TAG, "acknowledgePurchaseResponseListener: Acknowledgment failure")
        }
    }

    /* ------------------------------------- Internet Connection ------------------------------------- */

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val isInternetConnected: Boolean
        get() {
            try {
                val network = connectivityManager.activeNetwork ?: return false
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network) ?: return false
                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } catch (ex: Exception) {
                return false
            }
        }

    companion object {
        const val TAG = "max_billing"
    }
}