package com.example.deniseshop.core.domain.repository

import androidx.paging.PagingData
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.Wishlist
import kotlinx.coroutines.flow.Flow

interface ShopRepository {
	suspend fun getHome(): Result<Home,  DataError.Remote>
	suspend fun getCategories(): Result<List<Category>, DataError.Remote>
	fun getWishlists(limit: Int): Flow<PagingData<Wishlist>>
	suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote>
	suspend fun removeWishlist(id: Long): Result<Unit, DataError.Remote>
}