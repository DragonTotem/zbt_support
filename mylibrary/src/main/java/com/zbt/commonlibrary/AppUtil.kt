package com.zbt.commonlibrary

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils
import androidx.annotation.Nullable
import java.lang.reflect.Method
import android.os.Process


/**
 * Author       :zbt
 * Date         :2020/10/21 下午2:29
 * Version      :1.0.0
 * Description  :
 */
class AppUtil {
    /**
     * @return 当前进程名
     */
    @Nullable
    fun getCurrentProcessName(context: Context, currentProcessName: String): String? {
        if (!TextUtils.isEmpty(currentProcessName)) {
            return currentProcessName
        }

        var processName: String? = null

        //1)通过Application的API获取当前进程名
        processName = getCurrentProcessNameByApplication()
        if (!TextUtils.isEmpty(processName)) {
            return processName
        }

        //2)通过反射ActivityThread获取当前进程名
        processName = getCurrentProcessNameByActivityThread()
        if (!TextUtils.isEmpty(processName)) {
            return processName
        }

        //3)通过ActivityManager获取当前进程名
        processName = getCurrentProcessNameByActivityManager(context)
        return processName
    }


    /**
     * 通过Application新的API获取进程名，无需反射，无需IPC，效率最高。
     */
    fun getCurrentProcessNameByApplication(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) Application.getProcessName() else null
    }

    /**
     * 通过反射ActivityThread获取进程名，避免了ipc
     */
    fun getCurrentProcessNameByActivityThread(): String? {
        var processName: String? = null
        try {
            val declaredMethod: Method = Class.forName(
                "android.app.ActivityThread", false,
                Application::class.java.getClassLoader()
            )
                .getDeclaredMethod("currentProcessName", *arrayOfNulls<Class<*>?>(0))
            declaredMethod.setAccessible(true)
            val invoke: Any = declaredMethod.invoke(null, arrayOfNulls<Any>(0))
            if (invoke is String) {
                processName = invoke
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return processName
    }

    /**
     * 通过ActivityManager 获取进程名，需要IPC通信
     */
    fun getCurrentProcessNameByActivityManager(context: Context): String? {
        if (context == null) {
            return null
        }
        val pid: Int = Process.myPid()
        val am: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (am != null) {
            val runningAppList: List<ActivityManager.RunningAppProcessInfo> =
                am.getRunningAppProcesses()
            if (runningAppList != null) {
                for (processInfo in runningAppList) {
                    if (processInfo.pid === pid) {
                        return processInfo.processName
                    }
                }
            }
        }
        return null
    }
}