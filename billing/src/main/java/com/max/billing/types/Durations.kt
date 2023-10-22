package com.max.billing.types

import com.max.billing.data.ProductInfo
import com.max.billing.provider.DetailsProvider

sealed class Durations() : DetailsProvider() {
    class yearly(val planType: PlanType) : Durations() {
        override fun get(): ProductInfo {
            val prefix = "yearly_"
            when (planType) {
                PlanType.BASIC() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }

                PlanType.PRO() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.PRO().getTypeName()),
                        name = prefix.plus(PlanType.PRO().getTypeName())
                    )
                }

                PlanType.EXTREME() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.EXTREME().getTypeName()),
                        name = prefix.plus(PlanType.EXTREME().getTypeName())
                    )
                }

                else -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }
            }
        }
    }

    class sixMonths(val planType: PlanType) : Durations() {
        override fun get(): ProductInfo {
            val prefix = "semi_yearly_"
            when (planType) {
                PlanType.BASIC() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }

                PlanType.PRO() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.PRO().getTypeName()),
                        name = prefix.plus(PlanType.PRO().getTypeName())
                    )
                }

                PlanType.EXTREME() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.EXTREME().getTypeName()),
                        name = prefix.plus(PlanType.EXTREME().getTypeName())
                    )
                }

                else -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }
            }
        }
    }

    class quaterly(val planType: PlanType) : Durations() {
        override fun get(): ProductInfo {
            val prefix = "quaterly_"
            when (planType) {
                PlanType.BASIC() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }

                PlanType.PRO() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.PRO().getTypeName()),
                        name = prefix.plus(PlanType.PRO().getTypeName())
                    )
                }

                PlanType.EXTREME() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.EXTREME().getTypeName()),
                        name = prefix.plus(PlanType.EXTREME().getTypeName())
                    )
                }

                else -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }
            }
        }
    }

    class monthly(val planType: PlanType) : Durations() {
        override fun get(): ProductInfo {
            val prefix = "monthly_"
            when (planType) {
                PlanType.BASIC() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }

                PlanType.PRO() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.PRO().getTypeName()),
                        name = prefix.plus(PlanType.PRO().getTypeName())
                    )
                }

                PlanType.EXTREME() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.EXTREME().getTypeName()),
                        name = prefix.plus(PlanType.EXTREME().getTypeName())
                    )
                }

                else -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }
            }
        }
    }

    class weekly(val planType: PlanType) : Durations() {
        override fun get(): ProductInfo {
            val prefix = "weekly_"
            when (planType) {
                PlanType.BASIC() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }

                PlanType.PRO() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.PRO().getTypeName()),
                        name = prefix.plus(PlanType.PRO().getTypeName())
                    )
                }

                PlanType.EXTREME() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.EXTREME().getTypeName()),
                        name = prefix.plus(PlanType.EXTREME().getTypeName())
                    )
                }

                else -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }
            }
        }
    }

    class lifeTime(val planType: PlanType) : Durations() {
        override fun get(): ProductInfo {
            val prefix = "life_time_"
            when (planType) {
                PlanType.BASIC() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }

                PlanType.PRO() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.PRO().getTypeName()),
                        name = prefix.plus(PlanType.PRO().getTypeName())
                    )
                }

                PlanType.EXTREME() -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.EXTREME().getTypeName()),
                        name = prefix.plus(PlanType.EXTREME().getTypeName())
                    )
                }

                else -> {
                    return ProductInfo(
                        id = prefix.plus(PlanType.BASIC().getTypeName()),
                        name = prefix.plus(PlanType.BASIC().getTypeName())
                    )
                }
            }
        }
    }
}