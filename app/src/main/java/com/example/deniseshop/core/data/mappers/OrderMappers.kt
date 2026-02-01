package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.OrderAddressDto
import com.example.deniseshop.core.data.dto.OrderDetailDto
import com.example.deniseshop.core.data.dto.OrderDto
import com.example.deniseshop.core.data.dto.OrderProductDto
import com.example.deniseshop.core.domain.model.Order
import com.example.deniseshop.core.domain.model.OrderAddress
import com.example.deniseshop.core.domain.model.OrderDetail
import com.example.deniseshop.core.domain.model.OrderProduct

fun OrderDto.toOrder() = Order(
	id = id,
	name = name,
	image = image,
	code = code,
	status = status,
	date = date
)

fun OrderAddressDto.toOrderAddress() = OrderAddress(
	id = id,
	name = name,
	email = email,
	phone = phone,
	address = address
)

fun OrderProductDto.toOrderProduct() = OrderProduct(
	id = id,
	productId = productId,
	name = name,
	image = image,
	price = price,
	quantity = quantity,
	size = size,
	color = color,
	rated = rated,
	downloadable = downloadable ?: false
)

fun OrderDetailDto.toOrderDetail() = OrderDetail(
	id = id,
	code = code,
	status = status,
	paymentMethod = paymentMethod,
	paymentStatus = paymentStatus,
	amount = amount,
	tax = tax,
	shippingFee = shippingFee,
	discount = discount,
	products = products.map { it.toOrderProduct() },
	orderAddress = orderAddress?.toOrderAddress(),
	shippingStatus = shippingStatus,
	shipping = shipping,
	date = date,
	containPhysicalProduct = containPhysicalProduct
)