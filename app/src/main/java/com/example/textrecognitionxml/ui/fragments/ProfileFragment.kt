package com.example.textrecognitionxml.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.textrecognitionxml.R


class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireActivity().getSharedPreferences("NSBTAS_APP_SETTINGS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val btnChangeTheme: ImageView = view.findViewById(R.id.btnChangeTheme)

        val isLightThemeActive = sharedPreferences.getBoolean("isLightThemeActive", true)
        if (isLightThemeActive) {
            btnChangeTheme.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_dark_mode
                )
            );
        } else {
            btnChangeTheme.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_light_mode
                )
            );
            btnChangeTheme.imageTintList = ContextCompat.getColorStateList(
                requireContext(),
                R.color.font_color_light
            );
        }

        btnChangeTheme.setOnClickListener {
            if (isLightThemeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("isLightThemeActive", false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("isLightThemeActive", true)
            }
            editor.apply()
        }
    }
}