package com.max.billing.provider

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.max.billing.data.PurchaseInfo
import com.max.billing.extension.getDurationFromId
import com.max.billing.types.BillingType
import com.max.billing.types.Durations
import com.max.billing.types.MaxDateFormat
import com.max.billing.types.PlanType
import java.text.SimpleDateFormat
import java.util.Locale

class SubscriptionProvider {

    private var productDetailsList: ArrayList<ProductDetails> = arrayListOf()

    fun productIdsList(planType: PlanType) = arrayListOf(
        Durations.weekly(planType),
        Durations.monthly(planType),
        Durations.quaterly(planType),
        Durations.sixMonths(planType),
        Durations.lifeTime(planType)
    )

    fun getProductList(planType: PlanType): ArrayList<QueryProductDetailsParams.Product> {
        val products = ArrayList<QueryProductDetailsParams.Product>()
        productIdsList(planType).forEach { id ->
            products.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(id.get().id)
                    .setProductType(BillingType.SUBSCRIPTION().getTypeName())
                    .build()
            )
        }

        return products
    }

    fun setProductDetailsList(productDetailsList: ArrayList<ProductDetails>) {
        this.productDetailsList = productDetailsList
    }

    fun getProductListDetails(): ArrayList<ProductDetails> {
        return productDetailsList
    }

    fun getPurchaseDetail(purchase: Purchase): PurchaseInfo {
        val products = purchase.products[0]
        return PurchaseInfo(
            BillingType.SUBSCRIPTION(),
            getDurationFromId(products),
            SimpleDateFormat(
                MaxDateFormat.formatWithHoursAndSeconds,
                Locale.getDefault()
            ).format(purchase.purchaseTime),
            purchase.quantity,
            purchase.orderId,
            purchase.isAutoRenewing
        )
    }
}