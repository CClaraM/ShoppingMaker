package com.myapplication.shopingmarket.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.myapplication.shopingmarket.LoginTabFragment
import com.myapplication.shopingmarket.SignupTabFragment

class LoginAdapter(fm: FragmentManager?, private val context: Context, var totalTabs: Int) :
    FragmentPagerAdapter(
        fm!!
    ) {
    override fun getCount()=totalTabs


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                LoginTabFragment()
            }
            1 -> {
                SignupTabFragment()
            }
            else -> {
                LoginTabFragment()
            }
        }
    }
}