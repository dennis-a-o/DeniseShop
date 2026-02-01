package com.example.deniseshop.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.core.data.mappers.toReview
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.domain.model.Review

class ReviewsPagingSource(
	private val productId: Long,
	private val remote: RemoteDeniseShopDataSource
): PagingSource<Int, Review>() {
	override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
		val page = params.key ?: 1
		val pageSize = params.loadSize

		return try {
			val data = remote.getReviews(
				productId = productId,
				page = page,
				pageSize = pageSize,
			)

			LoadResult.Page(
				data = data.map { it.toReview() },
				prevKey = if(page == 1) null else page -1,
				nextKey = if (data.isEmpty()) null else page + 1
			)
		}catch (e: Exception){
			LoadResult.Error(e)
		}
	}
}