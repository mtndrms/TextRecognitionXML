package com.example.textrecognitionxml.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.models.Folder
import com.example.textrecognitionxml.ui.activities.FolderActivity
import com.google.android.material.card.MaterialCardView

@SuppressLint("ViewConstructor")
class FolderCard constructor(
    context: Context, folder: Folder
) : MaterialCardView(context) {

    init {
        val linearLayout = LinearLayout(context)
        val tvFolderTitle = TextView(context)
        val tvFolderSize = TextView(context)
        val ivFolderIcon = ImageView(context)

        val params = LayoutParams(
            resources.getDimension(R.dimen.folder_card_width).toInt(),
            resources.getDimension(R.dimen.folder_card_height).toInt()
        )
        params.marginEnd = getDpValue(10f)
        isClickable = true
        isCheckable = true
        radius = getDpValue(7f).toFloat()

        layoutParams = params

        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        linearLayout.run {
            orientation = LinearLayout.VERTICAL
            setPadding(getDpValue(15f), getDpValue(15f), getDpValue(15f), getDpValue(15f))
            setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
            layoutParams = linearLayoutParams
        }

        val folderIcon = ContextCompat.getDrawable(context, R.drawable.ic_folder)
        val ivLayoutParams = ViewGroup.LayoutParams(getDpValue(60f), getDpValue(60f))

        ivFolderIcon.contentDescription = resources.getString(R.string.folder_s_icon)
        folderIcon?.setTint(ContextCompat.getColor(context, R.color.font_color_dark))
        ivFolderIcon.setImageDrawable(folderIcon)
        ivFolderIcon.layoutParams = ivLayoutParams

        val textViewParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        tvFolderTitle.layoutParams = textViewParams
        tvFolderSize.layoutParams = textViewParams

        tvFolderTitle.setTextColor(ContextCompat.getColor(context, R.color.font_color_dark))
        tvFolderSize.setTextColor(ContextCompat.getColor(context, R.color.font_color_dark))

        tvFolderTitle.text = folder.name
        tvFolderSize.text = "${folder.documents.size} documents"

        linearLayout.addView(ivFolderIcon)
        linearLayout.addView(tvFolderTitle)
        linearLayout.addView(tvFolderSize)

        addView(linearLayout)

        setOnClickListener {
            val intent = Intent(context, FolderActivity::class.java)
            intent.putExtra("folderId", folder.id)
            context.startActivity(intent)
        }
    }

    private fun getDpValue(value: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
        ).toInt()
    }
}