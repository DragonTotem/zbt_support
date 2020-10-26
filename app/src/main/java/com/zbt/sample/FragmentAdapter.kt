package com.zbt.sample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Author       :zbt
 * Date         :2020/10/26 下午2:54
 * Version      :1.0.0
 * Description  :fragment适配器
 */
class FragmentAdapter(fragments: List<Fragment>, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val fragments = fragments

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}