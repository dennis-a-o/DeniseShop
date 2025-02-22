package com.example.deniseshop.data.repository

import androidx.paging.PagingSource
import com.example.deniseshop.common.state.DownloadState
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCart
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.models.ApiCheckout
import com.example.deniseshop.data.models.ApiContact
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.models.ApiHome
import com.example.deniseshop.data.models.ApiOrderDetail
import com.example.deniseshop.data.models.ApiPage
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.domain.models.UpdateProfileData
import com.example.deniseshop.domain.models.UserSignInData
import com.example.deniseshop.domain.models.UserSignUpData
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCoupon
import com.example.deniseshop.ui.models.UiFaq
import com.example.deniseshop.ui.models.UiOrder
import com.example.deniseshop.ui.models.UiProduct
import com.example.deniseshop.ui.models.UiReview
import com.example.deniseshop.ui.models.UiWishlist
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ApiRepository {
	fun signUp(userSignUpData: UserSignUpData): Flow<NetworkResponseState<String>>
	fun forgotPassword(email: String): Flow<NetworkResponseState<String>>
	fun signIn(email: String,password: String): Flow<NetworkResponseState<String>>
	fun getUser(): Flow<NetworkResponseState<ApiUser>>
	fun updateUser(updateProfileData: UpdateProfileData): Flow<NetworkResponseState<String>>
	fun uploadProfileImage(body: MultipartBody.Part): Flow<NetworkResponseState<String>>
	fun changePassword(currentPassword: String, newPassword: String): Flow<NetworkResponseState<String>>
	fun logout(): Flow<NetworkResponseState<String>>
	fun deleteAccount(): Flow<NetworkResponseState<String>>
	fun getHome(): Flow<NetworkResponseState<ApiHome>>
	fun getCategories(): Flow<NetworkResponseState<List<ApiCategory>>>
	fun getCategoryProduct(category: Long,queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
	fun getCategory(id: Long): Flow<NetworkResponseState<ApiCategory>>
	fun getProductFilter(category: Long, brand: Long): Flow<NetworkResponseState<ApiProductFilter>>
	fun getAllBrand(): PagingSource<Int, UiBrand>
	fun getBrandByCategory(category: Long): Flow<NetworkResponseState<List<ApiBrand>>>
	fun getBrand(id: Long): Flow<NetworkResponseState<ApiBrand>>
	fun getBrandProduct(brand: Long,queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
	fun getProducts(queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
	fun getFlashSale(id: Long): Flow<NetworkResponseState<ApiFlashSale>>
	fun getFlashSaleProduct(flashSale: Long, queryParams: ProductQueryParams): PagingSource<Int, UiProduct>
	fun getRecentViewedProduct(): PagingSource<Int, UiProduct>
	fun clearRecentViewedProduct(): Flow<NetworkResponseState<String>>
	fun getProductDetail(id: Long): Flow<NetworkResponseState<ApiProductDetail>>
	fun setViewed(productId:Long): Flow<NetworkResponseState<String>>
	fun getReviewStat(product: Long): Flow<NetworkResponseState<ApiReviewStat>>
	fun getReviews(product: Long): PagingSource<Int, UiReview>
	fun getWishlist(): PagingSource<Int, UiWishlist>
	fun addWishlist(product: Long): Flow<NetworkResponseState<String>>
	fun removeWishlist(id: Long): Flow<NetworkResponseState<String>>
	fun getWishlistProduct(): Flow<NetworkResponseState<List<Long>>>
	fun getCart(): Flow<NetworkResponseState<ApiCart>>
	fun addCart(product: Long, quantity: Int?,size: String?, color: String?): Flow<NetworkResponseState<String>>
	fun clearCart(): Flow<NetworkResponseState<String>>
	fun removeCart(product: Long): Flow<NetworkResponseState<String>>
	fun increaseCartQuantity(id: Long): Flow<NetworkResponseState<String>>
	fun decreaseCartQuantity(id: Long): Flow<NetworkResponseState<String>>
	fun applyCoupon(coupon:String): Flow<NetworkResponseState<String>>
	fun clearCoupon(): Flow<NetworkResponseState<String>>
	fun getCountries(): Flow<NetworkResponseState<List<String>>>
	fun getAllAddress(): Flow<NetworkResponseState<List<ApiAddress>>>
	fun addAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<String>>
	fun updateAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<String>>
	fun setAddressDefault(id: Long): Flow<NetworkResponseState<String>>
	fun removeAddress(id: Long): Flow<NetworkResponseState<String>>
	fun checkout(): Flow<NetworkResponseState<ApiCheckout>>
	fun placeOrder(): Flow<NetworkResponseState<String>>
	fun createPaypalPayment(): Flow<NetworkResponseState<String>>
	fun paypalPaymentSuccess(token: String, payerId: String):Flow<NetworkResponseState<String>>
	fun paypalPaymentCancel():Flow<NetworkResponseState<String>>
	fun getOrders(): PagingSource<Int, UiOrder>
	fun getOrderDetail(id: Long): Flow<NetworkResponseState<ApiOrderDetail>>
	fun addReview(itemId: Long,rating: Int, review:String):Flow<NetworkResponseState<String>>
	fun downloadOrderItem(itemId: Long): Flow<DownloadState<String>>
	fun downloadOrderInvoice(orderId: Long): Flow<DownloadState<String>>
	fun getFaqs(): PagingSource<Int, UiFaq>
	fun getCoupons(): PagingSource<Int, UiCoupon>
	fun getContact(): Flow<NetworkResponseState<List<ApiContact>>>
	fun getPage(name: String): Flow<NetworkResponseState<ApiPage>>
}