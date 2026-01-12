package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.CategoryDto
import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.data.dto.ProductFilterDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.data.dto.WishlistDto
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
	suspend fun removeWishlist(
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
}