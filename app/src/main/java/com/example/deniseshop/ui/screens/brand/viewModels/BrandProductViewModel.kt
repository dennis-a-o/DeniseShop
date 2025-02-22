package com.example.deniseshop.ui.screens.brand.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.common.event.ProductFilterEvent
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiBrand
import com.example.deniseshop.data.models.ApiCategory
import com.example.deniseshop.data.models.ApiProductFilter
import com.example.deniseshop.domain.usercase.GetProductFilterUseCase
import com.example.deniseshop.domain.usercase.brand.GetBrandProductUseCase
import com.example.deniseshop.domain.usercase.brand.GetBrandUseCase
import com.example.deniseshop.domain.usercase.category.GetCategoryUseCase
import com.example.deniseshop.ui.components.filter.SortOption
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.ProductFilterState
import com.example.deniseshop.ui.models.ProductQueryParams
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCategory
import com.example.deniseshop.ui.models.UiProductFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BrandProductViewModel @Inject constructor(
	private val getBrandProductUseCase: GetBrandProductUseCase,
	private val getBrandUseCase: GetBrandUseCase,
	private val getCategoryUseCase: GetCategoryUseCase,
	private val getProductFilterUseCase: GetProductFilterUseCase,
	private val brandApiToUiMapper: BaseMapper<ApiBrand, UiBrand>,
	private val categoryApiToUiMapper: BaseMapper<ApiCategory, UiCategory>,
	private val productFilterApiToUiMapper: BaseMapper<ApiProductFilter, UiProductFilter>,
	savedStateHandle: SavedStateHandle
):ViewModel() {
	private val brandId: Long = savedStateHandle["brandId"]?: 0
	private val categoryId: Long = savedStateHandle["categoryId"]?: 0

	private val _isProductFilterVisible = MutableStateFlow(false)
	private val _isSortModalVisible = MutableStateFlow(false)
	private val _selectedSortOption = MutableStateFlow(SortOption.DATE_ASCENDING)
	private val _filterOptions = MutableStateFlow<ScreenState<UiProductFilter>>(ScreenState.Loading)
	private val _filterState = MutableStateFlow(ProductFilterState())
	private val _category = MutableStateFlow<UiCategory?>(null)
	private val _brand = MutableStateFlow<UiBrand?>(null)
	private val _brands = MutableStateFlow<List<UiBrand>>(emptyList())
	private val _title = MutableStateFlow("")

	val isProductFilterVisible = _isProductFilterVisible.asStateFlow()
	val isSortModalVisible = _isSortModalVisible.asStateFlow()
	val selectedSortOption = _selectedSortOption.asStateFlow()
	val filterOptions = _filterOptions.asStateFlow()
	val filterState = _filterState.asStateFlow()
	val category = _category.asStateFlow()
	val brands = _brands.asStateFlow()
	val title = _title.asStateFlow()

	val pager = Pager(PagingConfig(pageSize = 10)){
		getBrandProductUseCase(
			brand = brandId,
			ProductQueryParams(
				sortBy = getSortOptionName(_selectedSortOption.value),
				minPrice = _filterState.value.priceRange.start.toInt(),
				maxPrice = _filterState.value.priceRange.endInclusive.toInt(),
				categories = _filterState.value.selectedCategories,
				brands = _filterState.value.selectedBrands,
				colors = _filterState.value.selectedColors,
				sizes = _filterState.value.selectedSize,
				rating = _filterState.value.rating
			)
		)
	}.flow.cachedIn(viewModelScope)

	init {
		if(brandId > 0){
			getBrand()
		}
		if(categoryId > 0){
			getCategory()
		}
	}

	fun onSortOptionSelected(option: SortOption){
		_selectedSortOption.value = option
	}

	fun onOpenSortModal(){
		_isSortModalVisible.value = true
	}

	fun onDismissSortModal(){
		_isSortModalVisible.value = false
	}

	fun onProductFilterEvent(event: ProductFilterEvent){
		when(event){
			is ProductFilterEvent.BrandChanged -> {
				val updatedBrands = _filterState.value.selectedBrands
				_filterState.value = _filterState.value.copy(
					selectedBrands = if (updatedBrands.contains(event.brand)) {
						updatedBrands.minus(event.brand)
					}else{
						updatedBrands.plus(event.brand)
					}
				)
			}
			is ProductFilterEvent.CategoryChanged -> {
				val updatedCategories = _filterState.value.selectedCategories
				_filterState.value = _filterState.value.copy(
					selectedCategories = if (updatedCategories.contains(event.category)) {
						updatedCategories.minus(event.category)
					}else{
						updatedCategories.plus(event.category)
					}
				)
			}
			is ProductFilterEvent.ColorChanged -> {
				val updatedColors = _filterState.value.selectedColors
				_filterState.value = _filterState.value.copy(
					selectedColors = if (updatedColors.contains(event.color)) {
						updatedColors.minus(event.color)
					}else{
						updatedColors.plus(event.color)
					}
				)
			}
			is ProductFilterEvent.PriceRangeChanged -> {
				_filterState.value = _filterState.value.copy(priceRange = event.priceRange)
			}
			is ProductFilterEvent.RatingChanged -> {
				_filterState.value = _filterState.value.copy(rating = event.rating)
			}
			is ProductFilterEvent.SizeChanged -> {
				val updatedSizes = _filterState.value.selectedSize
				_filterState.value = _filterState.value.copy(
					selectedSize = if (updatedSizes.contains(event.size)) {
						updatedSizes.minus(event.size)
					}else{
						updatedSizes.plus(event.size)
					}
				)
			}
			is ProductFilterEvent.Open ->{
				_isProductFilterVisible.value = true
				if (_filterOptions.value !is ScreenState.Success){
					getFilterOptions()
				}
			}
			is ProductFilterEvent.Close ->{
				_isProductFilterVisible.value = false
			}
			ProductFilterEvent.Reset -> {
				_filterState.value = ProductFilterState()
			}
			ProductFilterEvent.Apply -> {
				_isProductFilterVisible.value = false
			}
		}
	}

	private fun getBrand(){
		getBrandUseCase(brandId).onEach {
			when(it){
				is NetworkResponseState.Error -> {}
				is NetworkResponseState.Loading -> {}
				is NetworkResponseState.Success -> {
					_title.value = it.result.name
					_brand.value = brandApiToUiMapper.map(it.result)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun getCategory(){
		getCategoryUseCase(categoryId).onEach {
			when(it){
				is NetworkResponseState.Error -> {}
				is NetworkResponseState.Loading -> {}
				is NetworkResponseState.Success -> {
					_title.value = it.result.name
					_category.value = categoryApiToUiMapper.map(it.result)
					categoryApiToUiMapper.map(it.result).brands?.let { it1 ->
						_brands.value = it1
					}
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun getFilterOptions(){
		getProductFilterUseCase(categoryId, brandId).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_filterOptions.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_filterOptions.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_filterOptions.value = ScreenState.Success(productFilterApiToUiMapper.map(it.result))
					_filterState.value =_filterState.value.copy(
						priceRange = 0f..(it.result.maxPrice?.toFloat() ?: 900000f)
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun getSortOptionName( option: SortOption): String{
		return when(option){
			SortOption.RATING_ASCENDING -> "rating_asc"
			SortOption.RATING_DESCENDING -> "rating_desc"
			SortOption.DATE_ASCENDING -> "date_asc"
			SortOption.DATE_DESCENDING -> "date_desc"
			SortOption.PRICE_ASCENDING -> "price_asc"
			SortOption.PRICE_DESCENDING -> "price_desc"
			SortOption.NAME_ASCENDING -> "name_asc"
			SortOption.NAME_DESCENDING -> "name_desc"
		}
	}
}