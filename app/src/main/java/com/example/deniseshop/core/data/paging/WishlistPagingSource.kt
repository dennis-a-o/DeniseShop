package com.example.deniseshop.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.core.data.mappers.toWishlist
import com.example.deniseshop.core.data.network.RetrofitDeniseShopNetworkApi
import com.example.deniseshop.core.domain.model.Wishlist

class WishlistPagingSource(
	private val api: RetrofitDeniseShopNetworkApi
): PagingSource<Int, Wishlist>() {
	override fun getRefreshKey(state: PagingState<Int, Wishlist>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wishlist> {
		val page = params.key ?: 1
		val pageSize = params.loadSize

		return try {
			val data = api.getWishlists(page = page, pageSize = pageSize)

			LoadResult.Page(
				data = data.map { it.toWishlist() },
				prevKey = if(page == 1) null else page -1,
				nextKey = if (data.isEmpty()) null else page + 1
			)
		}catch (e: Exception){
			LoadResult.Error(e)
		}
	}
}