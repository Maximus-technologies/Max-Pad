package com.max.billing.provider

import com.max.billing.data.ProductInfo

abstract class DetailsProvider {
    abstract fun get() : ProductInfo
}