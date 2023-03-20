package com.scorpion.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * SunnyWeatherNetwork单例类：统一的网络数据源访问入口，对所有
 * 网络请求的API进行封装。
 * 这是一个非常关键的类，用到了许多高级技巧。
 */
object SunnyWeatherNetwork {
    // 创建PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create(PlaceService::class.java)

    /**
     * 定义searchPlaces()函数，调用PlaceService接口中定义的searchPlaces()方法，发起搜索城市数据请求
     * 将searchPlaces()函数声明称挂起函数
     */
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    /**
     * 泛型函数，挂起函数。在该挂起函数中调用suspendCoroutine()函数
     * 调用Call对象的enqueue()方法让Retrofit发起网络请求
     */
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(
                object : Callback<T> {
                    override fun onResponse(call: Call<T>, response: Response<T>) {
                        val body = response.body()
                        if (body != null) continuation.resume(body)
                        else continuation.resumeWithException(
                            RuntimeException("response body is null")
                        )
                    }

                    override fun onFailure(call: Call<T>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                }
            )
        }
    }
}