package com.example.deniseshop.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.core.data.mappers.toBrand
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.domain.model.Brand

class BrandsPagingSource(
	private val remote: RemoteDeniseShopDataSource
): PagingSource<Int, Brand>() {
	override fun getRefreshKey(state: PagingState<Int, Brand>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Brand> {
		val page = params.key ?: 1
		val pageSize = params.loadSize

		return try {
			val data = remote.getBrands(
				page = page,
				pageSize = pageSize,
			)

			LoadResult.Page(
				data = data.map { it.toBrand() },
				prevKey = if(page == 1) null else page -1,
				nextKey = if (data.isEmpty()) null else page + 1
			)
		}catch (e: Exception){
			LoadResult.Error(e)
		}
	}
}