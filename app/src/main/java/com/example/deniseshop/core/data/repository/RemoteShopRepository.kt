package com.example.deniseshop.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.deniseshop.core.data.mappers.toCategory
import com.example.deniseshop.core.data.mappers.toHome
import com.example.deniseshop.core.data.mappers.toProductFilter
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.data.network.RetrofitDeniseShopNetworkApi
import com.example.deniseshop.core.data.paging.ProductsPagingSource
import com.example.deniseshop.core.data.paging.WishlistPagingSource
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.Product
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.Wishlist
import com.example.deniseshop.core.domain.repository.ShopRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteShopRepository @Inject constructor(
	private val remoteDeniseShopDataSource: RemoteDeniseShopDataSource,
	private val api: RetrofitDeniseShopNetworkApi
): ShopRepository {
	override suspend fun getHome(): Result<Home, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getHome()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toHome())
		}
	}

	override suspend fun getCategories(): Result<List<Category>, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getCategories()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success( data = res.data.map { it.toCategory() })
		}
	}

	override  fun getWishlists(limit: Int): Flow<PagingData<Wishlist>> {
		return Pager(
			config = PagingConfig(pageSize = limit),
			pagingSourceFactory = {
				WishlistPagingSource(
					api = api
				)
			}
		).flow
	}

	override suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.addToWishlist(productId)
	}

	override suspend fun removeWishlist(id: Long): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.removeWishlist(id)
	}

	override fun getProducts(filterParams: ProductFilterParams): Flow<PagingData<Product>> {
		return Pager(
			config = PagingConfig(pageSize = filterParams.pageSize),
			pagingSourceFactory = {
				ProductsPagingSource(
					api = api,
					filterParams = filterParams
				)
			}
		).flow
	}

	override suspend fun getProductFilter(
		categoryId: Long,
		brandId: Long
	): Result<ProductFilter, DataError.Remote> {
		return when(
			val res = remoteDeniseShopDataSource.getProductFilter(
				categoryId = categoryId,
				brandId = brandId
			)
		) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success( data = res.data.toProductFilter())
		}
	}
}