package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiCart
import com.example.deniseshop.data.models.ApiCartProduct
import com.example.deniseshop.ui.models.UiCart
import com.example.deniseshop.ui.models.UiCartProduct
import javax.inject.Inject



class CartProductListApiToUiMapper @Inject constructor(): BaseListMapper<ApiCartProduct, UiCartProduct> {
	override fun map(input: List<ApiCartProduct>): List<UiCartProduct> {
		return input.map {
			UiCartProduct(
				id = it.id,
				productId = it.productId,
				userId = it.userId,
				name = it.name,
				image = it.image,
				price = it.price,
				percentageDiscount = it.percentageDiscount,
				activePrice = it.activePrice,
				quantity = it.quantity,
				totalPrice = it.totalPrice,
				color = it.color,
				size = it.size
			)
		}
	}

}


class CartApiToUiMapper @Inject constructor(
	private val cartProductListApiToUiMapper: BaseListMapper<ApiCartProduct, UiCartProduct>
): BaseMapper<ApiCart, UiCart> {
	override fun map(input: ApiCart): UiCart {
		return UiCart(
			cartItems = cartProductListApiToUiMapper.map(input.cartItems),
			totalPrice = input.totalPrice,
			couponDiscount = input.couponDiscount,
			coupon = input.coupon,
			couponType = input.couponType
		)
	}
}