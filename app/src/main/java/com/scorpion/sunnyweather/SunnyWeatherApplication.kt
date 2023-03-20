package com.scorpion.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object {
        const val TOKEN = "hfzbr5b8vvxxh92a"
        // 注解：让Android Studio忽略内存泄漏警告提示
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        // Application的Context，全局只存在一份，在整个应用程序
        // 的生命周期内都不会回收，不存在内存泄漏风险。
        context = applicationContext
    }
}