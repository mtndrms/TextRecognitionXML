package com.example.textrecognitionxml.views

import android.content.Context
import android.util.AttributeSet

class TextArea @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    init {
        this.background = null
    }
}