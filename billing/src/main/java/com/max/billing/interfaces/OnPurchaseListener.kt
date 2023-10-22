package com.max.billing.interfaces

interface OnPurchaseListener {
    fun onPurchaseResult(isPurchaseSuccess: Boolean, message: String)
}