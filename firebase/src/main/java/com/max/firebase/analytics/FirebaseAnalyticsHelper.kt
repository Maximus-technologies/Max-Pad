package com.max.firebase.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseAnalyticsHelper(private val firebaseAnalytics: FirebaseAnalytics) {

    fun logScreenView(screenName: String) {
        // Log a screen view event
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logButtonClick(buttonName: String) {
        // Log a button click event
        val bundle = Bundle()
        bundle.putString("button_name", buttonName)
        firebaseAnalytics.logEvent("button_click", bundle)
    }

    fun logEvent(eventName: String) {
        // Log a button click event
        val bundle = Bundle()
        bundle.putString("event_name", eventName)
        firebaseAnalytics.logEvent("custom_event", bundle)
    }

    fun logEvent(eventName: String, eventValue: String) {
        // Log a button click event
        val bundle = Bundle()
        bundle.putString("event_value", eventValue)
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    fun logPurchase(purchaseAmount: Double, currency: String, itemId: String) {
        // Log a purchase event
        val bundle = Bundle()
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, purchaseAmount)
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
    }

    fun logShareApp(contentType: String, itemId: String, method: String) {
        // Log a user registration event
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        bundle.putString(FirebaseAnalytics.Param.METHOD, method)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle)
    }

}
