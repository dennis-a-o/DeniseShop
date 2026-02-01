package com.example.deniseshop.core.data.repository

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.mappers.toAddress
import com.example.deniseshop.core.data.mappers.toBrand
import com.example.deniseshop.core.data.mappers.toCart
import com.example.deniseshop.core.data.mappers.toCategory
import com.example.deniseshop.core.data.mappers.toCheckout
import com.example.deniseshop.core.data.mappers.toContact
import com.example.deniseshop.core.data.mappers.toFlashSale
import com.example.deniseshop.core.data.mappers.toHome
import com.example.deniseshop.core.data.mappers.toOrderDetail
import com.example.deniseshop.core.data.mappers.toPage
import com.example.deniseshop.core.data.mappers.toProductDetail
import com.example.deniseshop.core.data.mappers.toProductFilter
import com.example.deniseshop.core.data.mappers.toReviewStat
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.data.paging.BrandProductsPagingSource
import com.example.deniseshop.core.data.paging.BrandsPagingSource
import com.example.deniseshop.core.data.paging.CategoryProductsPagingSource
import com.example.deniseshop.core.data.paging.CouponsPagingSource
import com.example.deniseshop.core.data.paging.FaqsPagingSource
import com.example.deniseshop.core.data.paging.FlashSaleProductsPagingSource
import com.example.deniseshop.core.data.paging.OrdersPagingSource
import com.example.deniseshop.core.data.paging.ProductsPagingSource
import com.example.deniseshop.core.data.paging.RecentViewedProductsPagingSource
import com.example.deniseshop.core.data.paging.ReviewsPagingSource
import com.example.deniseshop.core.data.paging.WishlistPagingSource
import com.example.deniseshop.core.domain.model.Address
import com.example.deniseshop.core.domain.model.Brand
import com.example.deniseshop.core.domain.model.Cart
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.Checkout
import com.example.deniseshop.core.domain.model.Contact
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.FlashSale
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.OrderDetail
import com.example.deniseshop.core.domain.model.Page
import com.example.deniseshop.core.domain.model.PageType
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductDetail
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.ReviewStat
import com.example.deniseshop.core.domain.model.Wishlist
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteShopRepository @Inject constructor(
	private val remoteDeniseShopDataSource: RemoteDeniseShopDataSource,
	private val settingDataSource: SettingDataSource,
	@ApplicationContext val context: Context,
): ShopRepository {
	override suspend fun getHome(): Result<Home, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getHome()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toHome())
		}
	}

	override suspend fun getCategories(): Result<List<Category>, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getCategories()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success( data = res.data.map { it.toCategory() })
		}
	}

	override  fun getWishlists(limit: Int): Flow<PagingData<Wishlist>> {
		return Pager(
			config = PagingConfig(pageSize = limit),
			pagingSourceFactory = {
				WishlistPagingSource(
					remote = remoteDeniseShopDataSource
				)
			}
		).flow
	}

	override suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.addToWishlist(productId)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> {
				settingDataSource.saveWishlistItemId(productId)
				Result.Success(Unit)
			}
		}
	}

	override suspend fun removeFromWishlist(id: Long): Result<Unit, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.removeFromWishlist(id)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> {
				settingDataSource.deleteWishlistItem(id)
				Result.Success(Unit)
			}
		}
	}

	override fun getProducts(filterParams: ProductFilterParams): ProductsPagingSource {
		return ProductsPagingSource(
			settingDataSource = settingDataSource,
			filterParams = filterParams,
			remote = remoteDeniseShopDataSource
		)
	}

	override suspend fun getProductFilter(
		categoryId: Long,
		brandId: Long
	): Result<ProductFilter, DataError.Remote> {
		return when(
			val res = remoteDeniseShopDataSource.getProductFilter(
				categoryId = categoryId,
				brandId = brandId
			)
		) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success( data = res.data.toProductFilter())
		}
	}

	override suspend fun getCategory(id: Long): Result<Category, DataError.Remote> {
		return when(
			val res = remoteDeniseShopDataSource.getCategory(id)
		) {
			is Result.Error ->  Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toCategory())
		}
	}

	override fun getCategoryProducts(
		id: Long,
		filterParams: ProductFilterParams
	): CategoryProductsPagingSource {
		return CategoryProductsPagingSource(
			categoryId = id,
			filterParams = filterParams,
			remote = remoteDeniseShopDataSource
		)
	}

	override suspend fun getBrand(id: Long): Result<Brand, DataError.Remote> {
		return when(
			val res = remoteDeniseShopDataSource.getBrand(id)
		) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toBrand())
		}
	}

	override suspend fun getCategoryBrands(categoryId: Long): Result<List<Brand>, DataError.Remote> {
		return when(
			val res = remoteDeniseShopDataSource.getCategoryBrands(categoryId)
		) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.map { it.toBrand() })
		}
	}

	override fun getBrands(limit: Int): Flow<PagingData<Brand>> {
		return Pager(
			config = PagingConfig(pageSize = limit, initialLoadSize = 20),
			pagingSourceFactory = {
				BrandsPagingSource(
					remote = remoteDeniseShopDataSource
				)
			}
		).flow
	}

	override fun getBrandProducts(
		brandId: Long,
		filterParams: ProductFilterParams
	): BrandProductsPagingSource {
		return BrandProductsPagingSource(
			brandId = brandId,
			filterParams = filterParams,
			remote = remoteDeniseShopDataSource
		)
	}

	override suspend fun getCart(): Result<Cart, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getCart()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> Result.Success(res.data.toCart())
		}
	}

	override suspend fun addToCart(productData: ProductData): Result<Unit, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.addToCart(productData)){
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> {
			settingDataSource.saveCartItemId(productData.productId)
			Result.Success(Unit)
		}
		}
	}

	override suspend fun removeFromCart(productId: Long): Result<Unit, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.removeFromCart(productId)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> {
				settingDataSource.deleteCartItem(productId)
				Result.Success(Unit)
			}
		}
	}

	override suspend fun clearCart(): Result<Unit, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.clearCart()){
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> {
				settingDataSource.clearCart()
				Result.Success(Unit)
			}
		}
	}

	override suspend fun increaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.increaseCartItemQuantity(productId)
	}

	override suspend fun decreaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.decreaseCartItemQuantity(productId)
	}

	override suspend fun applyCoupon(coupon: String): Result<String, DataError> {
		return when(val res = remoteDeniseShopDataSource.applyCoupon(coupon)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> Result.Success(res.data.message)
		}
	}

	override suspend fun clearCoupon(): Result<String, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.clearCoupon()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> Result.Success(res.data.message)
		}
	}

	override suspend fun getFlashSale(id: Long): Result<FlashSale, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getFlashSale(id)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success-> Result.Success(res.data.toFlashSale())
		}
	}

	override fun getFlashSaleProducts(
		flashSaleId: Long,
		filterParams: ProductFilterParams
	): FlashSaleProductsPagingSource {
		return FlashSaleProductsPagingSource(
			flashSaleId = flashSaleId,
			filterParams = filterParams,
			remote = remoteDeniseShopDataSource
		)
	}

	override fun getRecentViewedProducts(): RecentViewedProductsPagingSource {
		return RecentViewedProductsPagingSource(
			remote = remoteDeniseShopDataSource
		)
	}

	override suspend fun clearRecentViewedProducts(): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.clearRecentViewedProducts()
	}

	override suspend fun getProductDetail(id: Long): Result<ProductDetail, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getProductDetail(id)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toProductDetail())
		}
	}

	override suspend fun setProductViewed(id: Long): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.setProductViewed(id)
	}

	override suspend fun getProductReviewStat(productId: Long): Result<ReviewStat, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getProductReviewStat((productId))) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toReviewStat())
		}
	}

	override fun getReviews(productId: Long): ReviewsPagingSource {
		return ReviewsPagingSource(
			productId = productId,
			remote = remoteDeniseShopDataSource
		)
	}

	override suspend fun getCheckout(): Result<Checkout, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getCheckout()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toCheckout())
		}
	}

	override suspend fun placeOrder(): Result<String, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.placeOrder()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success ->{
				settingDataSource.clearCart()
				Result.Success(res.data.message)
			}
		}
	}

	override suspend fun createPaypalPayment(): Result<String, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.createPaypalPayment()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.url)
		}
	}

	override suspend fun paypalPaymentSuccess(
		token: String,
		payerId: String
	): Result<String, DataError> {
		return when(
			val res = remoteDeniseShopDataSource.paypalPaymentSuccess(
				token = token,
				payerId = payerId
			)
		) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success ->{
				//clear local cart
				settingDataSource.clearCart()

				Result.Success(res.data.message)
			}
		}
	}

	override suspend fun paypalPaymentCancel(): Result<String, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.paypalPaymentCancel()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.message)
		}
	}

	override suspend fun getAddresses(): Result<List<Address>, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getAddresses()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.map { it.toAddress() })
		}
	}

	override suspend fun getAddress(id: Long): Result<Address?, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getAddress(id)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data?.toAddress())
		}
	}

	override suspend fun getCountries(): Result<List<String>, DataError.Remote> {
		return remoteDeniseShopDataSource.getCountries()
	}

	override suspend fun addAddress(address: Address): Result<String, DataError> {
		return when(val res = remoteDeniseShopDataSource.addAddress(address)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.message)
		}
	}

	override suspend fun updateAddress(address: Address): Result<String, DataError> {
		return when(val res = remoteDeniseShopDataSource.updateAddress(address)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.message)
		}
	}

	override suspend fun setDefaultAddress(id: Long): Result<String, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.setDefaultAddress(id)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.message)
		}
	}

	override suspend fun deleteAddress(id: Long): Result<String, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.deleteAddress(id)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.message)
		}
	}

	override fun getOrders(): OrdersPagingSource {
		return OrdersPagingSource(
			remote = remoteDeniseShopDataSource
		)
	}

	override suspend fun getOrderDetail(id: Long): Result<OrderDetail?, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getOrderDetail(id)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data?.toOrderDetail())
		}
	}

	override suspend fun addReview(
		orderItemId: Long,
		review: String,
		rating: Int
	): Result<String, DataError> {
		return when(
			val res = remoteDeniseShopDataSource.addReview(
				orderItemId = orderItemId,
				review = review,
				rating = rating
			)
		) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.message)
		}
	}

	override suspend fun downloadItem(id: Long): kotlin.Result<String> {
		return withContext(Dispatchers.IO){
			try {
				val response = remoteDeniseShopDataSource.downloadItem(id)
				if (response.isSuccessful){
					val body = response.body() ?: return@withContext kotlin.Result.failure(Exception("Empty body"))
					val  fileName = response.headers()["Content-Disposition"]
						?.split(";")?.get(1)
						?.split("=")?.get(1)
						?.removeSurrounding("\"")?: "item.zip"

					val resolver = context.contentResolver

					val contentValues = ContentValues().apply {
						put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
						put(MediaStore.MediaColumns.MIME_TYPE, "file/zip")

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
							put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
							put(MediaStore.MediaColumns.IS_PENDING, 1)
						}
					}

					val collection = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
						MediaStore.Downloads.EXTERNAL_CONTENT_URI
					}else{
						MediaStore.Files.getContentUri("external")
					}

					val fileUri = resolver.insert(collection,contentValues)

					fileUri?.let { uri ->
						resolver.openOutputStream(uri)?.use { outputStream ->
							body.byteStream().use { inputStream ->
								inputStream.copyTo(outputStream)
							}
						}

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
							contentValues.clear()
							contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
							resolver.update(uri, contentValues, null, null)
						}

					}

					kotlin.Result.success("Downloaded $fileName successfully,check your Downloads folder.")
				}else{
					kotlin.Result.failure(Exception("Download failed"))
				}
			}catch (e: Exception){
				kotlin.Result.failure(e)
			}
		}
	}

	override suspend fun downloadInvoice(orderId: Long): kotlin.Result<String> {
		return withContext(Dispatchers.IO){
			try {
				val response = remoteDeniseShopDataSource.downloadInvoice(orderId)
				if (response.isSuccessful){
					val body = response.body() ?: return@withContext kotlin.Result.failure(Exception("Empty body"))
					val  fileName = response.headers()["Content-Disposition"]
						?.split(";")?.get(1)
						?.split("=")?.get(1)
						?.removeSurrounding("\"") ?: "Invoice.pdf"

					val resolver = context.contentResolver

					val contentValues = ContentValues().apply {
						put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
						put(MediaStore.MediaColumns.MIME_TYPE, "document/pdf")

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
							put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
							put(MediaStore.MediaColumns.IS_PENDING, 1)
						}
					}

					val collection = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
						MediaStore.Downloads.EXTERNAL_CONTENT_URI
					}else{
						MediaStore.Files.getContentUri("external")
					}

					val fileUri = resolver.insert(collection,contentValues)

					fileUri?.let { uri ->
						resolver.openOutputStream(uri)?.use { outputStream ->
							body.byteStream().use { inputStream ->
								inputStream.copyTo(outputStream)
							}
						}

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
							contentValues.clear()
							contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
							resolver.update(uri, contentValues, null, null)
						}

					}

					kotlin.Result.success("Downloaded $fileName successfully,check your Download folder.")
				}else{
					kotlin.Result.failure(Exception("Download failed"))
				}
			}catch (e: Exception){
				kotlin.Result.failure(e)
			}
		}
	}

	override fun getFaqs() = FaqsPagingSource(
		remote = remoteDeniseShopDataSource
	)

	override fun getCoupons() = CouponsPagingSource(
		remote = remoteDeniseShopDataSource
	)

	override suspend fun getContact(): Result<List<Contact>, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getContact()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.map { it.toContact() })
		}
	}

	override suspend fun getPage(page: PageType): Result<Page, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getPage(page)) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toPage())
		}
	}

	override suspend fun syncWishlistItems() {
		withContext(Dispatchers.IO){
			try {
				val items = remoteDeniseShopDataSource.getWishlists(1,20)
					.map { it.productId }

				settingDataSource.saveWishlistItems(items)
			}catch (_: Exception){ }
		}
	}

	override suspend fun syncCartItems() {
		withContext(Dispatchers.IO){
			remoteDeniseShopDataSource.getCart()
				.onSuccess { cart ->
					val items = cart.cartItems.map { it.productId }
					settingDataSource.saveCartItems(items)
				}
				.onError {  }
		}
	}
}