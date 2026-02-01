package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SliderDto(
	@SerialName("id") val id : Long,
	@SerialName( "title") val title: String,
	@SerialName( "sub_title") val subTitle: String,
	@SerialName( "image") val image: String,
	@SerialName( "highlight_text") val highlightText: String,
	@SerialName( "description") val description: String,
	@SerialName( "link") val link: String,
	@SerialName( "type") val type: String,
	@SerialName( "type_id") val typeId: Long,
	@SerialName( "button_text") val buttonText: String
)
