/*
 * Copyright (c) 2023.
 */

package com.max.billing

import android.app.Activity
import android.content.Context
import com.max.billing.helper.BillingHelper
import com.max.billing.interfaces.OnConnectionListener
import com.max.billing.interfaces.OnPurchaseListener

class BillingManager(context: Context) : BillingHelper(context) {

    override fun setCheckForSubscription(isCheckRequired: Boolean) {
        checkForSubscription = isCheckRequired
    }

    override fun startConnection(
        productIdsList: ArrayList<String>,
        onConnectionListener: OnConnectionListener
    ) = startBillingConnection(productIdsList, onConnectionListener)

    fun makeInAppPurchase(activity: Activity?, onPurchaseListener: OnPurchaseListener) =
        purchaseInApp(activity, onPurchaseListener)

    fun makeSubPurchase(
        activity: Activity?,
        subscriptionPlans: String,
        onPurchaseListener: OnPurchaseListener
    ) = purchaseSub(activity, subscriptionPlans, onPurchaseListener)

}