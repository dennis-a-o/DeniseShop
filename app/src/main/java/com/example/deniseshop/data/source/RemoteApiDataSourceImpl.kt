package com.example.deniseshop.data.source

import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.data.api.DeniseShopApi
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
import com.example.deniseshop.data.models.ApiImageUpdate
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
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.models.ApiUserUpdate
import com.example.deniseshop.data.models.ApiWishlist
import com.example.deniseshop.domain.models.UpdateProfileData
import com.example.deniseshop.domain.models.UserSignUpData
import com.example.deniseshop.ui.models.ProductQueryParams
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.awaitResponse
import javax.inject.Inject

class RemoteApiDataSourceImpl @Inject constructor(
	private val deniseShopApi: DeniseShopApi
): RemoteApiDataSource {
	companion object {
		const val UNPROCESSABLE_ENTITY = 422
	}

	override fun signUp(userSignUpData: UserSignUpData): Flow<NetworkResponseState<ApiResponse>> {
		return flow{
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.signUp(
					firstName = userSignUpData.firstName,
					lastName =  userSignUpData.lastName,
					phone = userSignUpData.phone,
					email = userSignUpData.email,
					password = userSignUpData.password,
					acceptTerms = userSignUpData.acceptTerms
				).awaitResponse()

				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun signIn(email: String,password: String): Flow<NetworkResponseState<ApiSignInResponse>> {
		return flow{
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.signIn(
					email = email,
					password = password
				).awaitResponse()

				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun forgotPassword(email: String): Flow<NetworkResponseState<ApiResponse>> {
		return flow{
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.forgotPassword(email).awaitResponse()
				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getUser(): Flow<NetworkResponseState<ApiUser>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getProfile()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun updateUser(updateProfileData: UpdateProfileData): Flow<NetworkResponseState<ApiUserUpdate>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.updateProfile(
					firstName = updateProfileData.firstName,
					lastName = updateProfileData.lastName,
					email = updateProfileData.email,
					phone = updateProfileData.phone,
				).awaitResponse()

				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun uploadProfileImage(body: MultipartBody.Part): Flow<NetworkResponseState<ApiImageUpdate>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.uploadProfileImage(body).awaitResponse()

				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun changePassword(
		currentPassword: String,
		newPassword: String
	): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.changePassword(currentPassword, newPassword).awaitResponse()

				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun logout(refreshToken: String): ApiResponse {
		return  deniseShopApi.logout(refreshToken)

	}

	override suspend fun deleteAccount(): ApiResponse {
		 return deniseShopApi.deleteAccount()
	}

	override  fun getHome(): Flow<NetworkResponseState<ApiHome>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getHome()
				emit(NetworkResponseState.Success(response))
			}catch (e :Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getCategories(): Flow<NetworkResponseState<List<ApiCategory>>> {
		return flow{
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getAllCategory()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun getCategoryProduct(category: Long, queryParams: ProductQueryParams): List<ApiProduct> {
		return  deniseShopApi.getCategoryProduct(
			category = category,
			page = queryParams.page,
			pageSize = queryParams.pageSize,
			sortBy = queryParams.sortBy,
			minPrice = queryParams.minPrice,
			maxPrice = queryParams.maxPrice,
			categories = queryParams.categories,
			brands = queryParams.brands,
			colors = queryParams.colors,
			sizes = queryParams.sizes,
			rating = queryParams.rating
		)
	}

	override fun getCategory(id: Long): Flow<NetworkResponseState<ApiCategory>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getCategory(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception) {
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getProductFilter(category: Long, brand: Long): Flow<NetworkResponseState<ApiProductFilter>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val mockResponse = deniseShopApi.getProductFilter(category, brand)
				emit(NetworkResponseState.Success(mockResponse))
			} catch (e: Exception) {
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun getAllBrand(page: Int, pageSize: Int): List<ApiBrand> {
		return deniseShopApi.getAllBrand(page, pageSize)
	}

	override fun getBrandByCategory(category: Long): Flow<NetworkResponseState<List<ApiBrand>>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getBrandByCategory(category)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getBrand(id: Long): Flow<NetworkResponseState<ApiBrand>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response =  deniseShopApi.getBrand(id)
				emit(NetworkResponseState.Success(response))
			} catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun getBrandProduct(brand: Long, queryParams: ProductQueryParams): List<ApiProduct> {
		return  deniseShopApi.getBrandProduct(
			brand = brand,
			page = queryParams.page,
			pageSize = queryParams.pageSize,
			sortBy = queryParams.sortBy,
			minPrice = queryParams.minPrice,
			maxPrice = queryParams.maxPrice,
			categories = queryParams.categories,
			brands = queryParams.brands,
			colors = queryParams.colors,
			sizes = queryParams.sizes,
			rating = queryParams.rating
		)
	}

	override suspend fun getProducts(queryParams: ProductQueryParams): List<ApiProduct> {
		return deniseShopApi.getProducts(
			query = queryParams.query,
			page = queryParams.page,
			pageSize = queryParams.pageSize,
			sortBy = queryParams.sortBy,
			minPrice = queryParams.minPrice,
			maxPrice = queryParams.maxPrice,
			categories = queryParams.categories,
			brands = queryParams.brands,
			colors = queryParams.colors,
			sizes = queryParams.sizes,
			rating = queryParams.rating
		)
	}

	override fun getFlashSale(id: Long): Flow<NetworkResponseState<ApiFlashSale>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getFlashSale(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun getFlashSaleProduct(flashSale: Long, queryParams: ProductQueryParams): List<ApiProduct> {
		return deniseShopApi.getFlashSaleProduct(
			flashSale = flashSale,
			page = queryParams.page,
			pageSize = queryParams.pageSize,
			sortBy = queryParams.sortBy,
			minPrice = queryParams.minPrice,
			maxPrice = queryParams.maxPrice,
			categories = queryParams.categories,
			brands = queryParams.brands,
			colors = queryParams.colors,
			sizes = queryParams.sizes,
			rating = queryParams.rating
		)
	}

	override suspend fun getRecentViewedProduct(page: Int, pageSize: Int): List<ApiProduct> {
		return deniseShopApi.getRecentViewedProduct(page, pageSize)
	}

	override fun clearRecentViewed(): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response =  deniseShopApi.deleteRecentViewedProduct()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getProductDetail(id: Long): Flow<NetworkResponseState<ApiProductDetail>> {
		return flow {
			emit(NetworkResponseState.Loading)
			val response = deniseShopApi.getProductDeatil(id)
			emit(NetworkResponseState.Success(response))
		}.catch {
			emit(NetworkResponseState.Error(Exception(it)))
		}
	}

	override fun setViewed(productId: Long): Flow<NetworkResponseState<String>> {
		return flow {
			emit(NetworkResponseState.Loading)
			val response = deniseShopApi.setViewed(productId)
			emit(NetworkResponseState.Success(response.message))
		}.catch {
			emit(NetworkResponseState.Error(Exception(it)))
		}
	}

	override fun getReviewStat(product: Long): Flow<NetworkResponseState<ApiReviewStat>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getReviewStat(product)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun getReviews(product: Long, page: Int, pageSize: Int): List<ApiReview> {
		return deniseShopApi.getReviews(product, page, pageSize)
	}

	override suspend fun getWishlist(page: Int, pageSize: Int): List<ApiWishlist> {
		return deniseShopApi.getWishlist(page, pageSize)
	}

	override fun addWishlist(product: Long): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.addWishlist(product)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun removeWishlist(id: Long): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.removeWishlist(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getWishlistProduct(): Flow<NetworkResponseState<List<Long>>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getWishlistProduct()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getCart(): Flow<NetworkResponseState<ApiCart>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getCart()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun addCart(product: Long, quantity: Int?,size: String?, color:String?): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.addCart(product, quantity, color, size)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun clearCart(): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.clearCart()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun removeCart(product: Long): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response =  deniseShopApi.removeCart(product)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun increaseCartQuantity(id: Long): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response =  deniseShopApi.increaseCartQuantity(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun decreaseCartQuantity(id: Long): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response =  deniseShopApi.decreaseCartQuantity(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun applyCoupon(coupon: String): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.applyCoupon(coupon)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun clearCoupon(): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response =  deniseShopApi.clearCoupon()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getCountries(): Flow<NetworkResponseState<List<String>>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getCountries()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getAllAddress(): Flow<NetworkResponseState<List<ApiAddress>>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getAllAddress()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun addAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.addAddress(
					name = apiAddress.name,
					email =  apiAddress.email,
					phone = apiAddress.phone,
					country = apiAddress.country,
					state = apiAddress.state,
					city = apiAddress.city,
					address = apiAddress.address,
					zipCode = apiAddress.zipCode,
					type = apiAddress.type,
					default = if(apiAddress.default) 1 else 0
				).awaitResponse()
				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun updateAddress(apiAddress: ApiAddress): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.updateAddress(
					id = apiAddress.id,
					name = apiAddress.name,
					email =  apiAddress.email,
					phone = apiAddress.phone,
					country = apiAddress.country,
					state = apiAddress.state,
					city = apiAddress.city,
					address = apiAddress.address,
					zipCode = apiAddress.zipCode,
					type = apiAddress.type,
					default = if(apiAddress.default) 1 else 0
				).awaitResponse()

				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun setAddressDefault(id: Long): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.setAddressDefault(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun removeAddress(id: Long): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.removeAddress(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun checkout(): Flow<NetworkResponseState<ApiCheckout>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.checkout()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun placeOrder(): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.placeOrder()
				emit(NetworkResponseState.Success(response))
			}catch(e:Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun createPaypalPayment(): Flow<NetworkResponseState<String>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.createPaypalPayment()
				emit(NetworkResponseState.Success(response.url))
			}catch(e:Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun paypalPaymentSuccess(token:String, payerId: String): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.paypalPaymentSuccess(token = token, payerId = payerId)
				emit(NetworkResponseState.Success(response))
			}catch(e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun paypalPaymentCancel(): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.paypalPaymentCancel()
				emit(NetworkResponseState.Success(response))
			}catch(e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun getOrders(page: Int, pageSize: Int): List<ApiOrder> {
		return deniseShopApi.getOrders(page,pageSize)
	}

	override fun getOrderDetail(id: Long): Flow<NetworkResponseState<ApiOrderDetail>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getOrderDetail(id)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun addReview(
		itemId: Long,
		rating: Int,
		review: String
	): Flow<NetworkResponseState<ApiResponse>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.addReview(itemId, rating, review).awaitResponse()
				if (response.isSuccessful){
					emit(NetworkResponseState.Success(response.body()!!))
				}else if(response.code() == UNPROCESSABLE_ENTITY){
					emit(NetworkResponseState.Error(Exception(parseApiError(response.errorBody()).message)))
				}else{
					throw Exception(response.message())
				}
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override suspend fun downloadOrderItem(itemId: Long): Response<ResponseBody> {
		return deniseShopApi.downloadOrderItem(itemId)
	}

	override suspend fun downloadOrderInvoice(orderId: Long): Response<ResponseBody> {
		return deniseShopApi.downloadOrderInvoice(orderId)
	}

	override suspend fun getFaqs(page: Int, pageSize: Int): List<ApiFaq> {
		return deniseShopApi.getFaqs(page, pageSize)
	}

	override suspend fun getCoupons(page: Int, pageSize: Int): List<ApiCoupon> {
		return deniseShopApi.getCoupons(page, pageSize)
	}

	override fun getContact(): Flow<NetworkResponseState<List<ApiContact>>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getContact()
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	override fun getPage(name: String): Flow<NetworkResponseState<ApiPage>> {
		return flow {
			emit(NetworkResponseState.Loading)
			try {
				val response = deniseShopApi.getPage(name)
				emit(NetworkResponseState.Success(response))
			}catch (e: Exception){
				emit(NetworkResponseState.Error(e))
			}
		}
	}

	private fun parseApiError(response: ResponseBody?): ApiError{
		val errorBody = response?.source()?.readUtf8()?:"{}"

		val moshi = Moshi.Builder().build()
		val apiErrorAdapter = moshi.adapter(ApiError::class.java)
		val apiError = apiErrorAdapter.fromJson(errorBody) ?: ApiError(success = false, message = "Unexpected error occurred", errors = null)

		return apiError
	}
}