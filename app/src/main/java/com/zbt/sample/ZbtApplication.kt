package com.zbt.sample

import android.app.Application
import com.tencent.mmkv.MMKV

/**
 * Author       :zbt
 * Date         :2020/10/28 上午10:05
 * Version      :1.0.0
 * Description  :
 */
class ZbtApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MMKV.initialize(this)
    }
}