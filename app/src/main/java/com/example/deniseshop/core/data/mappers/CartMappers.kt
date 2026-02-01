package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.CartDto
import com.example.deniseshop.core.data.dto.CartProductDto
import com.example.deniseshop.core.data.dto.CheckoutDto
import com.example.deniseshop.core.domain.model.Cart
import com.example.deniseshop.core.domain.model.CartProduct
import com.example.deniseshop.core.domain.model.Checkout

fun CartProductDto.toCartProduct() = CartProduct(
	id = id,
	productId = productId,
	userId = userId,
	name = name,
	image = image,
	price = price,
	activePrice = activePrice,
	percentageDiscount = percentageDiscount,
	quantity = quantity,
	totalPrice = totalPrice,
	color = color,
	size = size
)

fun CartDto.toCart() = Cart(
	cartItems = cartItems.map { it.toCartProduct() },
	totalPrice = totalPrice,
	couponDiscount = couponDiscount,
	couponType = couponType,
	coupon = coupon
)

fun CheckoutDto.toCheckout() = Checkout(
	items = items.map { it.toCartProduct() },
	address = address?.toAddress(),
	payment = payment.map { it.toPaymentMethod() },
	containPhysicalItem = containPhysicalItem,
	subTotal = subTotal,
	couponDiscount = couponDiscount,
	shippingFee = shippingFee,
	tax = tax,
	totalAmount = totalAmount
)