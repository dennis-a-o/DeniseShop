package com.example.deniseshop.di.usecase

import com.example.deniseshop.domain.usercase.GetLoggedInStateUseCase
import com.example.deniseshop.domain.usercase.GetLoggedInStateUseCaseImpl
import com.example.deniseshop.domain.usercase.GetProductFilterUseCase
import com.example.deniseshop.domain.usercase.GetProductFilterUseCaseImpl
import com.example.deniseshop.domain.usercase.ValidateInputUseCase
import com.example.deniseshop.domain.usercase.ValidateInputUseCaseImpl
import com.example.deniseshop.domain.usercase.address.AddAddressUseCase
import com.example.deniseshop.domain.usercase.address.AddAddressUseCaseImpl
import com.example.deniseshop.domain.usercase.address.GetAllAddressUseCase
import com.example.deniseshop.domain.usercase.address.GetAllAddressUseCaseImpl
import com.example.deniseshop.domain.usercase.address.GetCountriesUseCase
import com.example.deniseshop.domain.usercase.address.GetCountriesUseCaseImpl
import com.example.deniseshop.domain.usercase.address.RemoveAddressUseCaseImpl
import com.example.deniseshop.domain.usercase.address.RemoveAddressUseCase
import com.example.deniseshop.domain.usercase.address.SetAddressDefaultUseCase
import com.example.deniseshop.domain.usercase.address.SetAddressDefaultUseCaseImpl
import com.example.deniseshop.domain.usercase.address.UpdateAddressUseCase
import com.example.deniseshop.domain.usercase.address.UpdateAddressUseCaseImpl
import com.example.deniseshop.domain.usercase.auth.ForgotPasswordUseCase
import com.example.deniseshop.domain.usercase.auth.ForgotPasswordUseCaseImpl
import com.example.deniseshop.domain.usercase.auth.SignInUseCase
import com.example.deniseshop.domain.usercase.auth.SignInUseCaseImpl
import com.example.deniseshop.domain.usercase.auth.SignUpUseCase
import com.example.deniseshop.domain.usercase.auth.SignUpUseCaseImpl
import com.example.deniseshop.domain.usercase.brand.GetBrandProductUseCase
import com.example.deniseshop.domain.usercase.brand.GetBrandProductUseCaseImpl
import com.example.deniseshop.domain.usercase.brand.GetBrandUseCase
import com.example.deniseshop.domain.usercase.brand.GetBrandUseCaseImpl
import com.example.deniseshop.domain.usercase.brand.GetCategoryBrandsUseCase
import com.example.deniseshop.domain.usercase.brand.GetCategoryBrandsUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.AddCartUseCase
import com.example.deniseshop.domain.usercase.cart.AddCartUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.ApplyCouponUseCase
import com.example.deniseshop.domain.usercase.cart.ApplyCouponUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.ClearCartUseCase
import com.example.deniseshop.domain.usercase.cart.ClearCartUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.ClearCouponUseCase
import com.example.deniseshop.domain.usercase.cart.ClearCouponUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.DecreaseQuantityUseCase
import com.example.deniseshop.domain.usercase.cart.DecreaseQuantityUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.GetCartUseCase
import com.example.deniseshop.domain.usercase.cart.GetCartUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.IncreaseQuantityUseCase
import com.example.deniseshop.domain.usercase.cart.IncreaseQuantityUseCaseImpl
import com.example.deniseshop.domain.usercase.cart.RemoveCartUseCase
import com.example.deniseshop.domain.usercase.cart.RemoveCartUseCaseImpl
import com.example.deniseshop.domain.usercase.category.GetCategoryProductsUseCase
import com.example.deniseshop.domain.usercase.category.GetCategoryProductsUseCaseImpl
import com.example.deniseshop.domain.usercase.category.GetCategoryUseCase
import com.example.deniseshop.domain.usercase.category.GetCategoryUseCaseImpl
import com.example.deniseshop.domain.usercase.checkout.CheckoutUseCase
import com.example.deniseshop.domain.usercase.checkout.CheckoutUseCaseImpl
import com.example.deniseshop.domain.usercase.checkout.CreatePaypalPaymentUseCase
import com.example.deniseshop.domain.usercase.checkout.CreatePaypalPaymentUseCaseImpl
import com.example.deniseshop.domain.usercase.checkout.PaypalPaymentCancelUseCase
import com.example.deniseshop.domain.usercase.checkout.PaypalPaymentCancelUseCaseImpl
import com.example.deniseshop.domain.usercase.checkout.PaypalPaymentSuccessUseCase
import com.example.deniseshop.domain.usercase.checkout.PaypalPaymentSuccessUseCaseImpl
import com.example.deniseshop.domain.usercase.checkout.PlaceOrderUseCase
import com.example.deniseshop.domain.usercase.checkout.PlaceOrderUseCaseImpl
import com.example.deniseshop.domain.usercase.contact.GetContactUseCase
import com.example.deniseshop.domain.usercase.contact.GetContactUseCaseImpl
import com.example.deniseshop.domain.usercase.coupon.GetCouponUseCase
import com.example.deniseshop.domain.usercase.coupon.GetCouponUseCaseImpl
import com.example.deniseshop.domain.usercase.faq.GetFaqUseCase
import com.example.deniseshop.domain.usercase.faq.GetFaqUseCaseImpl
import com.example.deniseshop.domain.usercase.flashsale.GetFlashSaleProductUseCase
import com.example.deniseshop.domain.usercase.flashsale.GetFlashSaleProductUseCaseImpl
import com.example.deniseshop.domain.usercase.flashsale.GetFlashSaleUseCase
import com.example.deniseshop.domain.usercase.flashsale.GetFlashSaleUseCaseImpl
import com.example.deniseshop.domain.usercase.home.GetHomeUseCase
import com.example.deniseshop.domain.usercase.home.GetHomeUseCaseImpl
import com.example.deniseshop.domain.usercase.order.AddReviewUseCase
import com.example.deniseshop.domain.usercase.order.AddReviewUseCaseImpl
import com.example.deniseshop.domain.usercase.order.DownloadInvoiceUseCase
import com.example.deniseshop.domain.usercase.order.DownloadInvoiceUseCaseImpl
import com.example.deniseshop.domain.usercase.order.DownloadOrderItemUseCase
import com.example.deniseshop.domain.usercase.order.DownloadOrderItemUseCaseImpl
import com.example.deniseshop.domain.usercase.order.GetOrderDetailUseCase
import com.example.deniseshop.domain.usercase.order.GetOrderDetailUseCaseImpl
import com.example.deniseshop.domain.usercase.order.GetOrdersUseCase
import com.example.deniseshop.domain.usercase.order.GetOrdersUseCaseImpl
import com.example.deniseshop.domain.usercase.page.GetPageUseCase
import com.example.deniseshop.domain.usercase.page.GetPageUseCaseImpl
import com.example.deniseshop.domain.usercase.product.GetProductDetailUseCase
import com.example.deniseshop.domain.usercase.product.GetProductDetailUseCaseImpl
import com.example.deniseshop.domain.usercase.product.GetProductUseCase
import com.example.deniseshop.domain.usercase.product.GetProductUseCaseImpl
import com.example.deniseshop.domain.usercase.product.SetViewedUseCase
import com.example.deniseshop.domain.usercase.product.SetViewedUseCaseImpl
import com.example.deniseshop.domain.usercase.profile.ChangePasswordUseCase
import com.example.deniseshop.domain.usercase.profile.ChangePasswordUseCaseImpl
import com.example.deniseshop.domain.usercase.profile.DeleteAccountUseCase
import com.example.deniseshop.domain.usercase.profile.DeleteAccountUseCaseImpl
import com.example.deniseshop.domain.usercase.profile.GetApiUserUseCase
import com.example.deniseshop.domain.usercase.profile.GetApiUserUseCaseImpl
import com.example.deniseshop.domain.usercase.profile.GetPrefUserUseCase
import com.example.deniseshop.domain.usercase.profile.GetPrefUserUseCaseImpl
import com.example.deniseshop.domain.usercase.profile.LogoutUseCase
import com.example.deniseshop.domain.usercase.profile.LogoutUseCaseImpl
import com.example.deniseshop.domain.usercase.profile.UpdateUserUseCase
import com.example.deniseshop.domain.usercase.profile.UpdateUserUseCaseImpl
import com.example.deniseshop.domain.usercase.profile.UpdateThemeUseCase
import com.example.deniseshop.domain.usercase.profile.UpdateThemeUseCaseImpl
import com.example.deniseshop.domain.usercase.recentViewed.ClearRecentViewedProductUseCase
import com.example.deniseshop.domain.usercase.recentViewed.ClearRecentViewedProductUseCaseImpl
import com.example.deniseshop.domain.usercase.recentViewed.GetRecentViewedProductUseCase
import com.example.deniseshop.domain.usercase.recentViewed.GetRecentViewedProductUseCaseImpl
import com.example.deniseshop.domain.usercase.review.GetReviewStatUseCase
import com.example.deniseshop.domain.usercase.review.GetReviewStatUseCaseImpl
import com.example.deniseshop.domain.usercase.review.GetReviewUseCase
import com.example.deniseshop.domain.usercase.review.GetReviewUseCaseImpl
import com.example.deniseshop.domain.usercase.search.DeleteSearchHistoryUseCase
import com.example.deniseshop.domain.usercase.search.DeleteSearchHistoryUseCaseImpl
import com.example.deniseshop.domain.usercase.search.GetSearchHistoryUseCase
import com.example.deniseshop.domain.usercase.search.GetSearchHistoryUseCaseImpl
import com.example.deniseshop.domain.usercase.search.SearchProductUseCase
import com.example.deniseshop.domain.usercase.search.SearchProductUseCaseImpl
import com.example.deniseshop.domain.usercase.wishlist.AddWishlistUseCase
import com.example.deniseshop.domain.usercase.wishlist.AddWishlistUseCaseImpl
import com.example.deniseshop.domain.usercase.wishlist.GetWishlistProductUseCase
import com.example.deniseshop.domain.usercase.wishlist.GetWishlistProductUseCaseImpl
import com.example.deniseshop.domain.usercase.wishlist.GetWishlistUseCase
import com.example.deniseshop.domain.usercase.wishlist.GetWishlistUseCaseImpl
import com.example.deniseshop.domain.usercase.wishlist.RemoveWishlistUseCase
import com.example.deniseshop.domain.usercase.wishlist.RemoveWishlistUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
	@Binds
	@ViewModelScoped
	abstract fun bindValidateInputUseCase(
		validateInputUseCaseImpl: ValidateInputUseCaseImpl,
	): ValidateInputUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindSignUpUseCase(
		signUpUseCaseImpl: SignUpUseCaseImpl
	): SignUpUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindSignInUseCase(
		signInUseCaseImpl: SignInUseCaseImpl
	): SignInUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindForgotPasswordUseCase(
		forgotPasswordUseCaseImpl: ForgotPasswordUseCaseImpl
	): ForgotPasswordUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetAPiUserUseCase(
		getApiUserUseCaseImpl: GetApiUserUseCaseImpl
	): GetApiUserUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindPrefUserUseCase(
		getPrefUserUseCaseImpl: GetPrefUserUseCaseImpl
	): GetPrefUserUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindSettingUseCase(
		settingUseCaseImpl: UpdateThemeUseCaseImpl
	): UpdateThemeUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindUpdateProfileUseCase(
		updateProfileUseCase: UpdateUserUseCaseImpl
	): UpdateUserUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindChangePasswordUseCase(
		changePasswordUseCaseImpl: ChangePasswordUseCaseImpl
	): ChangePasswordUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindLogoutUseCase(
		logoutseCaseImpl: LogoutUseCaseImpl
	): LogoutUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindDeleteAccountUseCase(
		deleteAccountUseCaseImpl: DeleteAccountUseCaseImpl
	): DeleteAccountUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetHomeUseCase(
		getHomeUseCaseImpl: GetHomeUseCaseImpl
	): GetHomeUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetAllCategoryUseCase(
		getAllCategoryUseCaseImpl: GetCategoryUseCaseImpl
	): GetCategoryUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetProductFilterUseCase(
		getProductFilterUseCaseImpl: GetProductFilterUseCaseImpl
	): GetProductFilterUseCase

	@Binds
	@ViewModelScoped
	abstract  fun  bindGetCategoryProductsUseCase(
		getCategoryProductsUseCaseImpl: GetCategoryProductsUseCaseImpl
	): GetCategoryProductsUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetBrandProductUseCase(
		getBrandProductUseCaseImpl: GetBrandProductUseCaseImpl
	):GetBrandProductUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetCategoryBrandsUseCase(
		getCategoryBrandsUseCaseImpl: GetCategoryBrandsUseCaseImpl
	): GetCategoryBrandsUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetBrandUseCase(
		getBrandUseCaseImpl: GetBrandUseCaseImpl
	): GetBrandUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindSearchProductUseCase(
		searchProductUseCaseImpl: SearchProductUseCaseImpl
	): SearchProductUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetSearchHistoryctUseCase(
		searchGetSearchHistoryUseCaseImpl: GetSearchHistoryUseCaseImpl
	): GetSearchHistoryUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindDeleteSearchHistoryUseCase(
		deleteSearchHistoryUseCaseImpl: DeleteSearchHistoryUseCaseImpl
	): DeleteSearchHistoryUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetProductUseCase(getProductUseCaseImpl: GetProductUseCaseImpl): GetProductUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetFlashSaleUseCase(getFlashSaleUseCaseImpl: GetFlashSaleUseCaseImpl): GetFlashSaleUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetFlashSaleProductUseCase(
		getFlashSaleProductUseCaseImpl: GetFlashSaleProductUseCaseImpl
	): GetFlashSaleProductUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetRecentViewedProductUseCase(
		getRecentViewedProductUseCaseImpl: GetRecentViewedProductUseCaseImpl
	): GetRecentViewedProductUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindClearRecentViewedUseCase(
		clearRecentViewedProductUseCaseImpl: ClearRecentViewedProductUseCaseImpl
	): ClearRecentViewedProductUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetProductDetailUseCase(getProductDetailUseCaseImpl: GetProductDetailUseCaseImpl): GetProductDetailUseCase

	@Binds
	@ViewModelScoped
	abstract fun  bindGetReviewStatUseCase(getReviewStatUseCaseImpl: GetReviewStatUseCaseImpl): GetReviewStatUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetReviewUseCase(getReviewUseCaseImpl: GetReviewUseCaseImpl): GetReviewUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetWishlistUseCase(getWishlistUseCaseImpl: GetWishlistUseCaseImpl): GetWishlistUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindAddWishlistUseCase(addWishlistUseCaseImpl: AddWishlistUseCaseImpl): AddWishlistUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindremoveWishlistUseCase(removeWishlistUseCaseImpl: RemoveWishlistUseCaseImpl): RemoveWishlistUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetLoggedInStateUseCase(getLoggedInStateUseCaseImpl: GetLoggedInStateUseCaseImpl): GetLoggedInStateUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetWishlistProductUseCase(getWishlistCountUseCaseImpl: GetWishlistProductUseCaseImpl): GetWishlistProductUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindAddCartUseCase(addCartUseCaseImpl: AddCartUseCaseImpl): AddCartUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindApplyCouponUseCase( applyCouponUseCaseImpl: ApplyCouponUseCaseImpl): ApplyCouponUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindClearCartUseCase(clearCartUseCaseImpl: ClearCartUseCaseImpl): ClearCartUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindClearCouponUseCase(clearCouponUseCaseImpl: ClearCouponUseCaseImpl): ClearCouponUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindDecreaseQuantityUseCase(decreaseQuantityUseCaseImpl: DecreaseQuantityUseCaseImpl): DecreaseQuantityUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetCartUseCase(getCartUseCaseImpl: GetCartUseCaseImpl): GetCartUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindIncreaseQuantityUseCase(increaseQuantityUseCaseImpl: IncreaseQuantityUseCaseImpl): IncreaseQuantityUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindRemoveCartUseCase(removeCartUseCaseImpl: RemoveCartUseCaseImpl): RemoveCartUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindAddAddressUseCase(addAddressUseCaseImpl: AddAddressUseCaseImpl): AddAddressUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetAllAddressUseCase(getAllAddressUseCaseImpl: GetAllAddressUseCaseImpl): GetAllAddressUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetCountriesUseCase(getCountriesUseCaseImpl: GetCountriesUseCaseImpl): GetCountriesUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindRemoveAddresUseCase(removeAddressUseCaseImpl: RemoveAddressUseCaseImpl): RemoveAddressUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindSetAddressDefaultUseCase(setAddressDefaultUseCaseImpl: SetAddressDefaultUseCaseImpl): SetAddressDefaultUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindUpdateAddressUseCase(updateAddressUseCaseImpl: UpdateAddressUseCaseImpl): UpdateAddressUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindCheckoutUseCase(checkoutUseCaseImpl: CheckoutUseCaseImpl): CheckoutUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindPlaceOrderUseCase(placeOrderUseCaseImpl: PlaceOrderUseCaseImpl): PlaceOrderUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindCreatePaypalPaymentUseCase(createPaypalPaymentUseCaseImpl: CreatePaypalPaymentUseCaseImpl): CreatePaypalPaymentUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindPaypalPaymentSuccessUseCase(paypalPaymentSuccessUseCaseImpl: PaypalPaymentSuccessUseCaseImpl): PaypalPaymentSuccessUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindPaypalPaymentCancelUseCase(paypalPaymentCancelUseCaseImpl: PaypalPaymentCancelUseCaseImpl): PaypalPaymentCancelUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetOrdersUseCase(getOrdersUseCaseImpl: GetOrdersUseCaseImpl): GetOrdersUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetOrderDetailUseCase(getOrderDetailUseCaseImpl: GetOrderDetailUseCaseImpl): GetOrderDetailUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindAddReviewUseCase(addReviewUseCaseImpl: AddReviewUseCaseImpl): AddReviewUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindDownloadInvoiceUseCase(downloadInvoiceUseCaseImpl: DownloadInvoiceUseCaseImpl): DownloadInvoiceUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindDownloadOrderItemUseCase(downloadOrderItemUseCaseImpl: DownloadOrderItemUseCaseImpl): DownloadOrderItemUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetCouponUseCase(GetCouponUseCaseImpl: GetCouponUseCaseImpl): GetCouponUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetFaqUseCase(GetFaqUseCaseImpl: GetFaqUseCaseImpl): GetFaqUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetContactUseCase(getContactUseCaseImpl: GetContactUseCaseImpl): GetContactUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindGetPageUseCase(getPageUseCaseImpl: GetPageUseCaseImpl): GetPageUseCase

	@Binds
	@ViewModelScoped
	abstract fun bindSetViewedUseCase(setViewedUseCaseImpl: SetViewedUseCaseImpl): SetViewedUseCase
}