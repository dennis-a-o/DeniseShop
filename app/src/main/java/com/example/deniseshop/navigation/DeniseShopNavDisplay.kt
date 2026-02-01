package com.example.deniseshop.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.deniseshop.app.ui.DeniseShopState
import com.example.deniseshop.core.domain.model.PageType
import com.example.deniseshop.core.presentation.BottomSheetSceneStrategy
import com.example.deniseshop.feature.addeditaddress.AddEditAddressScreen
import com.example.deniseshop.feature.addeditaddress.AddEditAddressViewModel
import com.example.deniseshop.feature.addresses.AddressesScreen
import com.example.deniseshop.feature.brandproducts.BrandProductsScreen
import com.example.deniseshop.feature.brandproducts.BrandProductsViewModel
import com.example.deniseshop.feature.brands.BrandsScreen
import com.example.deniseshop.feature.cart.CartScreen
import com.example.deniseshop.feature.categories.CategoriesScreen
import com.example.deniseshop.feature.categoryproducts.CategoryProductsScreen
import com.example.deniseshop.feature.categoryproducts.CategoryProductsViewModel
import com.example.deniseshop.feature.changepassword.presentation.ChangePasswordScreen
import com.example.deniseshop.feature.changetheme.ChangeThemeScreen
import com.example.deniseshop.feature.checkout.CheckoutScreen
import com.example.deniseshop.feature.checkout.CheckoutViewModel
import com.example.deniseshop.feature.contact.ContactScreen
import com.example.deniseshop.feature.coupons.CouponsScreen
import com.example.deniseshop.feature.editprofile.presentation.EditProfileScreen
import com.example.deniseshop.feature.faqs.FaqsScreen
import com.example.deniseshop.feature.flashsaleproducts.FlashSaleProductsScreen
import com.example.deniseshop.feature.flashsaleproducts.FlashSaleProductsViewModel
import com.example.deniseshop.feature.forgotpassword.presentation.ForgotPasswordScreen
import com.example.deniseshop.feature.home.HomeScreen
import com.example.deniseshop.feature.orderdetail.OrderDetailScreen
import com.example.deniseshop.feature.orderdetail.OrderDetailViewModel
import com.example.deniseshop.feature.orders.OrdersScreen
import com.example.deniseshop.feature.page.PageScreen
import com.example.deniseshop.feature.page.PageViewModel
import com.example.deniseshop.feature.productdetail.ProductDetailScreen
import com.example.deniseshop.feature.productdetail.ProductDetailViewModel
import com.example.deniseshop.feature.products.ProductsScreen
import com.example.deniseshop.feature.profile.presentation.ProfileScreen
import com.example.deniseshop.feature.recentviewed.RecentViewedScreen
import com.example.deniseshop.feature.reviews.ReviewsScreen
import com.example.deniseshop.feature.reviews.ReviewsViewModel
import com.example.deniseshop.feature.search.SearchScreen
import com.example.deniseshop.feature.signin.presentation.SignInScreen
import com.example.deniseshop.feature.signup.presentation.SignUpScreen
import com.example.deniseshop.feature.wishlists.WishlistsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeniseNavDisplay(
	appState: DeniseShopState,
	navigator: Navigator,
	onShowSnackBar: suspend (String, String?) -> Boolean,
	modifier: Modifier = Modifier
) {

	val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

	NavDisplay(
		backStack = appState.backStack,
		modifier = modifier,
		onBack = { navigator.goBack() },
		sceneStrategy = bottomSheetStrategy,
		entryDecorators = listOf(
			rememberSaveableStateHolderNavEntryDecorator(),
			rememberViewModelStoreNavEntryDecorator()
		),
		entryProvider = entryProvider {
			entry <Route.Home>{
				HomeScreen(
					onNavigate = { route ->
						navigator.navigate(route)
					}
				)
			}
			entry <Route.Categories>{
				CategoriesScreen(
					onNavigate = { route ->
						navigator.navigate(route)
					}
				)
			}
			entry <Route.Wishlists>{
				WishlistsScreen(
					onNavigate = { route ->
						navigator.navigate(route)
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry <Route.Profile>{
				ProfileScreen(
					onNavigate = { route ->
						navigator.navigate(route)
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.ChangeTheme>(
				metadata = BottomSheetSceneStrategy.bottomSheet()
			) {
				ChangeThemeScreen()
			}
			entry<Route.ChangePassword>(
				metadata = BottomSheetSceneStrategy.bottomSheet()
			) {
				ChangePasswordScreen(
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.EditProfile>(
				metadata = BottomSheetSceneStrategy.bottomSheet()
			) {
				EditProfileScreen(
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.Search>{
				SearchScreen(
					onBackClick = {
						navigator.goBack()
					},
					onProductClick = {
						navigator.navigate(Route.ProductDetail(it))
					}
				)
			}
			entry <Route.Products>{ key ->
				ProductsScreen(
					title = key.title,
					onBackClick = {
						navigator.goBack()
					},
					onNavigate = { route ->
						navigator.navigate(route)
					}
				)
			}
			entry<Route.ProductDetail> { key ->
				val viewModel = hiltViewModel<ProductDetailViewModel, ProductDetailViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)

				ProductDetailScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					},
					onNavigate = { route ->
						if (route == Route.Home)
							navigator.navigateHome()
						navigator.navigate(route)
					}
				)
			}
			entry<Route.Reviews> { key ->
				val viewModel = hiltViewModel<ReviewsViewModel, ReviewsViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)

				ReviewsScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					}
				)
			}
			entry<Route.Orders> {
				OrdersScreen(
					onBackClick = {
						navigator.goBack()
					},
					onOrderClick = {
						navigator.navigate(Route.OrderDetail(it))
					}
				)
			}
			entry<Route.OrderDetail> { key ->
				val viewModel = hiltViewModel<OrderDetailViewModel, OrderDetailViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)

				OrderDetailScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.Addresses> {
				AddressesScreen(
					onBackClick = {
						navigator.goBack()
					},
					onAddressClick = {
						navigator.navigate(Route.AddEditAddress(it))
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.AddEditAddress> { key->
				val viewModel = hiltViewModel<AddEditAddressViewModel, AddEditAddressViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)

				AddEditAddressScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.Cart> {
				CartScreen(
					onBackClick = {
						navigator.goBack()
					},
					onNavigate = { route->
						navigator.navigate(route)
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.Checkout> { key ->
				val viewModel = hiltViewModel<CheckoutViewModel,CheckoutViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)
				CheckoutScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					},
					onCheckoutDone = {
						navigator.navigateHome()
					},
					onEditAddAddressClick = {
						navigator.navigate(Route.AddEditAddress(it))
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry <Route.Coupons>{
				CouponsScreen(
					onBackClick = {
						navigator.goBack()
					}
				)
			}
			entry<Route.RecentViewed> {
				RecentViewedScreen(
					onBackClick = {
						navigator.goBack()
					},
					onProductClick = {
						navigator.navigate(Route.ProductDetail(it))
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.Brands> {
				BrandsScreen(
					onNavigate = { route ->
						navigator.navigate(route)
					},
					onBackClick = {
						navigator.goBack()
					}
				)
			}
			entry<Route.CategoryProducts> { key ->
				val viewModel = hiltViewModel<CategoryProductsViewModel, CategoryProductsViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)

				CategoryProductsScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					},
					onNavigate = { route ->
						navigator.navigate(route)
					},
				)
			}
			entry<Route.BrandProducts> { key ->
				val viewModel = hiltViewModel<BrandProductsViewModel,BrandProductsViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)

				BrandProductsScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					},
					onNavigate = { route ->
						navigator.navigate(route)
					}
				)
			}
			entry<Route.Page> { key ->
				val viewModel = hiltViewModel<PageViewModel, PageViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)

				PageScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					}
				)
			}
			entry<Route.Faqs> {
				FaqsScreen(
					onBackClick = {
						navigator.goBack()
					}
				)
			}
			entry<Route.Contact>{
				ContactScreen(
					onBackClick = {
						navigator.goBack()
					}
				)
			}
			entry<Route.FlashSale> { key ->
				val viewModel = hiltViewModel<FlashSaleProductsViewModel, FlashSaleProductsViewModel.Factory>(
					creationCallback = { factory ->
						factory.create(key)
					}
				)
				FlashSaleProductsScreen(
					viewModel = viewModel,
					onBackClick = {
						navigator.goBack()
					},
					onNavigate = { route ->
						navigator.navigate(route)
					},
				)
			}

			entry<Route.SignIn> {
				SignInScreen(
					onBackClick = {
						navigator.goBack()
					},
					onSingUpClick =  {
						navigator.navigate(Route.SignUp)
					},
					onForgotPasswordClick = {
						navigator.navigate(Route.ForgotPassword)
					},
					onSignIn = {
						navigator.navigateHome()
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.SignUp> {
				SignUpScreen(
					onBackClick = {
						navigator.goBack()
					},
					onTermsClick = {
						navigator.navigate(Route.Page(PageType.TERMS_CONDITIONS))
					},
					onSignInClick = {
						navigator.navigate(Route.SignIn)
					},
					onShowSnackBar = onShowSnackBar
				)
			}
			entry<Route.ForgotPassword> {
				ForgotPasswordScreen(
					onBackClick = {
						navigator.goBack()
					},
					onSignInClick = {
						navigator.goBack()
					},
					onShowSnackBar = onShowSnackBar
				)
			}
		}
	)
}