package com.tecsup.boticaphar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3 // Tres fragmentos

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PerfilFragment() // Cambia por tu clase de fragmento real
            1 -> HomeFragment() // Cambia por tu clase de fragmento real
            2 -> CarritoFragment() // Cambia por tu clase de fragmento real
            else -> HomeFragment()
        }
    }
}
