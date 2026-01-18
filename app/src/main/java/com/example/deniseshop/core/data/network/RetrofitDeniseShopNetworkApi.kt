package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.BrandDto
import com.example.deniseshop.core.data.dto.CartDto
import com.example.deniseshop.core.data.dto.CategoryDto
import com.example.deniseshop.core.data.dto.FlashSaleDto
import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.MessageDto
import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.data.dto.ProductFilterDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.data.dto.WishlistDto
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.data.models.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitDeniseShopNetworkApi {
	@POST("auth/register")
	@FormUrlEncoded
	 fun signUp(
		@Field("first_name") firstName: String,
		@Field("last_name") lastName: String,
		@Field("phone") phone: String,
		@Field("email") email: String,
		@Field("password") password: String,
		@Field("accepted_terms") acceptTerms: Boolean,
	): Call<Unit>

	@POST("auth/login")
	@FormUrlEncoded
	fun signIn(
		@Field("email") email: String,
		@Field("password") password: String,
	): Call<UserCredentialDto>

	@POST("auth/forgot-password")
	@FormUrlEncoded
	suspend fun forgotPassword(
		@Field("email") email: String,
	)

	@POST("auth/logout")
	suspend fun logout()

	@GET("user")
	suspend fun getUser(): UserDto

	@PUT("user")
	@FormUrlEncoded
	fun updateUser(
		@Field("first_name") firstName: String,
		@Field("last_name") lastName: String,
		@Field("email") email: String,
		@Field("phone") phone: String,
	): Call<UserDto>

	@POST("user/image")
	@Multipart
	suspend fun uploadUserImage(
		@Part body: MultipartBody.Part?
	): ImageDto

	@PUT("user/password")
	@FormUrlEncoded
	fun changePassword(
		@Field("current_password") currentPassword: String,
		@Field("new_password") newPassword: String
	): Call<Unit>

	@DELETE("user")
	suspend fun deleteAccount()

	@GET("home")
	suspend fun getHome(): HomeDto

	@GET("categories")
	suspend fun getCategories(): List<CategoryDto>

	@GET("wishlists")
	suspend fun getWishlists(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<WishlistDto>

	@POST("wishlist")
	@FormUrlEncoded
	suspend fun addToWishlist(
		@Field("product_id") product: Long,
	)

	@DELETE("wishlist/{id}")
	suspend fun removeFromWishlist(
		@Path("id") id: Long,
	)

	@GET("products")
	suspend fun getProducts(
		@Query("q") query: String,
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int,
		@Query("sort_by") sortBy: String,
		@Query("min_price") minPrice: Int,
		@Query("max_price") maxPrice: Int,
		@Query("categories[]") categories: List<String>,
		@Query("brands[]") brands: List<String>,
		@Query("colors[]") colors: List<String>,
		@Query("sizes[]") sizes: List<String>,
		@Query("rating") rating: Int
	): List<ProductDto>

	@GET("products/filter")
	suspend fun  getProductFilter(
		@Query("category") category: Long,
		@Query("brand") brand: Long
	): ProductFilterDto

	@GET("category/{category}/products")
	suspend fun getCategoryProducts(
		@Path("category") category: Long,
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int,
		@Query("sort_by") sortBy: String,
		@Query("min_price") minPrice: Int,
		@Query("max_price") maxPrice: Int,
		@Query("categories[]") categories: List<String>,
		@Query("brands[]") brands: List<String>,
		@Query("colors[]") colors: List<String>,
		@Query("sizes[]") sizes: List<String>,
		@Query("rating") rating: Int
	): List<ProductDto>

	@GET("category/{id}")
	suspend fun getCategory(@Path("id") id: Long): CategoryDto

	@GET("brands")
	suspend fun getBrands(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<BrandDto>

	@GET("brand/{id}")
	suspend fun getBrand(
		@Path("id") id: Long
	): BrandDto

	@GET("brand/category/{category}")
	suspend fun getCategoryBrands(
		@Path("category") category: Long
	): List<BrandDto>

	@GET("brand/{brand}/products")
	suspend fun  getBrandProducts(
		@Path("brand") brand: Long,
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int,
		@Query("sort_by") sortBy: String,
		@Query("min_price") minPrice: Int,
		@Query("max_price") maxPrice: Int,
		@Query("categories[]") categories: List<String>,
		@Query("brands[]") brands: List<String>,
		@Query("colors[]") colors: List<String>,
		@Query("sizes[]") sizes: List<String>,
		@Query("rating") rating: Int
	): List<ProductDto>

	@GET("cart")
	suspend fun getCart(): CartDto

	@POST("cart/add")
	@FormUrlEncoded
	suspend fun addToCart(
		@Field("product_id") product: Long,
		@Field("quantity") quantity: Int?,
		@Field("color") color: String?,
		@Field("size") size: String?,
	)

	@DELETE("cart/{id}/delete")
	suspend fun removeFromCart(
		@Path("id") id: Long,
	)

	@DELETE("cart/clear")
	suspend fun clearCart()

	@POST("cart/{id}/increase")
	suspend fun increaseCartItemQuantity(
		@Path("id") id: Long,
	)

	@POST("cart/{id}/decrease")
	suspend fun decreaseCartItemQuantity(
		@Path("id") id: Long,
	)

	@POST("coupon/apply")
	@FormUrlEncoded
	fun applyCoupon(
		@Field("coupon") coupon: String
	): Call<MessageDto>

	@DELETE("coupon/clear")
	suspend fun clearCoupon(): MessageDto

	@GET("flashsale/{id}")
	suspend fun getFlashSale(
		@Path("id") id: Long,
	): FlashSaleDto

	@GET("flashsale/{flashSale}/products")
	suspend fun getFlashSaleProducts(
		@Path("flashSale") flashSale: Long,
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int,
		@Query("sort_by") sortBy: String,
		@Query("min_price") minPrice: Int,
		@Query("max_price") maxPrice: Int,
		@Query("categories[]") categories: List<String>,
		@Query("brands[]") brands: List<String>,
		@Query("colors[]") colors: List<String>,
		@Query("sizes[]") sizes: List<String>,
		@Query("rating") rating: Int
	): List<ProductDto>

	@GET("recent-viewed")
	suspend fun getRecentViewedProducts(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int,
	): List<ProductDto>

	@DELETE("recent-viewed/clear")
	suspend fun clearRecentViewedProducts()
}