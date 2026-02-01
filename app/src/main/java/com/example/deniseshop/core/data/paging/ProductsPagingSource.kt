package com.example.deniseshop.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.mappers.toProduct
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.domain.model.Product
import com.example.deniseshop.core.domain.model.ProductFilterParams

class ProductsPagingSource(
	private val filterParams: ProductFilterParams,
	private val settingDataSource: SettingDataSource,
	private val remote: RemoteDeniseShopDataSource,
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
			val data = remote.getProducts(
				filterParams = filterParams.copy(
					page = page,
					pageSize = pageSize
				)
			)

			//save search query for history suggestions
			if (filterParams.query.isNotEmpty() && data.isNotEmpty()){
				settingDataSource.saveSearchQuery(filterParams.query)
			}

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