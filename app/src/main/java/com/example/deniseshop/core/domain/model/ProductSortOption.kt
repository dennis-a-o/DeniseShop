package com.example.deniseshop.core.domain.model

enum class ProductSortOption (
	val displayName: String,
	val value: String
){
	RATING_ASCENDING("Rating - Low to High","rating_asc"),
	RATING_DESCENDING("Rating - High to Low","rating_desc"),
	DATE_ASCENDING("Newest First", "date_asc"),
	DATE_DESCENDING("Oldest First", "date_desc"),
	PRICE_ASCENDING("Price - Low to High", "price_asc"),
	PRICE_DESCENDING("Price - High to Low",  "price_desc"),
	NAME_ASCENDING("Name - A to Z", "name_asc"),
	NAME_DESCENDING("Name - Z to A", "name_desc")
}