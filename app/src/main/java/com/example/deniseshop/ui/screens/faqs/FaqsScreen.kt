package com.example.deniseshop.ui.screens.faqs

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.HtmlText
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.FooterErrorUI
import com.example.deniseshop.ui.components.common.FooterLoadingUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiFaq

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqsScreen(
	onNavigateUp: () -> Unit,
	viewModel: FaqViewModel = hiltViewModel()
){
	val lazyPagingFaqs = viewModel.pager.collectAsLazyPagingItems()

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.faqs)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(painter = painterResource(R.drawable.ic_arrow_back),  contentDescription = "" )
					}
				}
			)
		}
	){ paddingValues ->
		Box(modifier = Modifier
			.padding(paddingValues)
			.fillMaxSize()
		){
			when (lazyPagingFaqs.loadState.refresh) {
				is LoadState.Loading -> {
					LoadingUi()
				}

				is LoadState.Error -> {
					ErrorUi(onErrorAction = { lazyPagingFaqs.retry() })
				}

				else -> {
					LazyColumn(
						contentPadding = PaddingValues(8.dp),
						modifier = Modifier
					) {
						items(lazyPagingFaqs.itemCount) {
							lazyPagingFaqs[it]?.let { it1 ->
								FaqItem(faq = it1)
								Spacer(Modifier.height(8.dp))
							}

						}
						lazyPagingFaqs.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item { FooterErrorUI { lazyPagingFaqs.retry() } }
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
private fun FaqItem(
	modifier: Modifier = Modifier,
	faq: UiFaq
){
	var isCollapsed by remember {
		mutableStateOf(false)
	}
	
	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
	){
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.Top
		) {
			Text(
				text = faq.question,
				modifier = Modifier.fillMaxWidth(fraction = 0.9f),
				style = MaterialTheme.typography.bodyLarge
			)
			IconButton(
				onClick = { isCollapsed = !isCollapsed },
				modifier = Modifier.size(20.dp)
			) {
				Icon(
					painter = if (isCollapsed){
						painterResource(R.drawable.ic_keyboard_arrow_up)
					}else{
						painterResource(R.drawable.ic_keyboard_arrow_down)
					},
					tint = MaterialTheme.colorScheme.primary,
					contentDescription = ""
				)
			}
		}
		if (isCollapsed)
			/*Text(
				text = faq.answer,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				)
			)*/
			HtmlText(html = faq.answer)
	}
}