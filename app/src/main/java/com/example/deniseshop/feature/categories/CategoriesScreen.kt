package com.example.deniseshop.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Brand
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.components.ColumnGrid
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.IconWithBadge
import com.example.deniseshop.feature.categories.components.CategoriesBrandItem
import com.example.deniseshop.feature.categories.components.MainCategoryItem
import com.example.deniseshop.feature.categories.components.SubCategoryItem
import com.example.deniseshop.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
	viewModel: CategoriesViewModel = hiltViewModel(),
	onNavigate: (String) -> Unit
) {
	val state =  viewModel.state.collectAsState()
	val cartItems = viewModel.cartItems.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text = stringResource(R.string.categories),
				)
			},
			actions = {
				IconButton(
					onClick = {
						onNavigate(Routes.Search.route)
					}
				) {
					Icon(
						painter = painterResource(id = R.drawable.ic_search),
						contentDescription = "",
					)
				}
				IconButton(
					onClick = {
						onNavigate(Routes.Cart.route)
					}
				) {
					IconWithBadge(
						badge = cartItems.value.size,
						icon = R.drawable.ic_cart_outline ,
						modifier = Modifier.padding(4.dp) ,
					)
				}
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = { viewModel.refresh() },
			modifier = Modifier
				.fillMaxSize()
		) {
			when(state.value) {
				is ScreenState.Error -> {
					ErrorUi(
						onErrorAction = {
							viewModel.refresh()
						},
						title = stringResource(R.string.error),
						message = (state.value as ScreenState.Error).error.asString()
					)
				}
				ScreenState.Loading -> {
					CircularProgressIndicator(
						modifier = Modifier
							.align(Alignment.Center)
					)
				}
				is ScreenState.Success<List<Category>> -> {
					CategoryScreen(
						categories = (state.value as ScreenState.Success<List<Category>>).data,
						onNavigate = onNavigate
					)
				}
			}
		}
	}
}

@Composable
private fun CategoryScreen(
	categories: List<Category>,
	onNavigate: (String) -> Unit,
	modifier: Modifier = Modifier
){
	var selectedCategory by remember { mutableStateOf(categories[0]) }

	Row(
		modifier = modifier
			.fillMaxSize()
	) {
		MainCategoriesSection(
			categories = categories,
			selectedCategory = selectedCategory,
			modifier = Modifier
				.weight(30f),
			onCategoryClick = {
				selectedCategory = it
			}
		)
		Spacer(Modifier.width(8.dp))
		SelectedCategorySection(
			category = selectedCategory,
			modifier = Modifier
				.weight(70f),
			onCategoryClick = {
				onNavigate("${Routes.CategoryProductScreen.route}/$it")
			},
			onBrandClick = {
				onNavigate("${Routes.BrandProductScreen.route}/$it/0")
			},
			onSeeAllCategoryClick = {
				onNavigate("${Routes.CategoryProductScreen.route}/$it")
			},
			onSeeAllBrandClick = {
				onNavigate("${Routes.BrandProductScreen.route}/0/$it")
			}
		)
	}
}

@Composable
private fun MainCategoriesSection(
	categories: List<Category>,
	selectedCategory: Category,
	onCategoryClick: (Category) -> Unit,
	modifier: Modifier = Modifier
){
	Column(
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.padding(start = 8.dp, top = 8.dp)
	) {
		categories.forEach { category ->
			MainCategoryItem(
				category = category,
				selected = selectedCategory  == category,
				onClick = onCategoryClick
			)
			Spacer(Modifier.height(8.dp))
		}
	}
}

@Composable
private fun SelectedCategorySection(
	category: Category,
	onCategoryClick: (Long) -> Unit,
	onBrandClick: (Long) -> Unit,
	onSeeAllCategoryClick: (Long) -> Unit,
	onSeeAllBrandClick: (Long) -> Unit,
	modifier: Modifier = Modifier
){
	Column(
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.padding(top = 8.dp, end = 8.dp)
	) {
		if (category.brands?.isNotEmpty() ?: false) {
			BrandContent(
				brands = category.brands,
				onBrandClick = onBrandClick,
				onSeeAllBrandClick = {
					onSeeAllBrandClick(category.id)
				}
			)
			Spacer(Modifier.height(8.dp))
		}

		category.categories?.forEach { category ->
			CategoryContent(
				category = category,
				onCategoryClick = onCategoryClick,
				onSeeAllCategoryClick = onSeeAllCategoryClick
			)
			Spacer(Modifier.height(8.dp))
		}
	}
}

@Composable
private fun BrandContent(
	brands: List<Brand>,
	onBrandClick: (Long) -> Unit,
	onSeeAllBrandClick: () -> Unit,
	modifier: Modifier = Modifier
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = stringResource(R.string.brands),
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = onSeeAllBrandClick,
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		ColumnGrid(
			columns = 3,
			itemCount = brands.size,
			gridWidthSpacing = 8.dp,
			gridHeightSpacing = 8.dp
		){ index ->
			CategoriesBrandItem(
				brand = brands[index],
				onClick = onBrandClick
			)
		}
	}
}

@Composable
private fun CategoryContent(
	category: Category,
	onCategoryClick: (Long) -> Unit,
	onSeeAllCategoryClick: (Long) -> Unit,
	modifier: Modifier = Modifier
){
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = category.name,
				style = MaterialTheme.typography.titleMedium
			)
			TextButton(
				onClick = {
					onSeeAllCategoryClick(category.id)
				},
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		if (category.categories?.isNotEmpty() ?: false) {
			ColumnGrid(
				columns = 3,
				itemCount = category.categories.size,
				gridWidthSpacing = 8.dp,
				gridHeightSpacing = 8.dp
			) { index ->
				SubCategoryItem(
					category = category.categories[index],
					onClick = onCategoryClick
				)
			}
		}
	}
}