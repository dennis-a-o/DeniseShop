package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.WishlistDto
import com.example.deniseshop.core.domain.model.Wishlist

fun WishlistDto.toWishlist(): Wishlist{
	return Wishlist(
		id = id,
		productId = productId,
		image = image,
		name = name,
		price = price,
		activePrice = activePrice,
		percentageDiscount = percentageDiscount,
		quantity = quantity
	)
}