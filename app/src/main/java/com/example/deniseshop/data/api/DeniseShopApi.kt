package com.example.deniseshop.data.api

import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCart
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.models.ApiCheckout
import com.example.deniseshop.data.models.ApiContact
import com.example.deniseshop.data.models.ApiCoupon
import com.example.deniseshop.data.models.ApiFaq
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.models.ApiHome
import com.example.deniseshop.data.models.ApiOrder
import com.example.deniseshop.data.models.ApiOrderDetail
import com.example.deniseshop.data.models.ApiPage
import com.example.deniseshop.data.models.ApiPaymentUrl
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.data.models.ApiResponse
import com.example.deniseshop.data.models.ApiReview
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.data.models.ApiSignInResponse
import com.example.deniseshop.data.models.ApiToken
import com.example.deniseshop.data.models.ApiUserUpdate
import com.example.deniseshop.data.models.ApiImageUpdate
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.models.ApiWishlist
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
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
import retrofit2.http.Streaming

interface DeniseShopApi {

	@POST("auth/register")
	@FormUrlEncoded
	 fun signUp(
		@Field("first_name") firstName: String,
		@Field("last_name") lastName: String,
		@Field("phone") phone: String,
		@Field("email") email: String,
		@Field("password") password: String,
		@Field("accepted_terms") acceptTerms: Boolean,
	): Call<ApiResponse>

	@POST("auth/login")
	@FormUrlEncoded
	fun signIn(
		@Field("email") email: String,
		@Field("password") password: String,
	): Call<ApiSignInResponse>

	@POST("auth/refresh")
	fun refreshToken(): Call<ApiToken>

	@POST("auth/forgot-password")
	@FormUrlEncoded
	fun forgotPassword(
		@Field("email") email: String,
	): Call<ApiResponse>

	@POST("auth/logout")
	@FormUrlEncoded
	suspend fun logout(
		@Field("refresh_token") refreshToken: String
	): ApiResponse

	@GET("profile")
	suspend fun getProfile(): ApiUser

	@POST("profile/update")
	@FormUrlEncoded
	fun updateProfile(
		@Field("first_name") firstName: String,
		@Field("last_name") lastName: String,
		@Field("email") email: String,
		@Field("phone") phone: String,
	): Call<ApiUserUpdate>

	@POST("profile/update-image")
	@Multipart
	fun uploadProfileImage(
		@Part body: MultipartBody.Part?
	): Call<ApiImageUpdate>

	@POST("profile/change-password")
	@FormUrlEncoded
	fun changePassword(
		@Field("current_password") currentPassword: String,
		@Field("new_password") newPassword: String
	): Call<ApiResponse>

	@DELETE("profile/delete")
	suspend fun deleteAccount(): ApiResponse

	@GET("home")
	suspend fun getHome(): ApiHome

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
	): List<ApiProduct>

	@GET("categories")
	suspend fun getAllCategory(): List<ApiCategory>

	@GET("category/{category}/products")
	suspend fun getCategoryProduct(
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
	): List<ApiProduct>

	@GET("category/{id}")
	suspend fun getCategory(@Path("id") id: Long): ApiCategory

	@GET("products/filter")
	suspend fun  getProductFilter(
		@Query("category") category: Long,
		@Query("brand") brand: Long
	): ApiProductFilter

	@GET("brand/category/{category}")
	suspend fun getBrandByCategory(
		@Path("category") category: Long
	): List<ApiBrand>

	@GET("brands")
	suspend fun getAllBrand(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<ApiBrand>

	@GET("brand/{id}")
	suspend fun getBrand(
		@Path("id") id: Long
	): ApiBrand

	@GET("brand/{brand}/products")
	suspend fun  getBrandProduct(
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
	): List<ApiProduct>

	@GET("flashsale/{id}")
	suspend fun getFlashSale(
		@Path("id") id: Long,
	): ApiFlashSale

	@GET("flashsale/{flashSale}/products")
	suspend fun getFlashSaleProduct(
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
	): List<ApiProduct>

	@GET("recent-viewed")
	suspend fun getRecentViewedProduct(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int,
	): List<ApiProduct>

	@DELETE("recent-viewed/clear")
	suspend fun deleteRecentViewedProduct(): ApiResponse

	@GET("product/{id}")
	suspend fun getProductDeatil(
		@Path("id") id: Long
	): ApiProductDetail

	@POST("product/{id}")
	suspend fun setViewed(
		@Path("id") id: Long
	): ApiResponse

	@GET("review-stat/{product}")
	suspend fun getReviewStat(
		@Path("product") product: Long
	): ApiReviewStat

	@GET("reviews/{product}")
	suspend fun getReviews(
		@Path("product") product: Long,
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<ApiReview>

	@GET("wishlists")
	suspend fun getWishlist(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<ApiWishlist>

	@GET("wishlist/products")
	suspend fun getWishlistProduct(): List<Long>

	@POST("wishlist/add")
	@FormUrlEncoded
	suspend fun addWishlist(
		@Field("product_id") product: Long,
	): ApiResponse

	@DELETE("wishlist/{id}/delete")
	suspend fun removeWishlist(
		@Path("id") id: Long,
	): ApiResponse

	@GET("cart")
	suspend fun getCart(): ApiCart

	@POST("cart/add")
	@FormUrlEncoded
	suspend fun addCart(
		@Field("product_id") product: Long,
		@Field("quantity") quantity: Int?,
		@Field("color") color: String?,
		@Field("size") size: String?,
	): ApiResponse

	@DELETE("cart/clear")
	suspend fun clearCart(): ApiResponse

	@DELETE("cart/{id}/delete")
	suspend fun removeCart(
		@Path("id") id: Long,
	): ApiResponse

	@POST("cart/{id}/increase")
	suspend fun increaseCartQuantity(
		@Path("id") id: Long,
	): ApiResponse

	@POST("cart/{id}/decrease")
	suspend fun decreaseCartQuantity(
		@Path("id") id: Long,
	): ApiResponse

	@POST("coupon/apply")
	@FormUrlEncoded
	suspend fun applyCoupon(
		@Field("coupon") coupon: String
	): ApiResponse

	@DELETE("coupon/clear")
	suspend fun clearCoupon(): ApiResponse

	@GET("address")
	suspend fun getAllAddress(): List<ApiAddress>

	@GET("address/countries")
	suspend fun getCountries(): List<String>

	@POST("address/add")
	@FormUrlEncoded
	fun addAddress(
		@Field("name") name: String,
		@Field("email") email: String,
		@Field("phone") phone: String,
		@Field("country") country: String,
		@Field("state") state: String,
		@Field("city") city: String,
		@Field("address") address: String,
		@Field("zip_code") zipCode: String,
		@Field("type") type: String,
		@Field("default") default: Int
	): Call<ApiResponse>

	@FormUrlEncoded
	@POST("address/{id}/update")
	fun updateAddress(
		@Path("id") id: Long,
		@Field("name") name: String,
		@Field("email") email: String,
		@Field("phone") phone: String,
		@Field("country") country: String,
		@Field("state") state: String,
		@Field("city") city: String,
		@Field("address") address: String,
		@Field("zip_code") zipCode: String,
		@Field("type") type: String,
		@Field("default") default: Int
	): Call<ApiResponse>

	@PUT("address/{id}/default")
	suspend fun setAddressDefault(@Path("id") id: Long): ApiResponse

	@DELETE("address/{id}/delete")
	suspend fun removeAddress(@Path("id") id: Long): ApiResponse

	@GET("checkout")
	suspend fun checkout(): ApiCheckout

	@POST("checkout/place-order")
	suspend fun placeOrder(): ApiResponse

	@POST("payment/create-paypal-payment")
	suspend fun createPaypalPayment(): ApiPaymentUrl

	@POST("payment/create-card-payment")
	@FormUrlEncoded
	suspend fun createCardPayment(
		@Field("type") type: String,
		@Field("first_name") firstName: String,
		@Field("last_name") lastName: String,
		@Field("number") number: Long,
		@Field("expiry_month") expiryMonth: Int,
		@Field("expiry_year") expiryYear: Int,
		@Field("cvv") cvv: Int,
	): ApiResponse

	@GET("payment/paypal-success")
	suspend fun paypalPaymentSuccess(
		@Query("token") token: String,
		@Query("payer_id") payerId: String
	): ApiResponse

	@GET("payment/paypal-cancel")
	suspend fun paypalPaymentCancel(): ApiResponse

	@GET("orders")
	suspend fun getOrders(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<ApiOrder>

	@GET("order/{id}")
	suspend fun getOrderDetail(
		@Path("id") id: Long
	): ApiOrderDetail

	@POST("order/add-review/{item_id}")
	@FormUrlEncoded
	fun addReview(
		@Path("item_id") itemId: Long,
		@Field("rating") rating: Int,
		@Field("review") review: String
	): Call<ApiResponse>

	@GET("order/item/{item_id}/download")
	@Streaming
	suspend fun downloadOrderItem(
		@Path("item_id") itemId: Long
	): Response<ResponseBody>

	@GET("order/{order_id}/invoice")
	@Streaming
	suspend fun downloadOrderInvoice(
		@Path("order_id") orderId: Long
	): Response<ResponseBody>

	@GET("faqs")
	suspend fun getFaqs(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<ApiFaq>

	@GET("coupons")
	suspend fun getCoupons(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<ApiCoupon>

	@GET("contact")
	suspend fun getContact(): List<ApiContact>

	@GET("page")
	suspend fun getPage(
		@Query("name") name:String
	): ApiPage

}