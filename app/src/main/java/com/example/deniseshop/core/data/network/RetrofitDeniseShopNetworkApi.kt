package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.AddressDto
import com.example.deniseshop.core.data.dto.BrandDto
import com.example.deniseshop.core.data.dto.CartDto
import com.example.deniseshop.core.data.dto.CategoryDto
import com.example.deniseshop.core.data.dto.CheckoutDto
import com.example.deniseshop.core.data.dto.ContactDto
import com.example.deniseshop.core.data.dto.CouponDto
import com.example.deniseshop.core.data.dto.FaqDto
import com.example.deniseshop.core.data.dto.FlashSaleDto
import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.MessageDto
import com.example.deniseshop.core.data.dto.OrderDetailDto
import com.example.deniseshop.core.data.dto.OrderDto
import com.example.deniseshop.core.data.dto.PageDto
import com.example.deniseshop.core.data.dto.PaymentUrlDto
import com.example.deniseshop.core.data.dto.ProductDetailDto
import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.data.dto.ProductFilterDto
import com.example.deniseshop.core.data.dto.ReviewDto
import com.example.deniseshop.core.data.dto.ReviewStatDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.data.dto.WishlistDto
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

	@GET("product/{id}")
	suspend fun getProductDetail(
		@Path("id") id: Long
	): ProductDetailDto

	@POST("product/{id}")
	suspend fun setProductViewed(
		@Path("id") id: Long
	)

	@GET("review-stat/{product}")
	suspend fun getReviewStat(
		@Path("product") product: Long
	): ReviewStatDto

	@GET("reviews/{product}")
	suspend fun getReviews(
		@Path("product") product: Long,
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<ReviewDto>

	@GET("checkout")
	suspend fun getCheckout(): CheckoutDto

	@POST("checkout/place-order")
	suspend fun placeOrder(): MessageDto

	@POST("payment/create-paypal-payment")
	suspend fun createPaypalPayment(): PaymentUrlDto

	@GET("payment/paypal-success")
	fun paypalPaymentSuccess(
		@Query("token") token: String,
		@Query("payer_id") payerId: String
	): Call<MessageDto>

	@GET("payment/paypal-cancel")
	suspend fun paypalPaymentCancel(): MessageDto

	@GET("address")
	suspend fun getAddresses(): List<AddressDto>

	@GET("address/{id}")
	suspend fun getAddress(@Path("id") id: Long ): AddressDto?

	@GET("address/countries/list")
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
	): Call<MessageDto>

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
	): Call<MessageDto>

	@PUT("address/{id}/default")
	suspend fun setDefaultAddress(@Path("id") id: Long): MessageDto

	@DELETE("address/{id}/delete")
	suspend fun deleteAddress(@Path("id") id: Long): MessageDto

	@GET("orders")
	suspend fun getOrders(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<OrderDto>

	@GET("order/{id}")
	suspend fun getOrderDetail(
		@Path("id") id: Long
	): OrderDetailDto?

	@POST("order/add-review/{item_id}")
	@FormUrlEncoded
	fun addReview(
		@Path("item_id") itemId: Long,
		@Field("rating") rating: Int,
		@Field("review") review: String
	): Call<MessageDto>

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
	): List<FaqDto>

	@GET("coupons")
	suspend fun getCoupons(
		@Query("page") page: Int,
		@Query("page_size") pageSize: Int
	): List<CouponDto>

	@GET("contact")
	suspend fun getContact(): List<ContactDto>

	@GET("page")
	suspend fun getPage(
		@Query("name") name:String
	): PageDto
}