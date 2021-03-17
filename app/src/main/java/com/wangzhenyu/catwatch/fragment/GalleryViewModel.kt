package com.wangzhenyu.catwatch.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangzhenyu.catwatch.data.Girl
import com.wangzhenyu.catwatch.data.GirlPhoto
import com.wangzhenyu.catwatch.service.CreateService
import com.wangzhenyu.catwatch.service.GirlService
import com.wangzhenyu.catwatch.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {


    private val _photoListLiveData = MutableLiveData<List<GirlPhoto>>().also {
        it.value = ArrayList<GirlPhoto>()
    }


    val photoListLiveData: LiveData<List<GirlPhoto>>
        get() = _photoListLiveData

    fun getGirlPhoto(number: String) {

        val service = CreateService.create<GirlService>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val girl: Girl = service.searchGirl(number)
                val list: ArrayList<GirlPhoto> = ArrayList<GirlPhoto>()
                for (photo in girl.data) {
                    list.add(GirlPhoto(photo.images[0]))
                }

                _photoListLiveData.postValue(list)
                LogUtil.d("RetrofitGirl", list.toString())
            } catch (e: Exception) {
                LogUtil.d("RetrofitGirl", "未获取数据")
            }
        }
    }

    //fun
}