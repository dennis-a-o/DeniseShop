package com.example.deniseshop.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deniseshop.R
import com.example.deniseshop.feature.changepassword.presentation.ChangePasswordBottomSheet
import com.example.deniseshop.feature.changetheme.ChangeThemeBottomSheet
import com.example.deniseshop.feature.editprofile.presentation.EditProfileBottomSheet
import com.example.deniseshop.feature.forgotpassword.presentation.ForgotPasswordScreen
import com.example.deniseshop.feature.profile.presentation.ProfileScreen
import com.example.deniseshop.feature.signin.presentation.SignInScreen
import com.example.deniseshop.feature.signup.presentation.SignUpScreen
import com.example.deniseshop.ui.models.UiAddress
import com.example.deniseshop.ui.screens.address.AddressFormScreen
import com.example.deniseshop.ui.screens.address.AddressScreen
import com.example.deniseshop.ui.screens.address.viewModels.AddressFormViewModel
import com.example.deniseshop.ui.screens.brand.BrandProductScreen
import com.example.deniseshop.ui.screens.brand.BrandScreen
import com.example.deniseshop.ui.screens.brand.viewModels.BrandProductViewModel
import com.example.deniseshop.ui.screens.cart.CartScreen
import com.example.deniseshop.ui.screens.cart.CartViewModel
import com.example.deniseshop.ui.screens.category.CategoryProductScreen
import com.example.deniseshop.ui.screens.category.CategoryScreen
import com.example.deniseshop.ui.screens.category.viewModels.CategoryProductViewModel
import com.example.deniseshop.ui.screens.checkout.CheckoutScreen
import com.example.deniseshop.ui.screens.contact.ContactScreen
import com.example.deniseshop.ui.screens.coupon.CouponScreen
import com.example.deniseshop.ui.screens.faqs.FaqsScreen
import com.example.deniseshop.ui.screens.flashsale.FlashSaleScreen
import com.example.deniseshop.ui.screens.flashsale.FlashSaleViewModel
import com.example.deniseshop.ui.screens.home.HomeScreen
import com.example.deniseshop.ui.screens.order.OrderDetailScreen
import com.example.deniseshop.ui.screens.order.OrderScreen
import com.example.deniseshop.ui.screens.order.viewModels.OrderDetailViewModel
import com.example.deniseshop.ui.screens.page.PageScreen
import com.example.deniseshop.ui.screens.page.PageViewModel
import com.example.deniseshop.ui.screens.product.ProductDetailScreen
import com.example.deniseshop.ui.screens.product.ProductScreen
import com.example.deniseshop.ui.screens.product.viewModels.ProductDetailViewModel
import com.example.deniseshop.ui.screens.rencentViewed.RecentlyViewedScreen
import com.example.deniseshop.ui.screens.review.ReviewScreen
import com.example.deniseshop.ui.screens.review.ReviewViewModel
import com.example.deniseshop.ui.screens.search.SearchScreen
import com.example.deniseshop.ui.screens.wishlist.WishlistScreen
import com.example.deniseshop.ui.screens.wishlist.WishlistViewModel


@Composable
fun NavGraph(
	viewIntentData: Uri?,
	onClearIntentData: () -> Unit
){
	val navController = rememberNavController()

	var showEditProfileBottomSheet by remember { mutableStateOf(false) }
	var showChangePasswordBottomSheet by remember { mutableStateOf(false) }
	var showChangeThemeBottomSheet by remember { mutableStateOf(false) }


	if (showEditProfileBottomSheet){
		EditProfileBottomSheet(
			onDismiss = {
				showEditProfileBottomSheet = false
			},
			onShowSnackBar = { a,b -> Boolean
				Log.d("onShowSnackBar()","$a, $b")
				true
			}
		)
	}

	if (showChangePasswordBottomSheet){
		ChangePasswordBottomSheet(
			onDismiss = {
				showChangePasswordBottomSheet = false
			},
			onShowSnackBar = { a,b -> Boolean
				Log.d("onShowSnackBar()","$a, $b")
				true
			}
		)
	}

	if (showChangeThemeBottomSheet){
		ChangeThemeBottomSheet(
			onDismiss = {
				showChangeThemeBottomSheet = false
			}
		)
	}

	val wishlistViewModel: WishlistViewModel = hiltViewModel()
	val cartViewModel: CartViewModel = hiltViewModel()

	val cartBadgeCount = cartViewModel.cartCountState.collectAsState()
	val wishlistBadgeCount = wishlistViewModel.wishlistCount.collectAsState()
	NavHost(
		navController = navController,
		startDestination = Routes.Home.route
	){
		composable(Routes.Home.route){
			HomeScreen(
				onNavigate = { route, options: NavOptions? ->
					navController.navigate(route, navOptions = options )
				},
				wishlistViewModel = wishlistViewModel,
				cartViewModel = cartViewModel
			)
		}

		composable(Routes.Category.route){
			CategoryScreen(
				onNavigate = { route, option ->
					navController.navigate(route, option)
				},
				wishlistBadgeCount.value,
				cartBadgeCount.value
			)
		}

		composable(Routes.Wishlist.route){
			WishlistScreen(
				onNavigate = { route, options ->
					navController.navigate(route,options)
				},
				viewModel = wishlistViewModel,
				cartViewModel = cartViewModel
			)
		}

		composable(Routes.Search.route){
			SearchScreen(
				onNavigate = {route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				wishlistViewModel = wishlistViewModel,
				cartViewModel = cartViewModel
			)
		}

		composable(
			route = Routes.ProductScreen.routeWithArgs,
			arguments = Routes.ProductScreen.arguments
		){ backStackEntry ->
			val title = backStackEntry.arguments?.getString("title")
			ProductScreen(
				onNavigate = {route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				title = title ?: stringResource(R.string.products),
				wishlistViewModel = wishlistViewModel,
				cartViewModel = cartViewModel
			)
		}

		composable(
			route = Routes.ProductDetail.routeWithArgs,
			arguments = Routes.ProductDetail.arguments
		) { backStackEntry ->
			val viewModel: ProductDetailViewModel = hiltViewModel(backStackEntry)
			ProductDetailScreen(
				onNavigate = {route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel,
				cartViewModel = cartViewModel,
				wishlistViewModel = wishlistViewModel
			)
		}

		composable (
			route = Routes.Reviews.routeWithArgs,
			arguments = Routes.Reviews.arguments,
		){ backStackEntry ->
			val viewModel: ReviewViewModel = hiltViewModel(backStackEntry)
			ReviewScreen(
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel
			)
		}

		composable(Routes.Orders.route) {
			OrderScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				}
			)
		}

		composable (
			route = Routes.OrderDetail.routeWithArgs,
			arguments = Routes.OrderDetail.arguments,
		) { backStackEntry ->
			val viewModel: OrderDetailViewModel = hiltViewModel(backStackEntry)
			OrderDetailScreen(
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel
			)
		}

		composable(Routes.Profile.route){
			ProfileScreen(
				onBackClick = navController::popBackStack,
				onNavigate = {
					navController.navigate(it)
				},
				onShowEditAccountBottomSheet = {
					showEditProfileBottomSheet = true
				},
				onShowChangePasswordBottomSheet = {
					showChangePasswordBottomSheet = true
				},
				onShowThemeBottomSheet = {
					showChangeThemeBottomSheet = true
				},
				onShowSnackBar = {a,b -> Boolean
					Log.d("onShowSnackBar()","$a, $b")
					true
				}
			)
		}

		composable (Routes.AllAddress.route){
			AddressScreen(
				onNavigate = {route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				}
			)
		}

		composable<UiAddress>{ backStackEntry ->
			val viewModel: AddressFormViewModel = hiltViewModel(backStackEntry)
			AddressFormScreen(
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel
			)
		}

		composable(Routes.AddressForm.route){
			val viewModel: AddressFormViewModel = hiltViewModel()
			AddressFormScreen(
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel
			)
		}

		composable(Routes.Cart.route){
			CartScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = cartViewModel,
				wishlistCount = wishlistBadgeCount.value
			)
		}

		composable("checkout"){
			CheckoutScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				onNavigateAddress =  { route, options ->
					navController.navigate(route, options)
				} ,
				viewIntentData = viewIntentData,
				onClearIntentData = { onClearIntentData() }
			)
		}

		composable(Routes.PromoCodes.route) {
			CouponScreen(
				onNavigateUp = {
					navController.navigateUp()
				},
			)
		}

		composable (Routes.RecentlyViewed.route){
			RecentlyViewedScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				}
			)
		}

		composable(Routes.BrandScreen.route) {
			BrandScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				cartBadgeCount = cartBadgeCount.value,
				wishlistBadgeCount = wishlistBadgeCount.value
			)
		}

		composable(
			route = Routes.CategoryProductScreen.routeWithArgs,
			arguments = Routes.CategoryProductScreen.arguments
		) { navBackStackEntry ->
			val viewModel: CategoryProductViewModel = hiltViewModel(navBackStackEntry)
			CategoryProductScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel,
				wishlistViewModel = wishlistViewModel,
				cartViewModel = cartViewModel
			)
		}

		composable(
			route = Routes.BrandProductScreen.routeWithArgs,
			arguments = Routes.BrandProductScreen.arguments
		) { navBackStackEntry ->
			val viewModel: BrandProductViewModel = hiltViewModel(navBackStackEntry)
			BrandProductScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel,
				wishlistViewModel = wishlistViewModel,
				cartViewModel = cartViewModel
			)
		}

		composable(
			route = Routes.PageScreen.routeWithArgs,
			arguments = Routes.PageScreen.arguments
		) { navBackStackEntry ->
			val viewModel: PageViewModel = hiltViewModel(navBackStackEntry)
			PageScreen(
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel = viewModel
			)
		}

		composable(Routes.FaqsScreen.route){
			FaqsScreen(onNavigateUp = { navController.navigateUp() })
		}

		composable(Routes.ContactScreen.route){
			ContactScreen(onNavigateUp = { navController.navigateUp() })
		}

		composable(
			route = Routes.FlashSaleScreen.routeWithArgs,
			arguments = Routes.FlashSaleScreen.arguments
		){ backStackEntry ->
			val viewModel: FlashSaleViewModel = hiltViewModel(backStackEntry)
			FlashSaleScreen(
				onNavigate = { route, options ->
					navController.navigate(route, options)
				},
				onNavigateUp = {
					navController.navigateUp()
				},
				viewModel,
				wishlistViewModel,
				cartViewModel
			)
		}

		composable(Routes.SignInScreen.route){
			SignInScreen(
				onBackClick = navController::navigateUp,
				onSingUpClick =  {
					navController.navigate(Routes.SignUpScreen.route)
				},
				onForgotPasswordClick = {
					navController.navigate(Routes.ForgotPassword.route)
				},
				onSignIn = {
					navController.popBackStack()
				},
				onShowSnackBar = {a,b -> Boolean
					Log.d("onShowSnackBar()","$a, $b")
					 true
				}
			)
		}

		composable(Routes.SignUpScreen.route){
			SignUpScreen(
				onBackClick = navController::navigateUp,
				onTermsClick = {
					navController.navigate("${Routes.PageScreen.route}/Terms Conditions")
				},
				onSignInClick = navController::navigateUp,
				onShowSnackBar = {a,b -> Boolean
					Log.d("onShowSnackBar()","$a, $b")
					true
				}
			)
		}

		composable(Routes.ForgotPassword.route){
			ForgotPasswordScreen(
				onBackClick = navController::popBackStack,
				onSignInClick = navController::navigateUp,
				onShowSnackBar = {a,b -> Boolean
					Log.d("onShowSnackBar()","$a, $b")
					true
				}
			)
		}
	}
}








