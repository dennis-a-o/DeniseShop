package com.example.deniseshop.core.domain.repository

import androidx.paging.PagingData
import com.example.deniseshop.core.data.paging.CategoryProductsPagingSource
import com.example.deniseshop.core.data.paging.ProductsPagingSource
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.Wishlist
import kotlinx.coroutines.flow.Flow

interface ShopRepository {
	suspend fun getHome(): Result<Home,  DataError.Remote>
	suspend fun getCategories(): Result<List<Category>, DataError.Remote>
	fun getWishlists(limit: Int): Flow<PagingData<Wishlist>>
	suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote>
	suspend fun removeWishlist(id: Long): Result<Unit, DataError.Remote>
	fun getProducts(filterParams: ProductFilterParams): ProductsPagingSource
	suspend fun getProductFilter(categoryId: Long, brandId: Long): Result<ProductFilter, DataError.Remote>
	suspend fun getCategory(id: Long): Result<Category, DataError.Remote>
	fun getCategoryProducts(id: Long, filterParams: ProductFilterParams): CategoryProductsPagingSource
}