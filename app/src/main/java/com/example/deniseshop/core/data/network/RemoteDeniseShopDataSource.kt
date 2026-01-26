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
import com.example.deniseshop.core.domain.model.Address
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.PageType
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface RemoteDeniseShopDataSource {
	suspend fun signUp(user: UserSignUp): Result<Unit, DataError>
	suspend fun signIn(email: String, password: String): Result<UserCredentialDto, DataError>
	suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote>
	suspend fun getUser(): Result<UserDto, DataError.Remote>
	suspend fun updateUser(user: User): Result<UserDto, DataError>
	suspend fun uploadUserImage(body: MultipartBody.Part): Result<ImageDto, DataError.Remote>
	suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit, DataError>
	suspend fun logout(): Result<Unit, DataError.Remote>
	suspend fun deleteUser(): Result<Unit, DataError.Remote>
	suspend fun getHome(): Result<HomeDto, DataError.Remote>
	suspend fun getCategories(): Result<List<CategoryDto>, DataError.Remote>
	suspend fun getWishlists(page: Int, pageSize: Int): List<WishlistDto>
	suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote>
	suspend fun removeFromWishlist(id: Long): Result<Unit, DataError.Remote>
	suspend fun getProducts(filterParams: ProductFilterParams): List<ProductDto>
	suspend fun getProductFilter(categoryId: Long, brandId: Long): Result<ProductFilterDto, DataError.Remote>
	suspend fun getCategory(id: Long): Result<CategoryDto, DataError.Remote>
	suspend fun getCategoryProducts(categoryId:Long, filterParams: ProductFilterParams):List<ProductDto>
	suspend fun getBrand(id: Long): Result<BrandDto, DataError.Remote>
	suspend fun getBrands(page: Int, pageSize: Int):List<BrandDto>
	suspend fun getBrandProducts(brandId:Long, filterParams: ProductFilterParams):List<ProductDto>
	suspend fun getCategoryBrands(categoryId: Long): Result<List<BrandDto>, DataError.Remote>
	suspend fun getCart(): Result<CartDto, DataError.Remote>
	suspend fun addToCart(productData: ProductData): Result<Unit, DataError.Remote>
	suspend fun removeFromCart(productId: Long): Result<Unit, DataError.Remote>
	suspend fun clearCart(): Result<Unit, DataError.Remote>
	suspend fun increaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote>
	suspend fun decreaseCartItemQuantity(productId: Long): Result<Unit, DataError.Remote>
	suspend fun applyCoupon(coupon: String): Result<MessageDto, DataError>
	suspend fun clearCoupon(): Result<MessageDto, DataError.Remote>
	suspend fun getFlashSale(id: Long): Result<FlashSaleDto, DataError.Remote>
	suspend fun getFlashSaleProducts(flashSaleId:Long, filterParams: ProductFilterParams):List<ProductDto>
	suspend fun getRecentViewedProducts(page: Int, pageSize: Int): List<ProductDto>
	suspend fun clearRecentViewedProducts(): Result<Unit, DataError.Remote>
	suspend fun getProductDetail(id: Long): Result<ProductDetailDto, DataError.Remote>
	suspend fun setProductViewed(id: Long): Result<Unit, DataError.Remote>
	suspend fun getProductReviewStat(productId: Long): Result<ReviewStatDto, DataError.Remote>
	suspend fun getReviews(productId: Long, page: Int, pageSize: Int): List<ReviewDto>
	suspend fun getCheckout(): Result<CheckoutDto, DataError.Remote>
	suspend fun placeOrder(): Result<MessageDto, DataError.Remote>
	suspend fun createPaypalPayment(): Result<PaymentUrlDto, DataError.Remote>
	suspend fun paypalPaymentSuccess(token: String, payerId: String): Result<MessageDto, DataError>
	suspend fun paypalPaymentCancel(): Result<MessageDto, DataError.Remote>
	suspend fun getAddresses(): Result<List<AddressDto>, DataError.Remote>
	suspend fun getAddress(id: Long): Result<AddressDto?, DataError.Remote>
	suspend fun getCountries(): Result<List<String>, DataError.Remote>
	suspend fun addAddress(address: Address): Result<MessageDto, DataError>
	suspend fun updateAddress(address: Address): Result<MessageDto, DataError>
	suspend fun setDefaultAddress(id: Long): Result<MessageDto, DataError.Remote>
	suspend fun deleteAddress(id: Long): Result<MessageDto, DataError.Remote>
	suspend fun getOrders(page: Int, pageSize: Int): List<OrderDto>
	suspend fun getOrderDetail(id: Long): Result<OrderDetailDto?, DataError.Remote>
	suspend fun addReview(orderItemId: Long, review:String, rating: Int): Result<MessageDto, DataError>
	suspend fun downloadItem(id: Long): Response<ResponseBody>
	suspend fun downloadInvoice(orderId: Long): Response<ResponseBody>
	suspend fun getFaqs(page: Int, pageSize: Int): List<FaqDto>
	suspend fun getCoupons(page: Int,pageSize: Int): List<CouponDto>
	suspend fun getContact(): Result<List<ContactDto>, DataError.Remote>
	suspend fun getPage(page: PageType): Result<PageDto, DataError.Remote>
}