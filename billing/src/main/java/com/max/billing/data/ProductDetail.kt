package com.max.billing.data

import com.max.billing.types.BillingType
import com.max.billing.types.Durations

data class ProductDetail(
    var id: String = "",
    var currency: String = "",
    var price: String = "",
    var duration: Durations,
    var billingType: BillingType,
    var trial: Boolean = false,
    var amountMicros: Long? = 0L
)