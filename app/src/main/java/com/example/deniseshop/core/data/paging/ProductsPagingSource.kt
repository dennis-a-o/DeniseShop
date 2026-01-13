package com.example.deniseshop.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.core.data.mappers.toProduct
import com.example.deniseshop.core.data.network.RetrofitDeniseShopNetworkApi
import com.example.deniseshop.core.domain.model.Product
import com.example.deniseshop.core.domain.model.ProductFilterParams

class ProductsPagingSource(
	private val api: RetrofitDeniseShopNetworkApi,
	private val filterParams: ProductFilterParams
): PagingSource<Int, Product>() {
	override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
		return state.anchorPosition?.let { anchorPosition ->
			state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
				?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
		}
	}

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
		val page = params.key ?: 1
		val pageSize = params.loadSize

		return try {
			val data = api.getProducts(
				query = filterParams.query,
				page = page,
				pageSize = pageSize,
				sortBy = filterParams.sortBy.value,
				minPrice = filterParams.minPrice,
				maxPrice = filterParams.maxPrice,
				categories = filterParams.categories,
				brands = filterParams.brands,
				colors = filterParams.categories,
				sizes = filterParams.sizes,
				rating = filterParams.rating
			)

			LoadResult.Page(
				data = data.map { it.toProduct() },
				prevKey = if(page == 1) null else page -1,
				nextKey = if (data.isEmpty()) null else page + 1
			)
		}catch (e: Exception){
			LoadResult.Error(e)
		}
	}
}