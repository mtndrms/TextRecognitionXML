package com.example.textrecognitionxml.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.models.Document

class DocumentFolderRecyclerViewAdapter(private val documents: MutableList<Document>) :
    RecyclerView.Adapter<DocumentFolderRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val context: TextView
        val date: TextView

        init {
            title = view.findViewById(R.id.tvDocumentTitle)
            context = view.findViewById(R.id.tvDocumentDetailOverview)
            date = view.findViewById(R.id.tvDocumentDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rv_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.run {
            title.text = documents[position].title
            context.text = documents[position].context
            date.text = documents[position].createdAt.toString()

            itemView.setOnClickListener {
                // TODO: Navigate to detailed document activity here!
            }

            itemView.setOnLongClickListener {
                // TODO: Show same options here (delete, edit, share etc).
                true
            }
        }
    }

    override fun getItemCount(): Int = documents.size
}