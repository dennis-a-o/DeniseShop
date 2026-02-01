package com.example.deniseshop.feature.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun GallerySection(
	images: List<String>,
	pagerState: PagerState = rememberPagerState(
		initialPage = 0,
		initialPageOffsetFraction = 0f,
		pageCount = { images.size }
	),
	modifier: Modifier = Modifier,
){
	Box (
		modifier = modifier.fillMaxWidth(),
	){
		HorizontalPager(
			state = pagerState,
			pageSize = PageSize.Fill,
			pageSpacing = 8.dp
		) { index ->
			AsyncImage(
				model = images[index],
				contentDescription = "",
				modifier = Modifier
					.height(200.dp)
					.fillMaxWidth()
					.clip(RoundedCornerShape(16.dp))
					.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
				alignment = Alignment.TopCenter,
				contentScale = ContentScale.FillWidth
			)
		}
		Row (
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth()
				.offset(y = (-16).dp),
			horizontalArrangement = Arrangement.Center
		){
			repeat(pagerState.pageCount){ index ->
				val color = if(pagerState.currentPage == index) MaterialTheme.colorScheme.primary else Color.White
				val width = if(pagerState.currentPage == index) 24.dp else 8.dp
				Box(
					modifier = Modifier
						.width(width)
						.size(8.dp)
						.clip(CircleShape)
						.background(color)
						.border(
							width = 1.dp,
							color = MaterialTheme.colorScheme.primary,
							shape = CircleShape
						)
				)
				Spacer(modifier = Modifier.width(4.dp))
			}
		}
	}
}