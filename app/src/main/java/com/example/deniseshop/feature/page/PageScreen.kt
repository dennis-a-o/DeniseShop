package com.example.deniseshop.feature.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Page
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi
import com.example.deniseshop.ui.components.HtmlText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageScreen(
	viewModel: PageViewModel,
	onBackClick: () -> Unit
) {
	val state by viewModel.state.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text = if (state is ScreenState.Success) {
						(state as ScreenState.Success<Page>).data.name
					} else {
						""
					},
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
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
				viewModel.onRefresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when(state) {
				is ScreenState.Error -> {
					ErrorUi(
						message = (state as ScreenState.Error).error.asString(),
						onErrorAction = {
							viewModel.onRefresh()
						}
					)
				}
				ScreenState.Loading -> {
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}
				is ScreenState.Success -> {
					PageScreen(
						page = (state as ScreenState.Success<Page>).data
					)
				}
			}
		}
	}
}

@Composable
private fun PageScreen(
	page: Page,
	modifier: Modifier = Modifier
){
	Column (
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 16.dp)
	){
		page.image?.let {
			Text(
				text = page.name,
				style = MaterialTheme.typography.titleMedium
			)
			Spacer(Modifier.height(8.dp))
			Image(
				painter = rememberAsyncImagePainter(model = it),
				contentDescription = "",
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.height(140.dp)
					.fillMaxWidth()
					.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			)
			Spacer(Modifier.height(8.dp))
		}
		page.description?.let {
			Text(
				text = it,
				style = MaterialTheme.typography.bodyMedium
			)
			Spacer(Modifier.height(8.dp))
		}
		HtmlText(
			modifier = Modifier,
			html = page.content
		)
	}
}