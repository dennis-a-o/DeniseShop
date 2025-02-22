package com.example.deniseshop.ui.screens.contact

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.ui.components.common.ErrorUi
import com.example.deniseshop.ui.components.common.LoadingUi
import com.example.deniseshop.ui.models.UiContact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
	onNavigateUp: () -> Unit,
	viewModel: ContactViewModel = hiltViewModel()
){
	val contactState = viewModel.contactState.collectAsState()

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.contactUs)) },
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
			when(contactState.value){
				is ScreenState.Error -> ErrorUi(onErrorAction = { viewModel.onRetry() })
				ScreenState.Loading -> LoadingUi()
				is ScreenState.Success -> {
					ContactScreen(
						contacts = (contactState.value as ScreenState.Success<List<UiContact>>).uiData
					)
				}
			}
		}
	}
}

@Composable
private fun ContactScreen(
	contacts : List<UiContact>
){
	Column (
		modifier = Modifier
			.verticalScroll(rememberScrollState())
			.fillMaxSize()
			.padding(8.dp)
	){
		contacts.forEach {
			ContactItem(contact = it)
			Spacer(Modifier.height(8.dp) )
		}
	}
}

@Composable
private fun ContactItem(
	modifier: Modifier = Modifier,
	contact: UiContact
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
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
				)
			)
		}
	}
}

