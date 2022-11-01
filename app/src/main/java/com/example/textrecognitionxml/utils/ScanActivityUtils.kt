package com.example.textrecognitionxml.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment

object ScanActivityUtils {
    var extractedText = ""
    var bitmap = createBitmap(1,1)
}