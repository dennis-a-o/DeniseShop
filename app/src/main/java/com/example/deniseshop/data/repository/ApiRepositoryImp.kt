package com.example.deniseshop.data.repository

import android.content.Context
import android.os.Environment
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deniseshop.common.state.DownloadState
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCart
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.models.ApiCheckout
import com.example.deniseshop.data.models.ApiContact
import com.example.deniseshop.data.models.ApiCoupon
import com.example.deniseshop.data.models.ApiError
import com.example.deniseshop.data.models.ApiFaq
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.models.ApiHome
import com.example.deniseshop.data.models.ApiOrder
import com.example.deniseshop.data.models.ApiOrderDetail
import com.example.deniseshop.data.models.ApiPage
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.data.models.ApiReview
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.models.ApiWishlist
import com.example.deniseshop.data.source.PreferencesDataSource
import com.example.deniseshop.data.source.RemoteApiDataSource
import com.example.deniseshop.domain.models.PrefUser
import com.example.deniseshop.domain.models.UpdateProfileData
import com.example.deniseshop.domain.models.UserSignUpData
import com.example.deniseshop.ui.mapper.BaseListMapper
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCoupon
import com.example.deniseshop.ui.models.UiFaq
import com.example.deniseshop.ui.models.UiOrder
import com.example.deniseshop.ui.models.UiProduct
import com.example.deniseshop.ui.models.UiReview
import com.example.deniseshop.ui.models.UiWishlist
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ApiRepositoryImp @Inject constructor(
	private val apiDataSource: RemoteApiDataSource,
	private val preferencesDataSource: PreferencesDataSource,
	private val productListApiToUiMapper: BaseListMapper<ApiProduct, UiProduct>,
	private val brandListApiToUiMapper: BaseListMapper<ApiBrand, UiBrand>,
	private val reviewListApiToUiMapper: BaseListMapper<ApiReview, UiReview>,
	private val wishlistListApiToUiMapper: BaseListMapper<ApiWishlist, UiWishlist>,
	private val orderListApiToUiMapper: BaseListMapper<ApiOrder, UiOrder>,
	private val faqListApiToUiMapper: BaseListMapper<ApiFaq,UiFaq>,
	private val couponListApiToUiMapper: BaseListMapper<ApiCoupon, UiCoupon>,
	@ApplicationContext val context: Context
) : ApiRepository {

	override fun signUp(userSignUpData: UserSignUpData): Flow<NetworkResponseState<String>> {
		return apiDataSource.signUp(userSignUpData).map {
			when(it){
				NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}
	}

	override fun signIn(email: String,  password: String): Flow<NetworkResponseState<String>>{
		return apiDataSource.signIn(email, password).map{
			when(it){
				NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					preferencesDataSource.setApiToken(it.result.apiToken)
					preferencesDataSource.setApiUser(it.result.user)
					preferencesDataSource.setIsLoggedIn(true)

					NetworkResponseState.Success(it.result.message)
				}
			}
		}
	}

	override fun getUser(): Flow<NetworkResponseState<ApiUser>> {
		return apiDataSource.getUser().map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					preferencesDataSource.setApiUser(it.result)
					NetworkResponseState.Success(it.result)
				}
			}
		}
	}

	override fun updateUser(updateProfileData: UpdateProfileData): Flow<NetworkResponseState<String>> {
		return apiDataSource.updateUser(updateProfileData).map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					preferencesDataSource.setApiUser(it.result.user)
					NetworkResponseState.Success(it.result.message)
				}
			}
		}
	}

	override fun uploadProfileImage(body: MultipartBody.Part): Flow<NetworkResponseState<String>> {
		return apiDataSource.uploadProfileImage(body).map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					preferencesDataSource.setProfileImage(it.result.image)
					NetworkResponseState.Success(it.result.message)
				}
			}
		}
	}

	override fun changePassword(
		currentPassword: String,
		newPassword: String
	): Flow<NetworkResponseState<String>> {
		return apiDataSource.changePassword(currentPassword, newPassword).map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					NetworkResponseState.Success(it.result.message)
				}
			}
		}
	}

	override fun logout(): Flow<NetworkResponseState<String>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val refreshToken = preferencesDataSource.getRefreshToken().first()
				val response = apiDataSource.logout(refreshToken)

				preferencesDataSource.clearApiUser()

				emit(NetworkResponseState.Success(response.message))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun deleteAccount(): Flow<NetworkResponseState<String>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = apiDataSource.deleteAccount()

				preferencesDataSource.clearApiUser()

				emit(NetworkResponseState.Success(response.message))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getHome(): Flow<NetworkResponseState<ApiHome>> {
		return apiDataSource.getHome().map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getCategories(): Flow<NetworkResponseState<List<ApiCategory>>> {
		return apiDataSource.getCategories().map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getCategoryProduct(category: Long, queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return object: PagingSource<Int, UiProduct>(){
			override fun getRefreshKey(state: PagingState<Int, UiProduct>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiProduct> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getCategoryProduct(category,queryParams.copy(page = page, pageSize = pageSize))

					LoadResult.Page(
						data = productListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getCategory(id: Long): Flow<NetworkResponseState<ApiCategory>> {
		return apiDataSource.getCategory(id).map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getProductFilter(category: Long, brand: Long): Flow<NetworkResponseState<ApiProductFilter>> {
		return apiDataSource.getProductFilter(category, brand).map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> NetworkResponseState.Error(it.exception)
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getAllBrand(): PagingSource<Int, UiBrand> {
		return object: PagingSource<Int, UiBrand>(){
			override fun getRefreshKey(state: PagingState<Int, UiBrand>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiBrand> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getAllBrand(page = page, pageSize = pageSize)

					LoadResult.Page(
						data = brandListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getBrandByCategory(category: Long): Flow<NetworkResponseState<List<ApiBrand>>> {
		return apiDataSource.getBrandByCategory(category)
	}

	override fun getBrand(id: Long): Flow<NetworkResponseState<ApiBrand>> {
		return apiDataSource.getBrand(id)
	}

	override fun getBrandProduct(brand: Long, queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return object: PagingSource<Int, UiProduct>(){
			override fun getRefreshKey(state: PagingState<Int, UiProduct>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiProduct> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getBrandProduct(brand,queryParams.copy(page = page, pageSize = pageSize))

					LoadResult.Page(
						data = productListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getProducts(queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return object: PagingSource<Int, UiProduct>(){
			override fun getRefreshKey(state: PagingState<Int, UiProduct>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiProduct> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getProducts(queryParams.copy(page = page, pageSize = pageSize))

					if (queryParams.query.isNotEmpty() && data.isNotEmpty()){
						preferencesDataSource.saveSearchQuery(queryParams.query)
					}

					LoadResult.Page(
						data = productListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getFlashSale(id: Long): Flow<NetworkResponseState<ApiFlashSale>> {
		return apiDataSource.getFlashSale(id)
	}

	override fun getFlashSaleProduct(flashSale: Long, queryParams: ProductQueryParams): PagingSource<Int, UiProduct> {
		return object: PagingSource<Int, UiProduct>(){
			override fun getRefreshKey(state: PagingState<Int, UiProduct>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiProduct> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getFlashSaleProduct(flashSale = flashSale, queryParams.copy(page = page, pageSize = pageSize))

					LoadResult.Page(
						data = productListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getRecentViewedProduct(): PagingSource<Int, UiProduct> {
		return object: PagingSource<Int, UiProduct>(){
			override fun getRefreshKey(state: PagingState<Int, UiProduct>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiProduct> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getRecentViewedProduct(page, pageSize)

					LoadResult.Page(
						data = productListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun clearRecentViewedProduct(): Flow<NetworkResponseState<String>> {
		return apiDataSource.clearRecentViewed().map {
			when(it){
				is NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}
	}

	override fun getProductDetail(id: Long): Flow<NetworkResponseState<ApiProductDetail>> {
		return apiDataSource.getProductDetail(id)
	}

	override fun setViewed(productId: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.setViewed(productId)
	}

	override fun getReviewStat(product: Long): Flow<NetworkResponseState<ApiReviewStat>> {
		return apiDataSource.getReviewStat(product)
	}

	override fun getReviews(product: Long): PagingSource<Int, UiReview> {
		return object: PagingSource<Int, UiReview>(){
			override fun getRefreshKey(state: PagingState<Int, UiReview>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiReview> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getReviews(product, page, pageSize)

					LoadResult.Page(
						data = reviewListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getWishlist(): PagingSource<Int, UiWishlist> {
		return object: PagingSource<Int, UiWishlist>(){
			override fun getRefreshKey(state: PagingState<Int, UiWishlist>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiWishlist> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getWishlist(page, pageSize)

					LoadResult.Page(
						data = wishlistListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun addWishlist(product: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.addWishlist(product).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					NetworkResponseState.Success(it.result.message)
				}
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun removeWishlist(id: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.removeWishlist(id).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					NetworkResponseState.Success(it.result.message)
				}
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getWishlistProduct(): Flow<NetworkResponseState<List<Long>>> {
		return apiDataSource.getWishlistProduct()
	}

	override fun getCart(): Flow<NetworkResponseState<ApiCart>> {
		return apiDataSource.getCart()
	}

	override fun addCart(
		product: Long,
		quantity: Int?,
		size: String?,
		color: String?
	): Flow<NetworkResponseState<String>> {
		return apiDataSource.addCart(product, quantity,size, color).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun clearCart(): Flow<NetworkResponseState<String>> {
		return apiDataSource.clearCart().map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun removeCart(product: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.removeCart(product).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun increaseCartQuantity(id: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.increaseCartQuantity(id).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun decreaseCartQuantity(id: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.decreaseCartQuantity(id).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun applyCoupon(coupon: String): Flow<NetworkResponseState<String>> {
		return apiDataSource.applyCoupon(coupon).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun clearCoupon(): Flow<NetworkResponseState<String>> {
		return apiDataSource.clearCoupon().map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getCountries(): Flow<NetworkResponseState<List<String>>> {
		return apiDataSource.getCountries()
	}

	override fun getAllAddress(): Flow<NetworkResponseState<List<ApiAddress>>> {
		return apiDataSource.getAllAddress()
	}

	override fun addAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<String>> {
		return apiDataSource.addAddress(apiAddress).map {
			when(it){
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun updateAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<String>> {
		return apiDataSource.updateAddress(apiAddress).map {
			when(it){
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun setAddressDefault(id: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.setAddressDefault(id).map {
			when(it){
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun removeAddress(id: Long): Flow<NetworkResponseState<String>> {
		return apiDataSource.removeAddress(id).map {
			when(it){
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun checkout(): Flow<NetworkResponseState<ApiCheckout>> {
		return apiDataSource.checkout()
	}

	override fun placeOrder(): Flow<NetworkResponseState<String>> {
		return apiDataSource.placeOrder().map {
			when(it){
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun createPaypalPayment(): Flow<NetworkResponseState<String>> {
		return apiDataSource.createPaypalPayment().map {
			when (it) {
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result)
			}
		}
	}

	override fun paypalPaymentSuccess(token:String, payerId: String): Flow<NetworkResponseState<String>> {
		return apiDataSource.paypalPaymentSuccess(token, payerId).map {
			when(it){
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun paypalPaymentCancel(): Flow<NetworkResponseState<String>> {
		return apiDataSource.paypalPaymentCancel().map {
			when(it){
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getOrders(): PagingSource<Int, UiOrder> {
		return object: PagingSource<Int, UiOrder>(){
			override fun getRefreshKey(state: PagingState<Int, UiOrder>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiOrder> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getOrders(page, pageSize)

					LoadResult.Page(
						data = orderListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getOrderDetail(id: Long): Flow<NetworkResponseState<ApiOrderDetail>> {
		return apiDataSource.getOrderDetail(id)
	}

	override fun addReview(
		itemId: Long,
		rating: Int,
		review: String
	): Flow<NetworkResponseState<String>> {
		return apiDataSource.addReview(itemId, rating, review).map {
			when(it){
				is NetworkResponseState.Loading -> it
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> {
					NetworkResponseState.Success(it.result.message)
				}
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun downloadOrderItem(itemId: Long): Flow<DownloadState<String>> {
		return flow {
			emit(DownloadState.Loading(itemId))
			try {
				val response = apiDataSource.downloadOrderItem(itemId)
				if (response.isSuccessful) {
					saveFile(response, "OrderItem.zip").collect {
						emit(DownloadState.Downloading(it))
					}
					emit(DownloadState.Success("Download completed successfully"))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(DownloadState.Error(e.message.toString()))
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun downloadOrderInvoice(orderId: Long): Flow<DownloadState<String>> {
		return flow {
			emit(DownloadState.Loading())
			try {
				val response = apiDataSource.downloadOrderInvoice(orderId)
				if (response.isSuccessful) {
					saveFile(response, "invoice.pdf").collect {
						emit(DownloadState.Downloading(it))
					}
					emit(DownloadState.Success("Download completed successfully"))
				}else{
					throw Exception(response.message())
				}

			}catch (e: Exception){
				emit(DownloadState.Error(e.message.toString()))
			}
		}.flowOn(Dispatchers.IO)
	}

	override fun getFaqs(): PagingSource<Int, UiFaq> {
		return object: PagingSource<Int, UiFaq>(){
			override fun getRefreshKey(state: PagingState<Int, UiFaq>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiFaq> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getFaqs(page, pageSize)

					LoadResult.Page(
						data = faqListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getCoupons(): PagingSource<Int, UiCoupon> {
		return object: PagingSource<Int, UiCoupon>(){
			override fun getRefreshKey(state: PagingState<Int, UiCoupon>): Int? {
				return state.anchorPosition?.let { anchorPosition ->
					state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
						?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
				}
			}

			override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UiCoupon> {
				val page = params.key ?: 1
				val pageSize = params.loadSize

				return try {
					val data = apiDataSource.getCoupons(page, pageSize)

					LoadResult.Page(
						data = couponListApiToUiMapper.map(data),
						prevKey = if(page == 1) null else page -1,
						nextKey = if (data.isEmpty()) null else page + 1
					)
				}catch (e: Exception){
					LoadResult.Error(e)
				}
			}
		}
	}

	override fun getContact(): Flow<NetworkResponseState<List<ApiContact>>> {
		return apiDataSource.getContact()
	}

	override fun getPage(name: String): Flow<NetworkResponseState<ApiPage>> {
		return apiDataSource.getPage(name)
	}

	override fun forgotPassword(email: String):Flow<NetworkResponseState<String>> {
		return  apiDataSource.forgotPassword(email).map {
			when(it){
				NetworkResponseState.Loading -> NetworkResponseState.Loading
				is NetworkResponseState.Error -> it
				is NetworkResponseState.Success -> NetworkResponseState.Success(it.result.message)

			}
		}
	}

	private fun saveFile(response: Response<ResponseBody>, name: String):Flow<Int> = flow {
		val  fileName =
			response.headers()["Content-Disposition"]?.split(";")?.get(1)?.split("=")?.get(1)
				?.removeSurrounding("\"")

		val destination = File(
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
			fileName?: name
		)

		response.body()?.let{ body ->
			body.byteStream().use { inputStream ->
				FileOutputStream(destination).use { outputStream ->
					val totalBytes = body.contentLength()
					var byteCopied = 0
					val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
					var bytes = inputStream.read(buffer)

					while (bytes >= 0){
						outputStream.write(buffer, 0, bytes)
						byteCopied += bytes
						bytes = inputStream.read(buffer)
						val progress = (byteCopied.toFloat() / totalBytes * 100).toInt()
						emit(progress)
					}
				}
			}
		}
	}
}