package com.example.deniseshop.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.core.data.mappers.toFaq
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.domain.model.Faq

class FaqsPagingSource(
	private val remote: RemoteDeniseShopDataSource
): PagingSource<Int, Faq>() {
	override fun getRefreshKey(state: PagingState<Int, Faq>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Faq> {
		val page = params.key ?: 1
		val pageSize = params.loadSize

		return try {
			val data = remote.getFaqs(
				page = page,
				pageSize = pageSize,
			)

			LoadResult.Page(
				data = data.map { it.toFaq() },
				prevKey = if(page == 1) null else page -1,
				nextKey = if (data.isEmpty()) null else page + 1
			)
		}catch (e: Exception){
			LoadResult.Error(e)
		}
	}
}