package com.emil.linksy.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emil.linksy.presentation.ui.page.OutsiderMomentFragment
import com.emil.linksy.presentation.ui.page.OutsiderPostFragment

class OutsiderProfilePagerAdapter(val id:Long,activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OutsiderPostFragment.newInstance(id)
            1 -> OutsiderMomentFragment.newInstance(id)
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}