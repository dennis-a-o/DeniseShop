package com.example.deniseshop.feature.faqs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Faq
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.HtmlText
import com.example.deniseshop.core.presentation.components.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqsScreen(
	viewModel: FaqsViewModel = hiltViewModel(),
	onBackClick: () -> Unit
) {
	val faqsItems = viewModel.faqsPagingSource.collectAsLazyPagingItems()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = { Text(text = stringResource(R.string.faqs)) },
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
				faqsItems.refresh()
			},
			modifier = Modifier
				.fillMaxSize()
		) {
			when (faqsItems.loadState.refresh) {
				LoadState.Loading -> {
					LoadingUi(
						modifier = Modifier
							.fillMaxSize()
					)
				}

				is LoadState.Error -> {
					ErrorUi(
						onErrorAction = {
							faqsItems.refresh()
						},
					)
				}

				is LoadState.NotLoading -> {
					LazyColumn(
						contentPadding = PaddingValues(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					) {
						items(faqsItems.itemCount) {
							faqsItems[it]?.let { it1 ->
								FaqItem(faq = it1)
							}

						}
						faqsItems.apply {
							when (loadState.append) {
								is LoadState.Error -> {
									item {
										ErrorUi( onErrorAction =  { faqsItems.retry() } )
									}
								}

								LoadState.Loading -> {
									item {
										LoadingUi()
									}
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
	faq: Faq,
	modifier: Modifier = Modifier,
){
	var isCollapsed by remember { mutableStateOf(false) }

	Column (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(16.dp)
	){
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.Top,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(
				text = faq.question,
				modifier = Modifier,
				style = MaterialTheme.typography.bodyLarge
			)
			IconButton(
				onClick = { isCollapsed = !isCollapsed },
				modifier = Modifier
					.size(20.dp)
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
			HtmlText(html = faq.answer)
	}
}