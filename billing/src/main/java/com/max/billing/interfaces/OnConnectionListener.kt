package com.max.billing.interfaces

import com.max.billing.data.PurchaseInfo

interface OnConnectionListener {
    fun onConnectionResult(isSuccess: Boolean, message: String) {}
    fun onOldPurchaseResult(isPurchased: Boolean, purchaseDetail: PurchaseInfo?)
}