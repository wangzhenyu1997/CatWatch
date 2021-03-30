package com.wangzhenyu.catwatch.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wangzhenyu.catwatch.data.Hit
import com.wangzhenyu.catwatch.respority.Repository
import kotlinx.coroutines.flow.Flow

class OneViewModel : ViewModel() {
    fun getPagingData(): Flow<PagingData<Hit>> {
        return Repository.getPagingData().cachedIn(viewModelScope)
    }
}