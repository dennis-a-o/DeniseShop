package com.example.deniseshop.di.mappers

import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCart
import com.example.deniseshop.data.models.ApiCartProduct
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.models.ApiCheckout
import com.example.deniseshop.data.models.ApiContact
import com.example.deniseshop.data.models.ApiCoupon
import com.example.deniseshop.data.models.ApiFaq
import com.example.deniseshop.data.models.ApiFeaturedFlashSale
import com.example.deniseshop.data.models.ApiFlashSale
import com.example.deniseshop.data.models.ApiHome
import com.example.deniseshop.data.models.ApiOrder
import com.example.deniseshop.data.models.ApiOrderAddress
import com.example.deniseshop.data.models.ApiOrderDetail
import com.example.deniseshop.data.models.ApiOrderProduct
import com.example.deniseshop.data.models.ApiPage
import com.example.deniseshop.data.models.ApiPaymentMethod
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.data.models.ApiReview
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.data.models.ApiSlider
import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.data.models.ApiWishlist
import com.example.deniseshop.domain.models.PrefUser
import com.example.deniseshop.ui.mapper.AddressApiToUiMapper
import com.example.deniseshop.ui.mapper.AddressListApiToUiMapper
import com.example.deniseshop.ui.mapper.BaseListMapper
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.mapper.BrandApiToUiMapper
import com.example.deniseshop.ui.mapper.BrandListApiToUiMapper
import com.example.deniseshop.ui.mapper.CartApiToUiMapper
import com.example.deniseshop.ui.mapper.CartProductListApiToUiMapper
import com.example.deniseshop.ui.mapper.CategoryApiToUiMapper
import com.example.deniseshop.ui.mapper.CategoryListApiToUiMapper
import com.example.deniseshop.ui.mapper.CheckoutApiToUiMapper
import com.example.deniseshop.ui.mapper.ContactListApiToUiMapper
import com.example.deniseshop.ui.mapper.CouponListApiToUiMapper
import com.example.deniseshop.ui.mapper.FaqListApiToUiMapper
import com.example.deniseshop.ui.mapper.FeaturedFlashSaleApiToUiMapper
import com.example.deniseshop.ui.mapper.FlashSaleApiToUiMapper
import com.example.deniseshop.ui.mapper.HomeApiToUiMapper
import com.example.deniseshop.ui.mapper.OrderAddressApiToUiMapper
import com.example.deniseshop.ui.mapper.OrderDetailApiToUiMapper
import com.example.deniseshop.ui.mapper.OrderListApiToUiMapper
import com.example.deniseshop.ui.mapper.OrderProductListApiToUiMapper
import com.example.deniseshop.ui.mapper.PageApiToUiMapper
import com.example.deniseshop.ui.mapper.PaymentMethodListAPiToUiMapper
import com.example.deniseshop.ui.mapper.ProductApiToUiMapper
import com.example.deniseshop.ui.mapper.ProductDetailAPiToUiMapper
import com.example.deniseshop.ui.mapper.ProductFilterApiToUiMapper
import com.example.deniseshop.ui.mapper.ProductListApiToUiMapper
import com.example.deniseshop.ui.mapper.ReviewApiToUiMapper
import com.example.deniseshop.ui.mapper.ReviewListApiToUiMapper
import com.example.deniseshop.ui.mapper.ReviewStatAPiToUiMapper
import com.example.deniseshop.ui.mapper.SliderListApiToUiMapper
import com.example.deniseshop.ui.mapper.UserApiToUiMapper
import com.example.deniseshop.ui.mapper.UserPrefToUiMapper
import com.example.deniseshop.ui.mapper.WishlistApiToUiMapper
import com.example.deniseshop.ui.mapper.WishlistListApiToUiMapper
import com.example.deniseshop.ui.models.UiAddress
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCart
import com.example.deniseshop.ui.models.UiCartProduct
import com.example.deniseshop.ui.models.UiCategory
import com.example.deniseshop.ui.models.UiCheckout
import com.example.deniseshop.ui.models.UiContact
import com.example.deniseshop.ui.models.UiCoupon
import com.example.deniseshop.ui.models.UiFaq
import com.example.deniseshop.ui.models.UiFeaturedFlashSale
import com.example.deniseshop.ui.models.UiFlashSale
import com.example.deniseshop.ui.models.UiHome
import com.example.deniseshop.ui.models.UiOrder
import com.example.deniseshop.ui.models.UiOrderAddress
import com.example.deniseshop.ui.models.UiOrderDetail
import com.example.deniseshop.ui.models.UiOrderProduct
import com.example.deniseshop.ui.models.UiPage
import com.example.deniseshop.ui.models.UiPaymentMethod
import com.example.deniseshop.ui.models.UiProduct
import com.example.deniseshop.ui.models.UiProductDetail
import com.example.deniseshop.ui.models.UiProductFilter
import com.example.deniseshop.ui.models.UiReview
import com.example.deniseshop.ui.models.UiReviewStat
import com.example.deniseshop.ui.models.UiSlider
import com.example.deniseshop.ui.models.UiUser
import com.example.deniseshop.ui.models.UiWishlist
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class MapperModule {
	@Binds
	@ViewModelScoped
	abstract fun bindUserApiToUiMapper(userApiToUiMapper: UserApiToUiMapper):BaseMapper<ApiUser, UiUser>

	@Binds
	@ViewModelScoped
	abstract fun bindUserPrefToUiMapper(userPrefToUiMapper: UserPrefToUiMapper):BaseMapper<PrefUser, UiUser>

	@Binds
	@ViewModelScoped
	abstract fun bindApiProductToUiProduct( productApiToUiMapper: ProductApiToUiMapper): BaseMapper<ApiProduct, UiProduct>

	@Binds
	@ViewModelScoped
	abstract fun bindApiProductListToUiProductList( productListApiToUiMapper: ProductListApiToUiMapper): BaseListMapper<ApiProduct, UiProduct>

	@Binds
	@ViewModelScoped
	abstract fun bindCategoryApiToUi( categoryApiToUiMapper: CategoryApiToUiMapper): BaseMapper<ApiCategory, UiCategory>

	@Binds
	@ViewModelScoped
	abstract fun bindCategoryListApiToUi( categoryListApiToUiMapper: CategoryListApiToUiMapper): BaseListMapper<ApiCategory, UiCategory>

	@Binds
	@ViewModelScoped
	abstract fun bindBrandApiToUi( brandApiToUiMapper: BrandApiToUiMapper): BaseMapper<ApiBrand, UiBrand>

	@Binds
	@ViewModelScoped
	abstract fun bindBrandListApiToUi( brandListApiToUiMapper: BrandListApiToUiMapper): BaseListMapper<ApiBrand, UiBrand>

	@Binds
	@ViewModelScoped
	abstract fun bindFlashSaleApiToUi( flashSaleApiToUiMapper: FlashSaleApiToUiMapper): BaseMapper<ApiFlashSale, UiFlashSale>

	@Binds
	@ViewModelScoped
	abstract fun bindSliderListApiToUi( sliderListApiToUiMapper: SliderListApiToUiMapper): BaseListMapper<ApiSlider, UiSlider>

	@Binds
	@ViewModelScoped
	abstract fun bindHomeApiToUi( homeApiToUiMapper: HomeApiToUiMapper): BaseMapper<ApiHome, UiHome>

	@Binds
	@ViewModelScoped
	abstract fun bindFeaturedFlashSaleApiToUi( featuredFlashSaleApiToUiMapper: FeaturedFlashSaleApiToUiMapper): BaseMapper<ApiFeaturedFlashSale, UiFeaturedFlashSale>

	@Binds
	@ViewModelScoped
	abstract fun bindProductFilterApiToUi(productFilterApiToUiMapper: ProductFilterApiToUiMapper): BaseMapper<ApiProductFilter, UiProductFilter>

	@Binds
	@ViewModelScoped
	abstract fun  bindReviewApiToUiMapper( reviewApiToUiMapper: ReviewApiToUiMapper): BaseMapper<ApiReview, UiReview>

	@Binds
	@ViewModelScoped
	abstract fun bindReviewListApiToUiMapper(reviewListApiToUiMapper: ReviewListApiToUiMapper):BaseListMapper<ApiReview, UiReview>

	@Binds
	@ViewModelScoped
	abstract fun bindReviewStatAPiToUiMapper(reviewStatAPiToUiMapper: ReviewStatAPiToUiMapper):BaseMapper<ApiReviewStat, UiReviewStat>

	@Binds
	@ViewModelScoped
	abstract fun bindProductDetailAPiToUiMapper(productDetailAPiToUiMapper: ProductDetailAPiToUiMapper):BaseMapper<ApiProductDetail, UiProductDetail>

	@Binds
	@ViewModelScoped
	abstract  fun  bindWishlistApiToUiMapper(wishlistApiToUiMapper: WishlistApiToUiMapper):BaseMapper<ApiWishlist, UiWishlist>

	@Binds
	@ViewModelScoped
	abstract  fun  bindWishlistListApiToUiMapper(wishlistListApiToUiMapper: WishlistListApiToUiMapper):BaseListMapper<ApiWishlist, UiWishlist>

	@Binds
	@ViewModelScoped
	abstract  fun bindCartProductListApiToUiMapper(cartProductListApiToUiMapper: CartProductListApiToUiMapper):BaseListMapper<ApiCartProduct, UiCartProduct>

	@Binds
	@ViewModelScoped
	abstract  fun bindCartApiToUiMapper(cartApiToUiMapper: CartApiToUiMapper):BaseMapper<ApiCart, UiCart>

	@Binds
	@ViewModelScoped
	abstract  fun bindAddressApiToUiMapper(addressApiToUiMapper: AddressApiToUiMapper):BaseMapper<ApiAddress, UiAddress>

	@Binds
	@ViewModelScoped
	abstract  fun bindAddressListApiToUiMapper(addressListApiToUiMapper: AddressListApiToUiMapper):BaseListMapper<ApiAddress, UiAddress>

	@Binds
	@ViewModelScoped
	abstract  fun bindCheckoutApiToUiMapper(checkoutApiToUiMapper: CheckoutApiToUiMapper):BaseMapper<ApiCheckout, UiCheckout>

	@Binds
	@ViewModelScoped
	abstract fun bindPaymenMethodListAPiToUiMapper(paymentMethodListAPiToUiMapper: PaymentMethodListAPiToUiMapper):BaseListMapper<ApiPaymentMethod, UiPaymentMethod>

	@Binds
	@ViewModelScoped
	abstract fun bindOrderListApiToUiMapper(orderListApiToUiMapper: OrderListApiToUiMapper):BaseListMapper<ApiOrder, UiOrder>

	@Binds
	@ViewModelScoped
	abstract fun bindOrderProductListApiToUiMapper(orderProductListApiToUiMapper: OrderProductListApiToUiMapper): BaseListMapper<ApiOrderProduct, UiOrderProduct>

	@Binds
	@ViewModelScoped
	abstract fun bindOrderAddressApiToUiMapper(orderAddressApiToUiMapper: OrderAddressApiToUiMapper): BaseMapper<ApiOrderAddress, UiOrderAddress>

	@Binds
	@ViewModelScoped
	abstract fun bindOrderDetailApiToUiMapper(orderDetailApiToUiMapper: OrderDetailApiToUiMapper): BaseMapper<ApiOrderDetail, UiOrderDetail>

	@Binds
	@ViewModelScoped
	abstract fun bindCouponListApiToUiMapper(couponListApiToUiMapper: CouponListApiToUiMapper): BaseListMapper<ApiCoupon, UiCoupon>

	@Binds
	@ViewModelScoped
	abstract fun bindFaqListApiToUiMapper(faqListApiToUiMapper: FaqListApiToUiMapper): BaseListMapper<ApiFaq, UiFaq>

	@Binds
	@ViewModelScoped
	abstract fun bindPageApiToUiMapper( pageApiToUiMapper: PageApiToUiMapper): BaseMapper<ApiPage, UiPage>

	@Binds
	@ViewModelScoped
	abstract fun bindContactListApiToUiMapper(contactListApiToUiMapper: ContactListApiToUiMapper):BaseListMapper<ApiContact, UiContact>
}