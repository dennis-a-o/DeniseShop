package com.example.deniseshop.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument


sealed class Routes(val route: String){
	data object Home: Routes("home")
	data object Category: Routes("category")
	data object Cart: Routes("cart")
	data object Checkout: Routes("checkout")
	data object Wishlist: Routes("wishlist")
	data object Search: Routes("search")
	data object ProductDetail: Routes("productDetail"){
		private const val PRODUCT_ID = "productId"
		val routeWithArgs = "$route/{$PRODUCT_ID}"
		val arguments = listOf(
			navArgument(PRODUCT_ID){ type = NavType.LongType },
		)
	}
	data object  Reviews: Routes("review"){
		private const val PRODUCT_ID = "productId"
		val routeWithArgs = "$route/{$PRODUCT_ID}"
		val arguments = listOf(
			navArgument(PRODUCT_ID){ type = NavType.LongType },
		)
	}
	data object ProductScreen: Routes("products"){
		private const val TITLE = "title"
		val routeWithArgs = "$route/{$TITLE}"
		val arguments = listOf(
			navArgument(TITLE){ type = NavType.StringType },
		)
	}
	data object Orders: Routes("orders")
	data object  OrderDetail: Routes("orderDetail"){
		private const val ORDER_ID = "orderId"
		val routeWithArgs = "$route/{$ORDER_ID}"
		val arguments = listOf(
			navArgument(ORDER_ID){ type = NavType.LongType },
		)
	}
	data object Profile: Routes("profile")
	data object AllAddress: Routes("allAddress")
	data object AddEditAddress: Routes("addEditAddress"){
		private  const val ADDRESS_ID = "addressId"
		val routeWithArgs = "$route/{$ADDRESS_ID}"
		val arguments = listOf(
			navArgument(ADDRESS_ID){
				type = NavType.LongType
				defaultValue = 0L
			}
		)
	}

	data object PromoCodes: Routes("promoCodes")
	data object RecentlyViewed: Routes("recentlyViewed")
	data object PageScreen: Routes("page"){
		private  const val PAGE_NAME = "pageName"
		val routeWithArgs = "$route/{$PAGE_NAME}"
		val arguments = listOf(
			navArgument(PAGE_NAME){type = NavType.StringType }
		)
	}
	data object  FaqsScreen: Routes("faqs")
	data object ContactScreen: Routes("contact")
	data object FlashSaleScreen: Routes("flashSale"){
		private const val FLASH_SALE_ID = "flashSaleId"
		val routeWithArgs = "$route/{${FLASH_SALE_ID}}"
		val arguments = listOf(
			navArgument(FLASH_SALE_ID){type = NavType.LongType}
		)
	}
	data object SignInScreen: Routes("signIn")
	data object SignUpScreen: Routes("signUp")
	data object ForgotPassword: Routes("forgotPassword")
	data object BrandScreen: Routes("brands")
	data object CategoryProductScreen: Routes("categoryProduct"){
		private const val CATEGORY_ID = "categoryId"
		val routeWithArgs = "$route/{$CATEGORY_ID}"
		val arguments = listOf(
			navArgument(CATEGORY_ID){type = NavType.LongType}
		)
	}
	data object BrandProductScreen: Routes("brandProduct"){
		private const val BRAND_ID = "brandId"
 		private const val CATEGORY_ID = "categoryId"
		val routeWithArgs = "$route/{$BRAND_ID}/{$CATEGORY_ID}"
		val arguments = listOf(
			navArgument(BRAND_ID){type = NavType.LongType},
			navArgument(CATEGORY_ID){type = NavType.LongType}
		)
	}
}