package com.zbt.sample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.zbt.sample.java.SecondActivity

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity.class"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.w(TAG, "start")
//        testArray()
//        test1()
//        test2()
//        test3()
//        test4()
//        test5()
//        test6()
        test7()

        initView()
    }

    private fun initView() {
        findViewById<View>(R.id.fab).setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        }

        val fragment1 = MainFragment()
        val fragment2 = MainFragment()
        val fragments = mutableListOf<MainFragment>()
        fragments.add(fragment1)
        fragments.add(fragment2)

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val pagerAdapter = FragmentAdapter(fragments, supportFragmentManager)
        viewPager.adapter = pagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val fragment = fragments[position]
                fragment.setPageIndex(position + 1)
                fragment.test2()
                fragment.test3()
                fragment.test4()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        // 存
        // 存
        val editor: SharedPreferences.Editor = getSharedPreferences("data", MODE_PRIVATE).edit()
        editor.putString("name", "ZhangSan")
        editor.putInt("age", 12)
        editor.putBoolean("isMarried", false)
        editor.commit() // 或editor.apply()
        editor.apply()

    }

    fun test7() {
        val list = listOf<Int>(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        var all = list.all { it >= 0 }
        println("all ->  $all")
        //打印 all ->  true
        all = list.all { it > 10 }
        println("all -> $all")
        //打印 all -> false
        all = list.all { it > 7 }
        // 打印 all -> false
        println("all -> $all")
    }

    private fun testArray() {
        val langs = listOf("C", "C++", "Java", "Python", "JavaScript")

        langs
            .filter { it.startsWith("C") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }
    }

    fun computeRunTime() {
        (0..10000000)
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .count { it < 10 }
            .run {
                println("by using list way, result is : $this")
            }
    }

    fun computeRunTime1() {
        (0..10000000)
            .asSequence()
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .count { it < 10 }
            .run {
                println("by using list way, result is : $this")
            }
    }

    fun test1() {
        (0..6)
            .asSequence()
            .map {//map返回是Sequence<T>，故它属于中间操作
                println("map: $it")
                return@map it + 1
            }
            .filter {//filter返回是Sequence<T>，故它属于中间操作
                println("filter: $it")
                return@filter it % 2 == 0
            }
    }

    fun test2() {
        (0..6)
            .asSequence()
            .map {//map返回是Sequence<T>，故它属于中间操作
                println("test2 map: $it")
                return@map it + 1
            }
            .filter {//filter返回是Sequence<T>，故它属于中间操作
                println("test2 filter: $it")
                return@filter it % 2 == 0
            }
            .count {//count返回是Int，返回的是一个结果，故它属于末端操作
                it < 6
            }
            .run {
                println("result is $this");
            }
    }

    fun computeRunTime(action: (() -> Unit)?) {
        val startTime = System.currentTimeMillis()
        action?.invoke()
        Log.w(TAG, "the code run time is ${System.currentTimeMillis() - startTime} ms")
    }

    fun test3() = computeRunTime {
        (0..10000000)//10000000数据量级
            .asSequence()
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .count { it < 100 }
            .run {
                Log.w(TAG, "by using sequence result is $this")
            }
    }

    fun test4() = computeRunTime {
        (0..10000000)//10000000数据量级
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .count { it < 100 }
            .run {
                Log.w(TAG, "by using list result is $this")
            }
    }

    fun test5() = computeRunTime {
        (0..1000)//1000数据量级
            .asSequence()
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .count { it < 100 }
            .run {
                Log.w(TAG, "by using sequence result is $this")
            }
    }

    fun test6() = computeRunTime {
        (0..1000)//1000数据量级
            .map { it + 1 }
            .filter { it % 2 == 0 }
            .count { it < 100 }
            .run {
                Log.w(TAG, "by using list result is $this")
            }
    }

    fun testForSharedPreferences() {
        val esp = getSharedPreferences("data", Context.MODE_PRIVATE)
    }
}