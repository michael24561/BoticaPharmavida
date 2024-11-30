package com.tecsup.boticaphar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3 // Tres fragmentos

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PerfilFragment()
            1 -> HomeFragment()
            2 -> CarritoFragment()
            else -> HomeFragment()
        }
    }
}
