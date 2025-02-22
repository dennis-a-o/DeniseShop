package com.example.deniseshop.ui.screens.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.ui.components.ColumnGrid
import com.example.deniseshop.ui.components.IconWithBadge
import com.example.deniseshop.ui.components.bars.BottomNavBar
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiBrand
import com.example.deniseshop.ui.models.UiCategory
import com.example.deniseshop.ui.screens.category.viewModels.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	wishlistBadgeCount: Int,
	cartBadgeCount: Int,
	viewModel: CategoryViewModel = hiltViewModel()
){
	val categoryState by viewModel.categoryState.collectAsState()

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.categories)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				actions = {
					TopAppBarAction(
						cartBadgeCountState = cartBadgeCount,
						onClickSearch = {
							onNavigate(Routes.Search.route, null)
						},
						onClickCart = {
							onNavigate(Routes.Cart.route, null)
						}
					)
				}
			)
		},
		bottomBar = {
			BottomNavBar(
				onNavigate = { rt, opt -> onNavigate(rt, opt) },
				currentRoute = Routes.Category.route,
				wishlistBadgeCount = wishlistBadgeCount
			)
		}
	){  paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		){
			when(categoryState){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}
				is ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success -> {
					Categories(
						categories = (categoryState as ScreenState.Success<List<UiCategory>>).uiData,
						onClickBrand = {
							onNavigate("${Routes.BrandProductScreen.route}/$it/0",null)
						},
						onClickCategory = {
							onNavigate("${Routes.CategoryProductScreen.route}/$it",null)
					    },
						onSeeAllCategory = {
							onNavigate("${Routes.CategoryProductScreen.route}/$it",null)
						},
						onSeeAllBrand = {
							onNavigate("${Routes.BrandProductScreen.route}/0/$it", null)
						},
					)
				}
			}
		}
	}
}

@Composable
private fun Categories(
	categories: List<UiCategory>,
	onClickCategory: (Long) -> Unit,
	onClickBrand: (Long) -> Unit,
	onSeeAllCategory: (Long) -> Unit,
	onSeeAllBrand: (Long) -> Unit,
){
	var selectedCategory by remember { mutableIntStateOf(0) }

	Row (modifier = Modifier.fillMaxSize()){
		Column (
			modifier = Modifier
				.verticalScroll(rememberScrollState())
				.wrapContentWidth()
				.fillMaxHeight()
				.padding(start = 8.dp)
		){
			Spacer(Modifier.height(8.dp))
			categories.forEachIndexed { index,category ->
				MainCategoryItem(
					category = category,
					onClick = { selectedCategory = index },
					isSelected =  selectedCategory == index
				)
				Spacer(Modifier.height(8.dp))
			}
		}
		Column (
			modifier = Modifier
				.verticalScroll(rememberScrollState())
				.fillMaxSize()
		){
			Spacer(Modifier.height(8.dp))
			if(categories.isNotEmpty()) {
				MainCategoryContent(
					category = categories[selectedCategory],
					onClickCategory = { onClickCategory(it) },
					onClickBrand = { onClickBrand(it) },
					onSeeAllCategory = { onSeeAllCategory(it) },
					onSeeAllBrand = { onSeeAllBrand(it) }
				)
			}
		}
	}
}

@Composable
fun MainCategoryContent(
	category: UiCategory,
	onClickCategory: (Long) -> Unit,
	onClickBrand: (Long) -> Unit,
	onSeeAllCategory: (Long) -> Unit,
	onSeeAllBrand: (Long) -> Unit
){
	if (!category.brands.isNullOrEmpty()){
		BrandContent(
			brands = category.brands,
			onClick = { onClickBrand(it) },
			onSeeAllBrand = { onSeeAllBrand(category.id) }
		)
	}
	Spacer(Modifier.height(8.dp))
	category.categories?.forEach { subCategory ->
		SubCategoryContent(
			category = subCategory,
			onClick = { onClickCategory(it) },
			onSeeAllCategory = { onSeeAllCategory(it) }
		)
		Spacer(Modifier.height(8.dp))
	}
}

@Composable
fun BrandContent(
	modifier: Modifier = Modifier,
	brands : List<UiBrand>,
	onClick: (Long) -> Unit,
	onSeeAllBrand: () -> Unit
){
	Column (
		modifier = modifier
			.padding(horizontal = 8.dp)
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
				onClick = { onSeeAllBrand() },
				modifier = Modifier.align(Alignment.CenterVertically)
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
			CategoryBrandItem(
				onClick = { onClick(it) },
				brand = brands[index]
			)
		}
	}
}

@Composable
fun SubCategoryContent(
	modifier: Modifier = Modifier,
	category: UiCategory,
	onClick: (Long) -> Unit,
	onSeeAllCategory: (Long) -> Unit
){
	Column(
		modifier = modifier
			.padding(horizontal = 8.dp)
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
				onClick = { onSeeAllCategory(category.id) },
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = stringResource(R.string.seeAll),
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		if (!category.categories.isNullOrEmpty()) {
			ColumnGrid(
				columns = 3,
				itemCount = category.categories.size,
				gridWidthSpacing = 8.dp,
				gridHeightSpacing = 8.dp
			) { index ->
				SubCategoryItem(
					category = category.categories[index],
					onClick = { onClick(it) }
				)
			}
		}
	}
}

@Composable
fun SubCategoryItem(
	modifier: Modifier = Modifier,
	category: UiCategory,
	onClick: (Long) -> Unit
){
	Column (
		modifier = modifier
			.clip(RoundedCornerShape(16.dp))
			.clickable { onClick(category.id) },
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	){
		Image(
			painter = rememberAsyncImagePainter(model = category.image),
			contentDescription = "",
			modifier = Modifier
				.clip(RoundedCornerShape(14.dp))
				.height(50.dp)
				.fillMaxWidth(),
			contentScale = ContentScale.Fit
		)
		Spacer(Modifier.height(4.dp))
		Text(
			text = category.name,
			style = MaterialTheme.typography.bodySmall,
			maxLines = 2,
			textAlign = TextAlign.Center
		)
		Spacer(Modifier.height(4.dp))
	}
}

@Composable
private fun CategoryBrandItem(
	modifier: Modifier = Modifier,
	brand: UiBrand,
	onClick: (Long) -> Unit
){
	Column (
		modifier = modifier
			.clip(RoundedCornerShape(16.dp))
			.clickable { onClick(brand.id) },
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	){
		Image(
			painter = rememberAsyncImagePainter(model = brand.logo),
			contentDescription = "",
			modifier = Modifier
				.clip(RoundedCornerShape(14.dp))
				.height(50.dp)
				.fillMaxWidth(),
			contentScale = ContentScale.Fit
		)
		Text(
			text = brand.name,
			style = MaterialTheme.typography.bodySmall,
			maxLines = 2,
			textAlign = TextAlign.Center
		)
		Spacer(Modifier.height(4.dp))
	}
}

@Composable
fun MainCategoryItem(
	category: UiCategory,
	onClick: (Long) -> Unit,
	isSelected: Boolean,
){
	val tabColor = if (isSelected){
		MaterialTheme.colorScheme.background
	} else{
		MaterialTheme.colorScheme.surfaceContainerLowest
	}

	val textStyle = if (isSelected){
		MaterialTheme.typography.bodySmall.copy(
			color = MaterialTheme.colorScheme.primary,
			fontWeight = FontWeight.W500,
			textAlign = TextAlign.Center
		)
	}else{
		MaterialTheme.typography.bodySmall.copy(
			textAlign = TextAlign.Center
		)
	}

	Column (
		modifier = Modifier
			.width(80.dp)
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = tabColor)
			.clickable { onClick(category.id) },
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	){
		Image(
			painter = rememberAsyncImagePainter(model = category.image),
			contentDescription = "" ,
			modifier = Modifier
				.height(50.dp)
				.clip(RoundedCornerShape(14.dp)),
			contentScale = ContentScale.Crop
		)
		Text(
			text = category.name,
			modifier = Modifier.padding(horizontal = 8.dp),
			overflow = TextOverflow.Ellipsis,
			maxLines = 2,
			style =  textStyle
		)
		Spacer(Modifier.height(4.dp))
	}
}


@Composable
private fun TopAppBarAction(
	cartBadgeCountState: Int,
	onClickSearch: () -> Unit,
	onClickCart: () -> Unit,
){
	IconButton(
		onClick = { onClickSearch() }
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_search),
			contentDescription ="",
		)
	}
	IconButton(
		onClick = { onClickCart() }
	) {
		IconWithBadge(
			badge = cartBadgeCountState,
			icon = R.drawable.ic_cart_outline ,
			modifier = Modifier.padding(4.dp) ,
		)
	}
}