package com.max.billing.extension

import android.icu.text.SimpleDateFormat
import com.max.billing.types.MaxDateFormat
import java.util.Locale

fun formatMaxDate(): SimpleDateFormat {
    return SimpleDateFormat(
        MaxDateFormat.formatWithHoursAndSeconds,
        Locale.getDefault()
    )
}