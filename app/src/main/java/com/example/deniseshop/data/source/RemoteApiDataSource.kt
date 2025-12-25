package com.example.deniseshop.data.source

import com.example.deniseshop.common.state.NetworkResponseState
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
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.data.models.ApiResponse
import com.example.deniseshop.data.models.ApiReview
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.data.models.ApiSignInResponse
import com.example.deniseshop.data.models.ApiUserUpdate
import com.example.deniseshop.data.models.ApiImageUpdate
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.models.ApiWishlist
import com.example.deniseshop.domain.models.UpdateProfileData
import com.example.deniseshop.domain.models.UserSignUpData
import com.example.deniseshop.ui.models.ProductQueryParams
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface RemoteApiDataSource {
	fun signUp(userSignUpData: UserSignUpData): Flow<NetworkResponseState<ApiResponse>>
	fun signIn(email: String,password: String): Flow<NetworkResponseState<ApiSignInResponse>>
	fun forgotPassword(email: String): Flow<NetworkResponseState<ApiResponse>>
	fun getUser(): Flow<NetworkResponseState<ApiUser>>
	fun updateUser(updateProfileData: UpdateProfileData): Flow<NetworkResponseState<ApiUserUpdate>>
	fun uploadProfileImage(body: MultipartBody.Part): Flow<NetworkResponseState<ApiImageUpdate>>
	fun changePassword(currentPassword: String, newPassword: String): Flow<NetworkResponseState<ApiResponse>>
	suspend fun logout(refreshToken:String): ApiResponse
	suspend fun deleteAccount(): ApiResponse
	fun getHome(): Flow<NetworkResponseState<ApiHome>>
	fun getCategories(): Flow<NetworkResponseState<List<ApiCategory>>>
	suspend fun getCategoryProduct(category: Long,queryParams: ProductQueryParams): List<ApiProduct>
	fun getCategory(id: Long): Flow<NetworkResponseState<ApiCategory>>
	fun getProductFilter(category: Long, brand: Long): Flow<NetworkResponseState<ApiProductFilter>>
	suspend fun getAllBrand(page: Int, pageSize: Int): List<ApiBrand>
	fun getBrandByCategory(category: Long): Flow<NetworkResponseState<List<ApiBrand>>>
	fun getBrand(id: Long): Flow<NetworkResponseState<ApiBrand>>
	suspend fun getBrandProduct(brand: Long,queryParams: ProductQueryParams): List<ApiProduct>
	suspend fun getProducts(queryParams: ProductQueryParams): List<ApiProduct>
	fun getFlashSale(id: Long): Flow<NetworkResponseState<ApiFlashSale>>
	suspend fun getFlashSaleProduct(flashSale: Long, queryParams: ProductQueryParams): List<ApiProduct>
	suspend fun getRecentViewedProduct(page: Int, pageSize: Int): List<ApiProduct>
	fun clearRecentViewed(): Flow<NetworkResponseState<ApiResponse>>
	fun getProductDetail(id: Long): Flow<NetworkResponseState<ApiProductDetail>>
	fun setViewed(productId:Long): Flow<NetworkResponseState<String>>
	fun getReviewStat(product: Long): Flow<NetworkResponseState<ApiReviewStat>>
	suspend fun getReviews(product: Long, page: Int, pageSize: Int): List<ApiReview>
	suspend fun getWishlist(page: Int, pageSize: Int): List<ApiWishlist>
	fun addWishlist(product: Long): Flow<NetworkResponseState<ApiResponse>>
	fun removeWishlist(id: Long): Flow<NetworkResponseState<ApiResponse>>
	fun getWishlistProduct(): Flow<NetworkResponseState<List<Long>>>
	fun getCart(): Flow<NetworkResponseState<ApiCart>>
	fun addCart(product: Long, quantity: Int?,size: String?, color: String?): Flow<NetworkResponseState<ApiResponse>>
	fun clearCart(): Flow<NetworkResponseState<ApiResponse>>
	fun removeCart(product: Long): Flow<NetworkResponseState<ApiResponse>>
	fun increaseCartQuantity(id: Long): Flow<NetworkResponseState<ApiResponse>>
	fun decreaseCartQuantity(id: Long): Flow<NetworkResponseState<ApiResponse>>
	fun applyCoupon(coupon:String): Flow<NetworkResponseState<ApiResponse>>
	fun clearCoupon(): Flow<NetworkResponseState<ApiResponse>>
	fun getCountries(): Flow<NetworkResponseState<List<String>>>
	fun getAllAddress(): Flow<NetworkResponseState<List<ApiAddress>>>
	fun addAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<ApiResponse>>
	fun updateAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<ApiResponse>>
	fun setAddressDefault(id: Long): Flow<NetworkResponseState<ApiResponse>>
	fun removeAddress(id: Long): Flow<NetworkResponseState<ApiResponse>>
	fun checkout():Flow<NetworkResponseState<ApiCheckout>>
	fun placeOrder(): Flow<NetworkResponseState<ApiResponse>>
	fun createPaypalPayment(): Flow<NetworkResponseState<String>>
	fun paypalPaymentSuccess(token:String, payerId:String):Flow<NetworkResponseState<ApiResponse>>
	fun paypalPaymentCancel():Flow<NetworkResponseState<ApiResponse>>
	suspend fun getOrders(page:Int, pageSize: Int): List<ApiOrder>
	fun getOrderDetail(id: Long): Flow<NetworkResponseState<ApiOrderDetail>>
	fun addReview(itemId: Long,rating: Int, review:String):Flow<NetworkResponseState<ApiResponse>>
	suspend fun downloadOrderItem(itemId: Long): Response<ResponseBody>
	suspend fun downloadOrderInvoice(orderId: Long): Response<ResponseBody>
	suspend fun getFaqs(page: Int, pageSize: Int): List<ApiFaq>
	suspend fun getCoupons(page: Int, pageSize: Int): List<ApiCoupon>
	fun getContact(): Flow<NetworkResponseState<List<ApiContact>>>
	fun getPage(name: String): Flow<NetworkResponseState<ApiPage>>
}