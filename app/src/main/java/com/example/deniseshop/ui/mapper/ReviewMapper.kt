package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiReview
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.ui.models.UiReview
import com.example.deniseshop.ui.models.UiReviewStat
import javax.inject.Inject

class ReviewApiToUiMapper @Inject constructor(): BaseMapper<ApiReview, UiReview> {
	override fun map(input: ApiReview): UiReview {
		return UiReview(
			id = input.id,
			reviewerName = input.reviewerName,
			reviewerImage = input.reviewerImage,
			star = input.star,
			comment = input.comment,
			createdAt = input.createdAt
		)
	}
}
class ReviewListApiToUiMapper @Inject constructor(): BaseListMapper<ApiReview, UiReview> {
	override fun map(input: List<ApiReview>): List<UiReview> {
		return input.map {
			UiReview(
				id = it.id,
				reviewerName = it.reviewerName,
				reviewerImage = it.reviewerImage,
				star = it.star,
				comment = it.comment,
				createdAt = it.createdAt
			)
		}
	}
}

class ReviewStatAPiToUiMapper @Inject constructor(): BaseMapper<ApiReviewStat, UiReviewStat>{
	override fun map(input: ApiReviewStat): UiReviewStat {
		return UiReviewStat(
			totalReview = input.totalReview,
			averageRating = input.averageRating,
			starCount = input.starCount
		)
	}
}

