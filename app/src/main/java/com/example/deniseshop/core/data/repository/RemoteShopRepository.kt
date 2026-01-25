package com.example.deniseshop.core.data.repository

import android.os.Environment
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.deniseshop.core.data.datastore.SettingDataSource
import com.example.deniseshop.core.data.mappers.toAddress
import com.example.deniseshop.core.data.mappers.toBrand
import com.example.deniseshop.core.data.mappers.toCart
import com.example.deniseshop.core.data.mappers.toCategory
import com.example.deniseshop.core.data.mappers.toCheckout
import com.example.deniseshop.core.data.mappers.toFlashSale
import com.example.deniseshop.core.data.mappers.toHome
import com.example.deniseshop.core.data.mappers.toProductDetail
import com.example.deniseshop.core.data.mappers.toProductFilter
import com.example.deniseshop.core.data.mappers.toReviewStat
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.data.paging.BrandProductsPagingSource
import com.example.deniseshop.core.data.paging.BrandsPagingSource
import com.example.deniseshop.core.data.paging.CategoryProductsPagingSource
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
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.FlashSale
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.OrderDetail
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductDetail
import com.example.deniseshop.core.domain.model.ProductFilter
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.ReviewStat
import com.example.deniseshop.core.domain.model.Wishlist
import com.example.deniseshop.core.domain.repository.ShopRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okio.use
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class RemoteShopRepository @Inject constructor(
	private val remoteDeniseShopDataSource: RemoteDeniseShopDataSource,
	private val settingDataSource: SettingDataSource
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
		return remoteDeniseShopDataSource.addToWishlist(productId)
	}

	override suspend fun removeFromWishlist(id: Long): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.removeFromWishlist(id)
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
		return remoteDeniseShopDataSource.addToCart(productData)
	}

	override suspend fun removeFromCart(productId: Long): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.removeFromCart(productId)
	}

	override suspend fun clearCart(): Result<Unit, DataError.Remote> {
		return remoteDeniseShopDataSource.clearCart()
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
			is Result.Success -> Result.Success(res.data.message)
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
			is Result.Success -> Result.Success(res.data.message)
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
		TODO("Not yet implemented")
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
						?.removeSurrounding("\"")

					val destination = File(
						Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
						fileName?: "Item.zip"
					)

					body.byteStream().use { input ->
						destination.outputStream().use { output ->
							input.copyTo(output)
						}
					}
					kotlin.Result.success("Downloaded ${destination.name} successfully,check your Downloads folder.")
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
						?.removeSurrounding("\"")

					val destination = File(
						Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
						fileName?: "Invoice.pdf"
					)

					body.byteStream().use { input ->
						destination.outputStream().use { output ->
							input.copyTo(output)
						}
					}
					kotlin.Result.success("Downloaded ${destination.name} successfully,check your Downloads folder.")
				}else{
					kotlin.Result.failure(Exception("Download failed"))
				}
			}catch (e: Exception){
				kotlin.Result.failure(e)
			}
		}
	}
}