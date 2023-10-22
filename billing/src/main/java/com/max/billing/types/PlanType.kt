package com.max.billing.types

sealed class PlanType {
    class BASIC : PlanType() {
        fun getTypeName(): String {
            return "basic"
        }

        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class PRO : PlanType() {
        fun getTypeName(): String {
            return "pro"
        }

        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class EXTREME : PlanType() {
        fun getTypeName(): String {
            return "extreme"
        }

        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }
}