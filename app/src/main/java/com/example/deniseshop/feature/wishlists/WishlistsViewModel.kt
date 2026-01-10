package com.example.deniseshop.feature.wishlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.deniseshop.core.domain.model.onError
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import com.example.deniseshop.core.presentation.UiText
import com.example.deniseshop.core.presentation.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistsViewModel @Inject constructor(
	private val shopRepository: ShopRepository,
	private val settingRepository: UserSettingRepository
): ViewModel() {
	val wishlistPagingSource = shopRepository.getWishlists(
		limit = 20
	)
		.distinctUntilChanged()
		.cachedIn(viewModelScope)

	val cartItems = settingRepository.getCartItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	fun removeWishlist(
		id: Long,
		onSuccess: () -> Unit,
		onError: (UiText) -> Unit
	){
		viewModelScope.launch {
			shopRepository.removeWishlist(id)
				.onSuccess {
					onSuccess()
				}
				.onError {
					onError(it.toUiText())
				}
		}
	}

}