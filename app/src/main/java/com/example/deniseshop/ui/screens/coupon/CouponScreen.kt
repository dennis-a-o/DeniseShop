package com.example.deniseshop.ui.screens.coupon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiCoupon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponScreen(
	onNavigateUp: () -> Unit,
	viewModel: CouponViewModel = hiltViewModel()
){
	val lazyPagingCoupons = viewModel.pager.collectAsLazyPagingItems()

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.coupons)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "" )
					}
				}
			)
		},
	) { paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		) {
			when (lazyPagingCoupons.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi()
				}

				is LoadState.Error -> {
					ErrorUi(onErrorAction = { lazyPagingCoupons.retry() })
				}

				else -> {
					LazyColumn(
						contentPadding = PaddingValues(8.dp),
						modifier = Modifier
					) {
						items(lazyPagingCoupons.itemCount) {
							lazyPagingCoupons[it]?.let { it1 ->
								CouponItem(
									coupon = it1,
									onClick = {}
								)
								Spacer(Modifier.height(8.dp))
							}

						}
						lazyPagingCoupons.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item { FooterErrorUI { lazyPagingCoupons.retry() } }
								}

								LoadState.Loading -> {
									item { FooterLoadingUi() }
								}

								is LoadState.NotLoading -> Unit
							}
						}
					}
				}
			}
		}
	}
}


@Composable
private fun CouponItem(
	coupon: UiCoupon,
	modifier: Modifier = Modifier,
	onClick: (String) -> Unit
){
	Column(
		modifier = Modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
			.fillMaxWidth()
	) {
		Row (
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		){
			Column(
				modifier = Modifier.fillMaxWidth(fraction = 0.6f)
			){
				Row (verticalAlignment = Alignment.Bottom){
					when(coupon.type) {
						 "percent" -> {
							 Text(
								 text = "${coupon.value}%",
								 style = MaterialTheme.typography.titleLarge.copy(
									 color = MaterialTheme.colorScheme.primary,
									 fontWeight = FontWeight.W900
								 )
							 )
							 Spacer(modifier.width(8.dp))
							 Text(
								 text = "OFF",
								 style = MaterialTheme.typography.titleMedium.copy(
									 color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
								 )
							 )
						 }
						  "free_shipping"-> {
								 Text(
									 text = "Free Shipping",
									 style = MaterialTheme.typography.titleMedium.copy(
										 color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
									 )
								 )
							 }
						 else -> {
							Text(
								text = "${coupon.value} bucks",
								style = MaterialTheme.typography.titleLarge.copy(
									color = MaterialTheme.colorScheme.primary,
									fontWeight = FontWeight.W900
								)
							)
							Spacer(modifier.width(8.dp))
							Text(
								text = "OFF",
								style = MaterialTheme.typography.titleMedium.copy(
									color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
								)
							)
						}
					}
				}
				Text(
					text = coupon.description?:"",
					style = MaterialTheme.typography.bodyMedium
				)
			}
			Column {
				TextButton(
					onClick = { onClick(coupon.code) },
					modifier = Modifier.dashedBorder(
						color = MaterialTheme.colorScheme.primary,
						shape = RoundedCornerShape(16.dp),
						strokeWidth = 1.dp
					)
				) {
					SelectionContainer{
						Text(
							text = coupon.code,
							style = MaterialTheme.typography.bodyMedium.copy(
								fontWeight = FontWeight.W500
							)
						)
					}
				}
			}
		}
	}
}


fun Modifier.dashedBorder(
	color: Color,
	shape: Shape,
	strokeWidth: Dp = 2.dp,
	dashLength: Dp = 4.dp,
	gapLength: Dp = 4.dp,
	cap: StrokeCap = StrokeCap.Round
) = dashedBorder(brush = SolidColor(color), shape, strokeWidth, dashLength, gapLength, cap)
fun Modifier.dashedBorder(
	brush: Brush,
	shape: Shape,
	strokeWidth: Dp = 2.dp,
	dashLength: Dp = 4.dp,
	gapLength: Dp = 4.dp,
	cap: StrokeCap = StrokeCap.Round
) = this.drawWithContent {

	val outline = shape.createOutline(size, layoutDirection, density = this)

	val dashedStroke = Stroke(
		cap = cap,
		width = strokeWidth.toPx(),
		pathEffect = PathEffect.dashPathEffect(
			intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx())
		)
	)

	drawContent()

	drawOutline(
		outline = outline,
		style = dashedStroke,
		brush = brush
	)
}


