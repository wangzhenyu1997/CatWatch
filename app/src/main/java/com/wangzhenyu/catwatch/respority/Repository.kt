package com.wangzhenyu.catwatch.respority

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wangzhenyu.catwatch.data.Hit
import com.wangzhenyu.catwatch.datasource.PixabayDataSource
import kotlinx.coroutines.flow.Flow

object Repository {

    private const val PAGE_SIZE = 50


    fun getPagingData(): Flow<PagingData<Hit>> {
        return Pager(config = PagingConfig(PAGE_SIZE),
            pagingSourceFactory = { PixabayDataSource() }
        ).flow
    }


}
