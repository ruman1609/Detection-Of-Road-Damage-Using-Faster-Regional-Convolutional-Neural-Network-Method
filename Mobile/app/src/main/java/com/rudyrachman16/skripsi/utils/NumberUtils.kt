package com.rudyrachman16.skripsi.utils

import android.content.res.Resources
import android.util.TypedValue

object NumberUtils {
    val Number.toPx get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics)
}