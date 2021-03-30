package com.wangzhenyu.catwatch.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangzhenyu.catwatch.data.Hit
import com.wangzhenyu.catwatch.data.Pixabay
import com.wangzhenyu.catwatch.service.CreateService
import com.wangzhenyu.catwatch.service.Service
import com.wangzhenyu.catwatch.util.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {


    private val _photoListLiveData = MutableLiveData<ArrayList<Hit>>().also {
        it.value = ArrayList<Hit>()
    }

    val photoListLiveData: LiveData<ArrayList<Hit>>
        get() = _photoListLiveData

    fun getPhoto(question: String) {

        val service = CreateService.create<Service>()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val pixabay: Pixabay = service.searchFromPixabay(question,50,1)
                _photoListLiveData.postValue(pixabay.hits)
                LogUtil.d("RetrofitGirl", pixabay.hits.size.toString())
            } catch (e: Exception) {
                LogUtil.d("RetrofitGirl", "未获取数据")
            }
        }
    }

}