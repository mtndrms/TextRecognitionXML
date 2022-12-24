package com.example.textrecognitionxml.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.ui.activities.MainActivity

object Helpers {
    fun changeFragment(fragment: Fragment, activity: MainActivity) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.run {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack(null)
            commit()
        }
    }

    fun setTheme(activity: Activity) {
        val sharedPreferences =
            activity.getSharedPreferences("NSBTAS_APP_SETTINGS", Context.MODE_PRIVATE)
        val isLightThemeActive = sharedPreferences.getBoolean("isLightThemeActive", true)
        if (isLightThemeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}