package com.max.billing.data

import com.max.billing.types.BillingType
import com.max.billing.types.Durations

data class PurchaseInfo(
    val billingType: BillingType,
    val duration: Durations,
    val timeOfPurchase: String,
    val quantity: Int = 0,
    val orderId: String?,
    val isAutoRenewing: Boolean = false
)