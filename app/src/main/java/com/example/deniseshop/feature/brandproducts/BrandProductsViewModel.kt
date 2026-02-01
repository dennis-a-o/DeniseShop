package com.example.deniseshop.feature.brandproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.core.domain.model.ProductData
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.ProductSortOption
import com.example.deniseshop.core.domain.model.onSuccess
import com.example.deniseshop.core.domain.repository.ShopRepository
import com.example.deniseshop.core.domain.repository.UserSettingRepository
import com.example.deniseshop.core.presentation.models.ProductFilterState
import com.example.deniseshop.navigation.Route
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = BrandProductsViewModel.Factory::class)
class BrandProductsViewModel @AssistedInject constructor(
	private val shopRepository: ShopRepository,
	private val settingRepository: UserSettingRepository,
	@Assisted val navKey: Route.BrandProducts,
): ViewModel() {
	private val brandId = navKey.brandId
	private val categoryId = navKey.categoryId

	private val _state = MutableStateFlow(BrandProductsState())

	val state = _state.asStateFlow()

	val brandProductsPagingSource = Pager(
		config = PagingConfig(pageSize = 20, initialLoadSize = 20),
		pagingSourceFactory = {
			shopRepository.getBrandProducts(
				brandId = brandId,
				filterParams = ProductFilterParams(
					pageSize = 20,
					minPrice = _state.value.filterState.priceRange.start.toInt(),
					maxPrice = _state.value.filterState.priceRange.endInclusive.toInt(),
					sortBy = _state.value.sortOption,
					categories = _state.value.filterState.selectedCategories,
					brands = _state.value.filterState.selectedBrands,
					colors = _state.value.filterState.selectedColors,
					sizes = _state.value.filterState.selectedSize,
					rating = _state.value.filterState.rating
				)
			)
		}
	).flow.cachedIn(viewModelScope)

	val cartItems = settingRepository.getCartItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	val wishlistItems = settingRepository.getWishlistItems()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = emptyList(),
		)

	init {
		getBrand()
		getCategoryBrands()
		getProductFilter()
	}

	fun onFilterStateChange(filterState: ProductFilterState){
		_state.update {
			it.copy(
				filterState = filterState
			)
		}
	}

	fun onSortOptionChange(sortOption: ProductSortOption){
		_state.update {
			it.copy(
				sortOption = sortOption
			)
		}
	}

	fun onCartToggle(productId: Long){
		viewModelScope.launch {
			if (productId in cartItems.value){
				shopRepository.removeFromCart(productId)
			}else{
				shopRepository.addToCart(ProductData(
					productId = productId,
					quantity = null,
					size = null,
					color = null
					)
				)
			}
		}
	}

	fun onWishlistToggle(productId: Long){
		viewModelScope.launch {
			if (productId in wishlistItems.value){
				shopRepository.removeFromWishlist(productId)
			}else{
				shopRepository.addToWishlist(productId)
			}
		}
	}

	private fun getCategoryBrands(){
		viewModelScope.launch {
			shopRepository.getCategoryBrands(categoryId)
				.onSuccess { data ->
					_state.update {
						it.copy(brands = data)
					}
				}
		}
	}

	private fun getBrand(){
		viewModelScope.launch {
			shopRepository.getBrand(brandId)
				.onSuccess { data ->
					_state.update {
						it.copy(brand = data)
					}
				}
		}
	}

	private fun getProductFilter(){
		viewModelScope.launch {
			shopRepository.getProductFilter(0,0)
				.onSuccess { data ->
					_state.update {
						it.copy(filter = data)
					}
				}
		}
	}

	@AssistedFactory
	interface Factory {
		fun create(navKey: Route.BrandProducts): BrandProductsViewModel
	}
}