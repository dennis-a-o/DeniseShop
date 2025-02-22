package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiAddress
import com.example.deniseshop.data.models.ApiCartProduct
import com.example.deniseshop.data.models.ApiCheckout
import com.example.deniseshop.data.models.ApiPaymentMethod
import com.example.deniseshop.ui.models.UiAddress
import com.example.deniseshop.ui.models.UiCartProduct
import com.example.deniseshop.ui.models.UiCheckout
import com.example.deniseshop.ui.models.UiPaymentMethod
import javax.inject.Inject

class CheckoutApiToUiMapper @Inject constructor(
	private val cartProductListApiToUiMapper: BaseListMapper<ApiCartProduct,UiCartProduct>,
	private val addressApiToUiMapper: BaseMapper<ApiAddress,UiAddress>,
	private val paymentMethodApiToUiMapper: BaseListMapper<ApiPaymentMethod,UiPaymentMethod>
):BaseMapper<ApiCheckout,UiCheckout> {
	override fun map(input: ApiCheckout): UiCheckout {
		return UiCheckout(
			items = cartProductListApiToUiMapper.map(input.items),
			address = input.address?.let { addressApiToUiMapper.map(it) },
			payment = paymentMethodApiToUiMapper.map(input.payment),
			containPhysicalItem = input.containPhysicalItem,
			subTotal = input.subTotal,
			couponDiscount = input.couponDiscount,
			shippingFee = input.shippingFee,
			tax = input.tax,
			totalAmount = input.totalAmount
		)
	}
}

class PaymentMethodListAPiToUiMapper @Inject constructor(): BaseListMapper<ApiPaymentMethod,UiPaymentMethod>{
	override fun map(input: List<ApiPaymentMethod>): List<UiPaymentMethod> {
		return input.map {
			UiPaymentMethod(
				id = it.id,
				name = it.name,
				slug = it.slug,
				logo = it.logo
			)
		}
	}
}