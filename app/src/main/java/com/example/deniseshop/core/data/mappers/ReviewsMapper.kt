package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.ReviewDto
import com.example.deniseshop.core.data.dto.ReviewStatDto
import com.example.deniseshop.core.domain.model.Review
import com.example.deniseshop.core.domain.model.ReviewStat

fun ReviewDto.toReview() = Review(
	id = id,
	reviewerName = reviewerName,
	reviewerImage = reviewerImage,
	star = star,
	comment = comment,
	createdAt = createdAt
)

fun ReviewStatDto.toReviewStat() = ReviewStat(
	totalReview = totalReview,
	averageRating = averageRating,
	starCount = starCount
)
