package com.example.deniseshop.domain.usercase.wishlist

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.UiWishlist

interface GetWishlistUseCase {
	operator fun invoke(): PagingSource<Int, UiWishlist>
}