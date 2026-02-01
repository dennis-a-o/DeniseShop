package com.example.deniseshop.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
	@SerialName( "id") val id: Long,
	@SerialName(  "reviewer_name") val reviewerName: String,
	@SerialName(  "reviewer_image") val reviewerImage: String,
	@SerialName(  "star") val star: Int,
	@SerialName(  "comment") val comment: String,
	@SerialName(  "created_at") val createdAt: String
)
