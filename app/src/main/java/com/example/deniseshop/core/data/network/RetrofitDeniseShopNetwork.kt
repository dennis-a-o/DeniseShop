package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.BrandDto
import com.example.deniseshop.core.data.dto.CartDto
import com.example.deniseshop.core.data.dto.CategoryDto
import com.example.deniseshop.core.data.dto.CheckoutDto
import com.example.deniseshop.core.data.dto.FlashSaleDto
import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.MessageDto
import com.example.deniseshop.core.data.dto.PaymentUrlDto
import com.example.deniseshop.core.data.dto.ProductDetailDto
import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.data.dto.ProductFilterDto
import com.example.deniseshop.core.data.dto.ReviewDto
import com.example.deniseshop.core.data.dto.ReviewStatDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.data.dto.WishlistDto
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import okhttp3.MultipartBody
import retrofit2.awaitResponse
import javax.inject.Inject

class RetrofitDeniseShopNetwork @Inject constructor(
	private val api: RetrofitDeniseShopNetworkApi
): RemoteDeniseShopDataSource {
	override suspend fun signUp(user: UserSignUp): Result<Unit, DataError> {
		return safeCallAwaitable <Unit> {
			api.signUp(
				firstName = user.firstName,
				lastName = user.lastName,
				email = user.email,
				phone = user.phone,
				password = user.password,
				acceptTerms = user.acceptTerms
			).awaitResponse()
		}
	}

	override suspend fun signIn(
		email: String,
		password: String
	): Result<UserCredentialDto, DataError> {
		return safeCallAwaitable <UserCredentialDto> {
			api.signIn(
				email = email,
				password = password
			).awaitResponse()
		}
	}

	override suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.forgotPassword(email)
		}
	}

	override suspend fun getUser(): Result<UserDto, DataError.Remote> {
		return safeCall<UserDto> {
			api.getUser()
		}
	}

	override suspend fun updateUser(user: User): Result<UserDto, DataError> {
		return safeCallAwaitable<UserDto> {
			api.updateUser(
				firstName = user.firstName,
				lastName = user.lastName,
				email = user.email,
				phone = user.phone,
			).awaitResponse()
		}
	}

	override suspend fun uploadUserImage(body: MultipartBody.Part): Result<ImageDto, DataError.Remote> {
		return safeCall<ImageDto> {
			api.uploadUserImage(body)
		}
	}

	override suspend fun changePassword(
		currentPassword: String,
		newPassword: String
	): Result<Unit, DataError> {
		return safeCallAwaitable<Unit> {
			api.changePassword(
				currentPassword = currentPassword,
				newPassword = newPassword
			).awaitResponse()
		}
	}

	override suspend fun logout(): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.logout()
		}
	}

	override suspend fun deleteUser(): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.deleteAccount()
		}
	}

	override suspend fun getHome(): Result<HomeDto, DataError.Remote> {
		return safeCall<HomeDto> {
			api.getHome()
		}
	}

	override suspend fun getCategories(): Result<List<CategoryDto>, DataError.Remote> {
		return safeCall<List<CategoryDto>> {
			api.getCategories()
		}
	}

	override suspend fun getWishlists(
		page: Int,
		pageSize: Int
	): List<WishlistDto> {
		return api.getWishlists(
			page = page,
			pageSize = pageSize
		)

	}

	override suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.addToWishlist(productId)
		}
	}

	override suspend fun removeFromWishlist(id: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.removeFromWishlist(id)
		}
	}

	override suspend fun getProducts(filterParams: ProductFilterParams): List<ProductDto> {
		return api.getProducts(
				query = filterParams.query,
				page = filterParams.page,
				pageSize = filterParams.pageSize,
				sortBy = filterParams.sortBy.value,
				minPrice = filterParams.minPrice,
				maxPrice = filterParams.maxPrice,
				categories = filterParams.categories,
				brands = filterParams.brands,
				colors = filterParams.categories,
				sizes = filterParams.sizes,
				rating = filterParams.rating
			)

	}

	override suspend fun getProductFilter(
		categoryId: Long,
		brandId: Long
	): Result<ProductFilterDto, DataError.Remote> {
		return safeCall<ProductFilterDto> {
			api.getProductFilter(
				category = categoryId,
				brand = brandId
			)
		}
	}

	override suspend fun getCategory(id: Long): Result<CategoryDto, DataError.Remote> {
		return safeCall<CategoryDto> {
			api.getCategory(id)
		}
	}

	override suspend fun getCategoryProducts(
		categoryId: Long,
		filterParams: ProductFilterParams
	): List<ProductDto> {
		return api.getCategoryProducts(
			category = categoryId,
			page = filterParams.page,
			pageSize = filterParams.pageSize,
			sortBy = filterParams.sortBy.value,
			minPrice = filterParams.minPrice,
			maxPrice = filterParams.maxPrice,
			categories = filterParams.categories,
			brands = filterParams.brands,
			colors = filterParams.categories,
			sizes = filterParams.sizes,
			rating = filterParams.rating
		)
	}

	override suspend fun getBrand(id: Long): Result<BrandDto, DataError.Remote> {
		return safeCall<BrandDto> {
			api.getBrand(id)
		}
	}

	override suspend fun getBrands(
		page: Int,
		pageSize: Int
	): List<BrandDto> {
		return api.getBrands(
			page = page,
			pageSize = pageSize
		)
	}

	override suspend fun getBrandProducts(
		brandId: Long,
		filterParams: ProductFilterParams
	): List<ProductDto> {
		return api.getBrandProducts(
			brand = brandId,
			page = filterParams.page,
			pageSize = filterParams.pageSize,
			sortBy = filterParams.sortBy.value,
			minPrice = filterParams.minPrice,
			maxPrice = filterParams.maxPrice,
			categories = filterParams.categories,
			brands = filterParams.brands,
			colors = filterParams.categories,
			sizes = filterParams.sizes,
			rating = filterParams.rating
		)
	}

	override suspend fun getCategoryBrands(categoryId: Long): Result<List<BrandDto>, DataError.Remote> {
		return safeCall<List<BrandDto>> {
			api.getCategoryBrands(categoryId)
		}
	}

	override suspend fun getCart(): Result<CartDto, DataError.Remote> {
		return safeCall<CartDto>{
			api.getCart()
		}
	}

	override suspend fun addToCart(productData: ProductData): Result<Unit, DataError.Remote> {
		return safeCall {
			api.addToCart(
				product = productData.productId,
				quantity = productData.quantity,
				color = productData.color,
				size = productData.size
			)
		}
	}

	override suspend fun removeFromCart(productId: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.removeFromCart(productId)
		}
	}

	override suspend fun clearCart(): Result<Unit, DataError.Remote> {
		return safeCall {
			api.clearCart()
		}
	}

	override suspend fun increaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.increaseCartItemQuantity(productId)
		}
	}

	override suspend fun decreaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.decreaseCartItemQuantity(productId)
		}
	}

	override suspend fun applyCoupon(coupon: String): Result<MessageDto, DataError> {
		return safeCallAwaitable<MessageDto> {
			api.applyCoupon(coupon).awaitResponse()
		}
	}

	override suspend fun clearCoupon(): Result<MessageDto, DataError.Remote> {
		return safeCall<MessageDto> {
			api.clearCoupon()
		}
	}

	override suspend fun getFlashSale(id: Long): Result<FlashSaleDto, DataError.Remote> {
		return safeCall {
			api.getFlashSale(id)
		}
	}

	override suspend fun getFlashSaleProducts(
		flashSaleId: Long,
		filterParams: ProductFilterParams
	): List<ProductDto> {
		return  api.getFlashSaleProducts(
			flashSale = flashSaleId,
			page = filterParams.page,
			pageSize = filterParams.pageSize,
			sortBy = filterParams.sortBy.value,
			minPrice = filterParams.minPrice,
			maxPrice = filterParams.maxPrice,
			categories = filterParams.categories,
			brands = filterParams.brands,
			colors = filterParams.categories,
			sizes = filterParams.sizes,
			rating = filterParams.rating
		)
	}

	override suspend fun getRecentViewedProducts(
		page: Int,
		pageSize: Int
	): List<ProductDto> {
		return api.getRecentViewedProducts(
			page = page,
			pageSize = pageSize
		)
	}

	override suspend fun clearRecentViewedProducts(): Result<Unit, DataError.Remote> {
		return safeCall {
			api.clearRecentViewedProducts()
		}
	}

	override suspend fun getProductDetail(id: Long): Result<ProductDetailDto, DataError.Remote> {
		return safeCall<ProductDetailDto> {
			api.getProductDetail(id)
		}
	}

	override suspend fun setProductViewed(id: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.setProductViewed(id)
		}
	}

	override suspend fun getProductReviewStat(productId: Long): Result<ReviewStatDto, DataError.Remote> {
		return safeCall<ReviewStatDto> {
			api.getReviewStat(productId)
		}
	}

	override suspend fun getReviews(
		productId: Long,
		page: Int,
		pageSize: Int
	): List<ReviewDto> {
		return api.getReviews(
			product = productId,
			page = page,
			pageSize = pageSize
		)
	}

	override suspend fun getCheckout(): Result<CheckoutDto, DataError.Remote> {
		return safeCall<CheckoutDto> {
			api.getCheckout()
		}
	}

	override suspend fun placeOrder(): Result<MessageDto, DataError.Remote> {
		return safeCall<MessageDto> {
			api.placeOrder()
		}
	}

	override suspend fun createPaypalPayment(): Result<PaymentUrlDto, DataError.Remote> {
		return safeCall<PaymentUrlDto> {
			api.createPaypalPayment()
		}
	}

	override suspend fun paypalPaymentSuccess(
		token: String,
		payerId: String
	): Result<MessageDto, DataError> {
		return safeCallAwaitable<MessageDto> {
			api.paypalPaymentSuccess(
				token = token,
				payerId = payerId
			).awaitResponse()
		}
	}

	override suspend fun paypalPaymentCancel(): Result<MessageDto, DataError.Remote> {
		return safeCall<MessageDto> {
			api.paypalPaymentCancel()
		}
	}
}