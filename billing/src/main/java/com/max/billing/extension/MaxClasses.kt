package com.max.billing.extension

import com.max.billing.types.Durations
import com.max.billing.types.PlanType

fun getDurationFromId(products: String): Durations {
    return if (products.startsWith("yearly_")) {
        Durations.yearly(getPlanType(products))
    } else if (products.startsWith("semi_yearly_")) {
        Durations.sixMonths(getPlanType(products))
    } else if (products.startsWith("quaterly_")) {
        Durations.quaterly(getPlanType(products))
    } else if (products.startsWith("monthly_")) {
        Durations.monthly(getPlanType(products))
    } else if (products.startsWith("weekly_")) {
        Durations.weekly(getPlanType(products))
    } else if (products.startsWith("life_time_")) {
        Durations.lifeTime(getPlanType(products))
    } else {
        Durations.monthly(getPlanType(products))
    }
}

fun getPlanType(productId: String): PlanType {
    return if (productId.contains(PlanType.BASIC().getTypeName())) {
        PlanType.BASIC()
    } else if (productId.contains(PlanType.PRO().getTypeName())) {
        PlanType.PRO()
    } else if (productId.contains(PlanType.EXTREME().getTypeName())) {
        PlanType.EXTREME()
    } else {
        PlanType.BASIC()
    }
}