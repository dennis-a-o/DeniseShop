package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.BrandDto
import com.example.deniseshop.core.data.dto.CartDto
import com.example.deniseshop.core.data.dto.CategoryDto
import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.MessageDto
import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.data.dto.ProductFilterDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.data.dto.WishlistDto
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import okhttp3.MultipartBody

interface RemoteDeniseShopDataSource {
	suspend fun signUp(user: UserSignUp): Result<Unit, DataError>
	suspend fun signIn(email: String, password: String): Result<UserCredentialDto, DataError>
	suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote>
	suspend fun getUser(): Result<UserDto, DataError.Remote>
	suspend fun updateUser(user: User): Result<UserDto, DataError>
	suspend fun uploadUserImage(body: MultipartBody.Part): Result<ImageDto, DataError.Remote>
	suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, DataError>
	suspend fun logout(): Result<Unit, DataError.Remote>
	suspend fun deleteUser(): Result<Unit, DataError.Remote>
	suspend fun getHome(): Result<HomeDto, DataError.Remote>
	suspend fun getCategories(): Result<List<CategoryDto>, DataError.Remote>
	suspend fun getWishlists(page: Int, pageSize: Int): Result<List<WishlistDto>, DataError.Remote>
	suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote>
	suspend fun removeFromWishlist(id: Long): Result<Unit, DataError.Remote>
	suspend fun getProducts(filterParams: ProductFilterParams): Result<List<ProductDto>, DataError.Remote>
	suspend fun getProductFilter(categoryId: Long, brandId: Long): Result<ProductFilterDto, DataError.Remote>
	suspend fun getCategory(id: Long): Result<CategoryDto, DataError.Remote>
	suspend fun getBrand(id: Long): Result<BrandDto, DataError.Remote>
	suspend fun getCategoryBrands(categoryId: Long): Result<List<BrandDto>, DataError.Remote>
	suspend fun getCart(): Result<CartDto, DataError.Remote>
	suspend fun addToCart(productData: ProductData): Result<Unit, DataError.Remote>
	suspend fun removeFromCart(productId: Long): Result<Unit, DataError.Remote>
	suspend fun clearCart(): Result<Unit, DataError.Remote>
	suspend fun increaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote>
	suspend fun decreaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote>
	suspend fun applyCoupon(coupon: String): Result<MessageDto, DataError>
	suspend fun clearCoupon(): Result<MessageDto, DataError.Remote>
}