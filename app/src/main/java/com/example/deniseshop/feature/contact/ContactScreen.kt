package com.example.deniseshop.feature.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.Contact
import com.example.deniseshop.core.presentation.ScreenState
import com.example.deniseshop.core.presentation.components.ErrorUi
import com.example.deniseshop.core.presentation.components.LoadingUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
	viewModel: ContactViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
) {
	val state by viewModel.state.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(
					text = stringResource(R.string.contactUs),
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
					Column (
						modifier = Modifier
							.verticalScroll(rememberScrollState())
							.fillMaxSize()
							.padding(16.dp),
						verticalArrangement = Arrangement.spacedBy(16.dp)
					){
						(state as ScreenState.Success<List<Contact>>).data.forEach {
							ContactItem(contact = it)
						}
					}
				}
			}
		}
	}
}

@Composable
private fun ContactItem(
	contact: Contact,
	modifier: Modifier = Modifier,
){
	Row (
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.Top
	){
		Icon(
			painter = painterResource(
				id =  when(contact.type){
					"address" -> R.drawable.ic_home_outline
					"phone"-> R.drawable.ic_phone_outline
					"email" -> R.drawable.ic_mail_outline
					else -> R.drawable.ic_arrow_forward_ios
				}
			),
			tint = MaterialTheme.colorScheme.primary,
			contentDescription = "",
		)
		Spacer(Modifier.width(8.dp))
		Column {
			SelectionContainer {
				Text(
					text = contact.contact,
					style = MaterialTheme.typography.bodyMedium
				)
			}
			Text(
				text = contact.description,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.outline
				)
			)
		}
	}
}