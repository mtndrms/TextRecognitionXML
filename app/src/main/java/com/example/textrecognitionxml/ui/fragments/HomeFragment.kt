package com.example.textrecognitionxml.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.utils.FakeDatabase
import com.example.textrecognitionxml.views.FolderCard
import com.google.android.material.card.MaterialCardView

class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val llFolderCardsContainer: LinearLayout = view.findViewById(R.id.llFolderCardsContainer)

        FakeDatabase.generateFolderAndItsDocuments()
        FakeDatabase.folders.forEach {
            llFolderCardsContainer.addView(FolderCard(requireActivity(), it))
        }
    }
}