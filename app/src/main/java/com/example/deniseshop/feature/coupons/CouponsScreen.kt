package com.example.deniseshop.feature.coupons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Coupon
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsScreen(
	viewModel: CouponsViewModel = hiltViewModel(),
	onBackClick: () -> Unit
) {
	val couponItems = viewModel.couponsPagingSource.collectAsLazyPagingItems()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(stringResource(R.string.coupons),)
			},
			modifier = Modifier.shadow(elevation = 1.dp),
			navigationIcon = {
				IconButton(
					onClick = onBackClick
				) {
					Icon(
						painter = painterResource(R.drawable.ic_arrow_back),
						contentDescription = null,
					)
				}
			}
		)
		PullToRefreshBox(
			isRefreshing = false,
			onRefresh = {
				couponItems.refresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when (couponItems.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}

				is LoadState.Error -> {
					ErrorUi(
						onErrorAction = {
							couponItems.retry()
						}
					)
				}

				else -> {
					LazyColumn(
						contentPadding = PaddingValues(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						items(couponItems.itemCount) {
							couponItems[it]?.let { it1 ->
								CouponItem(
									coupon = it1,
									onClick = {}
								)
								Spacer(Modifier.height(8.dp))
							}

						}
						couponItems.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item {
										ErrorUi(
											onErrorAction = {
												retry()
											}
										)
									}
								}

								LoadState.Loading -> {
									item { LoadingUi() }
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
	coupon: Coupon,
	onClick: (String) -> Unit,
	modifier: Modifier = Modifier,
){
	Column(
		modifier = Modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
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
									color = MaterialTheme.colorScheme.outline,
								)
							)
						}
						"free_shipping"-> {
							Text(
								text = "Free Shipping",
								style = MaterialTheme.typography.titleMedium.copy(
									color = MaterialTheme.colorScheme.outline,
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
									color = MaterialTheme.colorScheme.outline,
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
			Spacer(Modifier.width(16.dp))
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