package com.example.deniseshop.core.domain.repository

import androidx.paging.PagingData
import com.example.deniseshop.core.data.paging.BrandProductsPagingSource
import com.example.deniseshop.core.data.paging.CategoryProductsPagingSource
import com.example.deniseshop.core.data.paging.FlashSaleProductsPagingSource
import com.example.deniseshop.core.data.paging.ProductsPagingSource
import com.example.deniseshop.core.data.paging.RecentViewedProductsPagingSource
import com.example.deniseshop.core.domain.model.Brand
import com.example.deniseshop.core.domain.model.Cart
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.FlashSale
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.ProductData
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
	suspend fun removeFromWishlist(id: Long): Result<Unit, DataError.Remote>
	fun getProducts(filterParams: ProductFilterParams): ProductsPagingSource
	suspend fun getProductFilter(categoryId: Long, brandId: Long): Result<ProductFilter, DataError.Remote>
	suspend fun getCategory(id: Long): Result<Category, DataError.Remote>
	fun getCategoryProducts(id: Long, filterParams: ProductFilterParams): CategoryProductsPagingSource
	suspend fun getBrand(id: Long): Result<Brand, DataError.Remote>
	suspend fun getCategoryBrands(categoryId: Long): Result<List<Brand>, DataError.Remote>
	fun getBrands(limit: Int): Flow<PagingData<Brand>>
	fun getBrandProducts(brandId: Long, filterParams: ProductFilterParams): BrandProductsPagingSource
	suspend fun getCart(): Result<Cart, DataError.Remote>
	suspend fun addToCart(productData: ProductData): Result<Unit, DataError.Remote>
	suspend fun removeFromCart(productId: Long): Result<Unit, DataError.Remote>
	suspend fun clearCart(): Result<Unit, DataError.Remote>
	suspend fun increaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote>
	suspend fun decreaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote>
	suspend fun applyCoupon(coupon: String): Result<String, DataError>
	suspend fun clearCoupon(): Result<String, DataError.Remote>
	suspend fun getFlashSale(id: Long): Result<FlashSale, DataError.Remote>
	fun getFlashSaleProducts(flashSaleId: Long, filterParams: ProductFilterParams): FlashSaleProductsPagingSource
	fun getRecentViewedProducts(): RecentViewedProductsPagingSource
	suspend fun clearRecentViewedProducts(): Result<Unit, DataError.Remote>
}