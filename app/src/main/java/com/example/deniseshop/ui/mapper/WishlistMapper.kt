package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiWishlist
import com.example.deniseshop.ui.models.UiWishlist
import javax.inject.Inject

class WishlistApiToUiMapper @Inject constructor(): BaseMapper<ApiWishlist, UiWishlist> {
	override fun map(input: ApiWishlist): UiWishlist {
		return UiWishlist(
			id = input.id,
			productId = input.productId,
			image = input.image,
			name = input.name,
			price = input.price,
			activePrice = input.activePrice,
			percentageDiscount = input.percentageDiscount,
			quantity = input.quantity
		)
	}
}

class WishlistListApiToUiMapper @Inject constructor(): BaseListMapper<ApiWishlist, UiWishlist> {
	override fun map(input: List<ApiWishlist>): List<UiWishlist> {
		return input.map {
			UiWishlist(
				id = it.id,
				productId = it.productId,
				image = it.image,
				name = it.name,
				price = it.price,
				activePrice = it.activePrice,
				percentageDiscount = it.percentageDiscount,
				quantity = it.quantity
			)
		}
	}
}