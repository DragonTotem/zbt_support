package com.zbt.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zbt.commonlibrary.PrettyImageView
import java.util.*

/**
 * Author       :zbt
 * Date         :2020/10/26 下午2:34
 * Version      :1.0.0
 * Description  :
 */
class MainFragment : Fragment() {

    private lateinit var pageTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageTv = view.findViewById(R.id.tv_page)
        pageTv.text = "1"

        val imageView1 = view.findViewById<PrettyImageView>(R.id.pic_test1)
        imageView1.setImageResource(R.drawable.ic_launcher_background)

        val imageView2 = view.findViewById<PrettyImageView>(R.id.piv_test2)
        imageView2.setImageResource(R.drawable.ic_launcher_background)
    }

    fun setPageIndex(page: Int) {
        pageTv.text = page.toString()
    }

    fun test2() {
        val isOddNumber = { number: Int ->
            println("number is $number")
            number % 2 == 1
        }

        println(isOddNumber)
        println(isOddNumber.invoke(100))
    }

    fun test3() {
        val isOddNumber = { number: Int ->
            number % 2 == 1
            println("number is $number")
        }

        println(isOddNumber)
        println(isOddNumber.invoke(100))
    }

    fun test4() {
        val nameList =
            listOf("Kotlin", "Java", "Python", "JavaScript", "Scala", "C", "C++", "Go", "Swift")
        nameList.filter {
            it.startsWith("K")
        }.map {
            "$it is a very good language"
        }.forEach {
            println(it)
        }
    }

    fun test5() {
        var count = 0//声明非final类型
        pageTv.setOnClickListener {
            println(count++)//直接访问和修改非final类型的变量
        }
    }
}