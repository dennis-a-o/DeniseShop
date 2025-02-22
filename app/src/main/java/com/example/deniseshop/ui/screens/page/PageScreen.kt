package com.example.deniseshop.ui.screens.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.ui.components.HtmlText
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageScreen(
	onNavigateUp: () -> Unit,
	viewModel: PageViewModel
){
	val pageState = viewModel.pageState.collectAsState()
	val title = viewModel.title.collectAsState()

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = title.value) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(painter = painterResource(R.drawable.ic_arrow_back),  contentDescription = "" )
					}
				}
			)
		},
		
	){ paddingValues ->
		Box(modifier = Modifier
			.padding(paddingValues)
			.fillMaxSize()
		){
			when(pageState.value){
				is ScreenState.Error -> {
					ErrorUi(onErrorAction = { viewModel.onRetry() })
				}
				ScreenState.Loading -> {
					LoadingUi()
				}
				is ScreenState.Success -> {
					PageScreen(page = (pageState.value as ScreenState.Success<UiPage>).uiData)
				}
			}
		}
	}
}

@Composable
private fun PageScreen(page: UiPage){
	Column (
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 8.dp)
	){
		Spacer(Modifier.height(8.dp))
		Text(
			text = page.name,
			style = MaterialTheme.typography.titleMedium
		)
		Spacer(Modifier.height(8.dp))
		page.image?.let {
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