package com.max.billing.provider

import android.icu.text.SimpleDateFormat
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.max.billing.BuildConfig
import com.max.billing.data.PurchaseInfo
import com.max.billing.types.BillingType
import com.max.billing.types.Durations
import com.max.billing.types.MaxDateFormat
import com.max.billing.types.PlanType
import java.util.Locale

class InAppProvider {
    private var productSKU = arrayListOf<String>()
    private var productDetails = arrayListOf<ProductDetails>()
    private var debuggableSKU = arrayListOf<String>(
        "android.test.item_unavailable",
        "android.test.refunded",
        "android.test.canceled",
        "android.test.purchased"
    )

    fun addProducts(products: ArrayList<String>) {
        productSKU.clear()
        productSKU.addAll(products)
    }

    fun productSKUs(): ArrayList<String> = productSKU

    fun getProductList(): ArrayList<QueryProductDetailsParams.Product> {
        val productList = arrayListOf<QueryProductDetailsParams.Product>()
        if (BuildConfig.DEBUG) {
            debuggableSKU.forEach { id ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(BillingType.INAPP().getPurchasedSKU())
                    .setProductType(BillingType.INAPP().getTypeName())
                    .build()
            }
        } else {
            productSKU.forEach { id ->
                productList.add(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(id)
                        .setProductType(BillingType.INAPP().getTypeName())
                        .build()
                )
            }
        }

        return productList
    }

    fun getProductDetail(): ProductDetails? {
        if (productDetails.isEmpty()) {
            return null
        }

        return productDetails.first()
    }

    fun getProductLists(): ArrayList<ProductDetails> {
        return productDetails
    }

    fun setProductDetail(productDetails: ArrayList<ProductDetails>) {
        this.productDetails = productDetails
    }

    fun getPurchaseDetail(purchase: Purchase): PurchaseInfo {
        return PurchaseInfo(
            billingType = BillingType.INAPP(),
            duration = Durations.lifeTime(PlanType.BASIC()),
            SimpleDateFormat(MaxDateFormat.formatWithHoursAndSeconds, Locale.getDefault()).format(
                purchase.purchaseTime
            ),
            purchase.quantity,
            purchase.orderId.toString()
        )
    }


}