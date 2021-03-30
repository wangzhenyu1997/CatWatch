package com.wangzhenyu.catwatch.datasource

import  androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wangzhenyu.catwatch.data.Hit
import com.wangzhenyu.catwatch.service.CreateService
import com.wangzhenyu.catwatch.service.Service

//Int表示页数的数据类型，Hit表示每一项的数据类型
class PixabayDataSource : PagingSource<Int, Hit>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hit> {

        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val repoResponse =
                CreateService.create<Service>()
                    .searchFromPixabay("cat", pageSize, page)

            val repoItems: ArrayList<Hit> = repoResponse.hits

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoItems.isNotEmpty()) page + 1 else null


            LoadResult.Page(repoItems, prevKey, nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Hit>): Int? = null
}



