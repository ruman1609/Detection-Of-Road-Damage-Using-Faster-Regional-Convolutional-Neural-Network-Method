package com.rudyrachman16.skripsi.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.setPadding
import com.rudyrachman16.skripsi.R
import com.rudyrachman16.skripsi.utils.NumberUtils.toPx

class TextButton : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private var icon: Drawable? = null
    private var title: String = ""

    private fun init(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TextButton, 0, 0).apply {
            try {
                icon = getDrawable(R.styleable.TextButton_icon)
                title = getString(R.styleable.TextButton_text) ?: ""
                generate()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                recycle()
            }
        }
    }

    private fun generate() {
        text = title
        textSize = 28f
        compoundDrawablePadding = 12.toPx.toInt()
        gravity = Gravity.CENTER_VERTICAL

        if (icon != null) {
            val iconCompat = DrawableCompat.wrap(icon!!)
            setCompoundDrawablesWithIntrinsicBounds(iconCompat, null, null, null)
        }

        background = ContextCompat.getDrawable(context, R.drawable.text_button_ripple)
        setPadding(8.toPx.toInt())
        isFocusable = true
        isClickable = true

        invalidate()
        requestLayout()
    }
}