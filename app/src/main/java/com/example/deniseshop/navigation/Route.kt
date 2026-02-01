package com.example.deniseshop.navigation

import androidx.navigation3.runtime.NavKey
import com.example.deniseshop.core.domain.model.PageType
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val requireLogin: Boolean = false): NavKey {

	@Serializable
	data object Home: Route(), NavKey

	@Serializable
	data object Categories: Route(), NavKey

	@Serializable
	data object Cart: Route(requireLogin = true), NavKey

	@Serializable
	data class Checkout(
		val status: String = "",
		val token: String = "",
		val payerId:String = ""
	): Route(requireLogin = true), NavKey

	@Serializable
	data object Wishlists: Route(requireLogin = true), NavKey

	@Serializable
	data object Search: Route(), NavKey

	@Serializable
	data class ProductDetail(val id: Long): Route(), NavKey

	@Serializable
	data class Reviews(val productId: Long): Route(), NavKey

	@Serializable
	data class Products(val title: String = "Products"): Route(), NavKey

	@Serializable
	data object Orders: Route(requireLogin = true), NavKey

	@Serializable
	data class OrderDetail(val id: Long): Route(requireLogin = true), NavKey

	@Serializable
	data object Profile: Route(), NavKey

	@Serializable
	data object ChangeTheme: Route(), NavKey

	@Serializable
	data object ChangePassword: Route(requireLogin = true), NavKey

	@Serializable
	data object EditProfile: Route(requireLogin = true), NavKey

	@Serializable
	data object Addresses: Route(requireLogin = true), NavKey

	@Serializable
	data class AddEditAddress(val id: Long = 0): Route(), NavKey

	@Serializable
	data object Coupons: Route(), NavKey

	@Serializable
	data object RecentViewed: Route(requireLogin = true), NavKey

	@Serializable
	data class Page(val pageType: PageType): Route(), NavKey

	@Serializable
	data object Faqs: Route(), NavKey

	@Serializable
	data object Contact: Route(), NavKey

	@Serializable
	data class FlashSale(val id: Long): Route(), NavKey

	@Serializable
	data object SignIn: Route(), NavKey

	@Serializable
	data object SignUp: Route(), NavKey

	@Serializable
	data object ForgotPassword: Route(), NavKey

	@Serializable
	data object Brands: Route(), NavKey

	@Serializable
	data class CategoryProducts(val categoryId: Long): Route(), NavKey

	@Serializable
	data class BrandProducts(val brandId: Long, val categoryId: Long): Route(), NavKey

}