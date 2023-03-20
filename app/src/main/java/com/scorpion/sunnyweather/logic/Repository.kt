package com.scorpion.sunnyweather.logic

import androidx.lifecycle.liveData
import com.scorpion.sunnyweather.logic.model.Place
import com.scorpion.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库层代码
 * Repository单例类，作为仓库层统一封装入口
 * 仓库层有点像数据获取与缓存的中间层
 * 仓库层中定义的方法，为了能将异步获取的数据以响应式编程的方式通知给上一层，
 * 通常会返回一个LiveData对象
 */
object Repository {
    /**
     * LiveData()函数的 线程参数类型 指定成Dispatchers.IO，代码块中
     * 所有代码就运行在子线程中，因为Android不允许在主线程发起网络请求
     * 读写数据库之类的本地操作也不建议在主线程中进行，因此非常有必要在
     * 仓库层进行一次线程转换
     */
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            // 发起网络请求
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                // Kotlin内置的Result.success()方法，包装获取的城市列表数据
                Result.success(places)
            }else {
                // 内置failure()方法包装异常信息
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            // 内置failure()方法包装异常信息
            Result.failure<List<Place>>(e)
        }

        /**
         * emit()方法将包装结果发射出去
         * emit()方法类似于调用LiveData的setValue()方法，通知数据变化
         */
        emit(result)
    }
}