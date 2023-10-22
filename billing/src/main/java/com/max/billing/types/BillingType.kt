package com.max.billing.types

sealed class BillingType {
    class INAPP : BillingType() {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }

        fun getTypeName(): String = "inapp"

        fun getPurchasedSKU(): String = "android.test.purchased"

        fun getRefundedSKU(): String = "android.test.refunded"

        fun getCancelledSKU(): String = "android.test.canceled"

        fun getItemUnavailableSKU(): String = "android.test.item_unavailable"

    }

    class SUBSCRIPTION : BillingType() {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }

        fun getTypeName(): String = "subs"

    }
}