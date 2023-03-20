package com.scorpion.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.scorpion.sunnyweather.logic.model.Place
import com.scorpion.sunnyweather.logic.Repository

// ViewModel相当于逻辑层和UI层之间的桥梁（虽然它更偏向于逻辑层）
// 通常和Activity/Fragment一一对应
class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    // UI层接口
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}