package com.example.deniseshop.data.models

import com.example.deniseshop.data.api.BooleanType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiOrder(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "code") val code: String,
	@field:Json(name = "status") val status: String,
	@field:Json(name = "created_at") val date: String
)

@JsonClass(generateAdapter = true)
data class ApiOrderProduct(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "product_id") val productId: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "image") val image: String,
	@field:Json(name = "price") val price: String,
	@field:Json(name = "quantity") val quantity: Int,
	@field:Json(name = "size") val size: String?,
	@field:Json(name = "color") val color: String?,
	@BooleanType @field:Json(name = "rated") val rated: Boolean,
	@BooleanType @field:Json(name = "downloadable") val downloadable: Boolean
)

@JsonClass(generateAdapter = true)
data class ApiOrderAddress(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "name") val name: String,
	@field:Json(name = "email") val email: String,
	@field:Json(name = "phone") val phone: String,
	@field:Json(name = "address") val address: String,
)

@JsonClass(generateAdapter = true)
data class ApiOrderDetail(
	@field:Json(name = "id") val id: Long,
	@field:Json(name = "code") val code: String,
	@field:Json(name = "status") val status: String,
	@field:Json(name = "payment_method") val paymentMethod: String,
	@field:Json(name = "payment_status") val paymentStatus: String,
	@field:Json(name = "amount") val amount: String,
	@field:Json(name = "tax") val tax: String,
	@field:Json(name = "shipping_fee") val shippingFee: String,
	@field:Json(name = "discount") val discount: String,
	@field:Json(name = "products") val products: List<ApiOrderProduct>,
	@field:Json(name = "order_address") val orderAddress: ApiOrderAddress?,
	@field:Json(name = "shipping_status") val shippingStatus: String?,
	@field:Json(name = "shipping") val shipping: String?,
	@field:Json(name = "date") val date: String,
	@BooleanType @field:Json(name = "contain_physical_product") val containPhysicalProduct:Boolean
)



