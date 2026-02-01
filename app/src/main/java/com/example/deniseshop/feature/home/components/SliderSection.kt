package com.example.deniseshop.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.core.domain.model.Slider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SectionSlider(
	sliders: List<Slider>,
	onClick: (Slider) -> Unit,
	pagerState: PagerState = rememberPagerState(
		initialPage = 0,
		initialPageOffsetFraction = 0f,
		pageCount = { sliders.size }
	)
){
	val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
	if (isDragged.not()){
		with(pagerState){
			var currentPagerKey by remember { mutableIntStateOf(0) }
			LaunchedEffect(key1 = currentPagerKey) {
				launch {
					delay(5000L)
					val nextPage = (currentPage+1).mod(pageCount)
					animateScrollToPage(nextPage)
					currentPagerKey = nextPage
				}
			}
		}
	}

	Box {
		HorizontalPager(
			state = pagerState,
			pageSize = PageSize.Fill,
			contentPadding = PaddingValues(16.dp),
			pageSpacing = 16.dp
		) { page ->
			BannerItem(slider = sliders[page], onClick = { onClick(it) } )
		}
		DotIndicators(
			pagerState = pagerState,
			modifier = Modifier.align(Alignment.BottomCenter)
		)
	}
}

@Composable
private fun BannerItem(
	slider: Slider,
	onClick: (Slider) -> Unit,
	modifier: Modifier = Modifier
){
	Box(
		modifier = modifier
			.height(180.dp)
			.fillMaxWidth()
			.shadow(1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
		contentAlignment = Alignment.BottomStart
	){
		Image(
			painter = rememberAsyncImagePainter(model = slider.image),
			contentDescription = null,
			contentScale = ContentScale.Inside,
			modifier = Modifier.align(Alignment.CenterEnd)
		)
		Column (Modifier.padding(16.dp)){
			Text(
				text = slider.subTitle,
				style = MaterialTheme.typography.bodySmall.copy(
					color = MaterialTheme.colorScheme.onBackground,
				),
				maxLines = 1
			)
			Text(
				text = slider.title,
				style = MaterialTheme.typography.bodyLarge.copy(
					color = MaterialTheme.colorScheme.onBackground,
					fontWeight = FontWeight.W700,
				),
				maxLines = 1
			)
			Text(
				text = slider.highlightText,
				style = MaterialTheme.typography.titleLarge.copy(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.W900
				),
				maxLines = 1
			)
			Text(
				text = slider.description,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
				),
				maxLines = 2
			)
			Spacer(Modifier.height(4.dp))
			Button(
				onClick = { onClick(slider) },
				modifier = Modifier,
				shape = RoundedCornerShape(16.dp),
			) {
				Text(
					text = slider.buttonText,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
	}
}

@Composable
private fun DotIndicators(
	pagerState: PagerState,
	modifier: Modifier
){
	Row (modifier = modifier.padding(bottom = 24.dp)){
		repeat(pagerState.pageCount){ index ->
			val color = if(pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.White
			val width = if(pagerState.currentPage == index) 24.dp else 8.dp
			Box(
				modifier = Modifier
					.width(width)
					.size(8.dp)
					.clip(CircleShape)
					.background(color)
			)
			Spacer(modifier = Modifier.width(4.dp))
		}
	}
}