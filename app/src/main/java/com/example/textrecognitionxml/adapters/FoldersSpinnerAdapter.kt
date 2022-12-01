package com.example.textrecognitionxml.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.models.Folder

class FoldersSpinnerAdapter(
    mContext: Context,
    viewResourceId: Int,
    private var folders: List<Folder>
) :
    ArrayAdapter<Folder>(mContext, viewResourceId, folders) {
    private val inflater: LayoutInflater =
        mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private class ViewHolder(row: View?) {
        val tvFolderName: TextView

        init {
            tvFolderName = row?.findViewById(R.id.tvFolderName) as TextView
        }
    }

    override fun getCount(): Int {
        return folders.size
    }

    override fun getItem(position: Int): Folder {
        return folders[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_spinner_folder, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.tvFolderName.text = folders[position].name
        return view
    }
}