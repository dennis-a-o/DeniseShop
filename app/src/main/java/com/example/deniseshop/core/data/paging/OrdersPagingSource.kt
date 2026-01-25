package com.example.deniseshop.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.core.data.mappers.toOrder
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.domain.model.Order

class OrdersPagingSource(
	private val remote: RemoteDeniseShopDataSource
): PagingSource<Int, Order>() {
	override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
		val page = params.key ?: 1
		val pageSize = params.loadSize

		return try {
			val data = remote.getOrders(
				page = page,
				pageSize = pageSize,
			)

			LoadResult.Page(
				data = data.map { it.toOrder() },
				prevKey = if(page == 1) null else page -1,
				nextKey = if (data.isEmpty()) null else page + 1
			)
		}catch (e: Exception){
			LoadResult.Error(e)
		}
	}
}