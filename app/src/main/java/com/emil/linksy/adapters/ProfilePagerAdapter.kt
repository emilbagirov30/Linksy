package com.emil.linksy.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emil.linksy.presentation.ui.MomentFragment
import com.emil.linksy.presentation.ui.PostFragment

class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostFragment()
            1 -> MomentFragment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}