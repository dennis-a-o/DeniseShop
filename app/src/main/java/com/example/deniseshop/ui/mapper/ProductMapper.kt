package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.models.ApiProduct
import com.example.deniseshop.data.models.ApiProductDetail
import com.example.deniseshop.data.models.ApiReview
import com.example.deniseshop.data.models.ApiReviewStat
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCategory
import com.example.deniseshop.ui.models.UiProduct
import com.example.deniseshop.ui.models.UiProductDetail
import com.example.deniseshop.ui.models.UiReview
import com.example.deniseshop.ui.models.UiReviewStat
import javax.inject.Inject

class ProductApiToUiMapper @Inject constructor(
	private val categoryListApiToUiMapper: BaseListMapper<ApiCategory, UiCategory>,
	private val brandApiToUiMapper: BaseMapper<ApiBrand, UiBrand>
): BaseMapper<ApiProduct, UiProduct> {
	override fun map(input: ApiProduct): UiProduct {
		return UiProduct(
			id = input.id,
			name = input.name,
			price = input.price,
			activePrice = input.activePrice,
			percentageDiscount = input.percentageDiscount,
			image = input.image,
			quantity = input.quantity,
			averageRating = input.averageRating,
			reviewCount = input.reviewCount,
			categories = input.categories?.let { categoryListApiToUiMapper.map(it) },
			brand = input.brand?.let { brandApiToUiMapper.map(it) },
			gallery = input.gallery,
			description = input.description,
			descriptionSummary = input.descriptionSummary,
			size = input.size,
			color = input.color
		)
	}
}

class ProductListApiToUiMapper @Inject constructor(): BaseListMapper<ApiProduct, UiProduct>{
	override fun map(input: List<ApiProduct>): List<UiProduct> {
		return input.map {
			UiProduct(
				id = it.id,
				name = it.name,
				price = it.price,
				activePrice = it.activePrice,
				percentageDiscount = it.percentageDiscount,
				image = it.image,
				quantity = it.quantity,
				averageRating = it.averageRating,
				reviewCount = it.reviewCount,
			)
		}
	}
}

class ProductDetailAPiToUiMapper @Inject constructor(
	private val productApiToUiMapper: BaseMapper<ApiProduct, UiProduct>,
	private val reviewStatAPiToUiMapper: BaseMapper<ApiReviewStat, UiReviewStat>,
	private val reviewListApiToUiMapper: BaseListMapper<ApiReview, UiReview>
): BaseMapper<ApiProductDetail, UiProductDetail>{
	override fun map(input: ApiProductDetail): UiProductDetail {
		return UiProductDetail(
			product = productApiToUiMapper.map(input.product),
			reviewStat = reviewStatAPiToUiMapper.map(input.reviewStat),
			reviews = reviewListApiToUiMapper.map(input.reviews)
		)
	}
}
