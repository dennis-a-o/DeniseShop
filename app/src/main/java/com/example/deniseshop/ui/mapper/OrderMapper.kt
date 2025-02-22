package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiOrder
import com.example.deniseshop.data.models.ApiOrderAddress
import com.example.deniseshop.data.models.ApiOrderDetail
import com.example.deniseshop.data.models.ApiOrderProduct
import com.example.deniseshop.ui.models.UiOrder
import com.example.deniseshop.ui.models.UiOrderAddress
import com.example.deniseshop.ui.models.UiOrderDetail
import com.example.deniseshop.ui.models.UiOrderProduct
import javax.inject.Inject

class OrderListApiToUiMapper @Inject constructor(): BaseListMapper<ApiOrder, UiOrder> {
	override fun map(input: List<ApiOrder>): List<UiOrder> {
		return input.map{
			UiOrder(
				id = it.id,
				name = it.name,
				image = it.image,
				code = it.code,
				status = it.status,
				date = it.date
			)
		}
	}
}

class OrderProductListApiToUiMapper @Inject constructor(): BaseListMapper<ApiOrderProduct, UiOrderProduct>{
	override fun map(input: List<ApiOrderProduct>): List<UiOrderProduct> {
		return input.map {
			UiOrderProduct(
				id = it.id,
				productId = it.productId,
				name = it.name,
				image = it.image,
				price = it.price,
				quantity = it.quantity,
				size = it.size,
				color = it.color,
				rated = it.rated,
				downloadable = it.downloadable
			)
		}
	}
}

class OrderAddressApiToUiMapper @Inject constructor(): BaseMapper<ApiOrderAddress, UiOrderAddress>{
	override fun map(input: ApiOrderAddress): UiOrderAddress {
		return UiOrderAddress(
			id = input.id,
			name = input.name,
			phone = input.phone,
			email = input.email,
			address = input.address
		)
	}
}

data class OrderDetailApiToUiMapper @Inject constructor(
	private val orderAddressApiToUiMapper: BaseMapper<ApiOrderAddress, UiOrderAddress>,
	private val orderProductListApiToUiMapper: BaseListMapper<ApiOrderProduct, UiOrderProduct>
): BaseMapper<ApiOrderDetail, UiOrderDetail>{
	override fun map(input: ApiOrderDetail): UiOrderDetail {
		return UiOrderDetail(
			id  = input.id,
			code = input.code,
			status = input.status,
			paymentMethod = input.paymentMethod,
			paymentStatus = input.paymentStatus,
			amount = input.amount,
			tax = input.tax,
			shippingFee = input.shippingFee,
			discount = input.discount,
			products = orderProductListApiToUiMapper.map(input.products),
			orderAddress = input.orderAddress?.let { orderAddressApiToUiMapper.map(it) },
			date = input.date,
			shippingStatus = input.shippingStatus,
			shipping = input.shipping,
			containPhysicalProduct = input.containPhysicalProduct
		)
	}
}