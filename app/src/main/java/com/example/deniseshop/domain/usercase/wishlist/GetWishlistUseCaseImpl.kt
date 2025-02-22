package com.example.deniseshop.domain.usercase.wishlist

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.UiWishlist
import javax.inject.Inject

class GetWishlistUseCaseImpl @Inject constructor(
	private val apiRepository: ApiRepository
): GetWishlistUseCase {
	override fun invoke(): PagingSource<Int, UiWishlist> {
		return apiRepository.getWishlist()
	}
}