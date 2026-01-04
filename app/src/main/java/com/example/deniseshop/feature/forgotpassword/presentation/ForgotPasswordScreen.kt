package com.example.deniseshop.feature.forgotpassword.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ButtonWithProgressIndicator
import com.example.deniseshop.core.presentation.components.DeniseShopTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
	viewModel: ForgotPasswordViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onSignInClick: () -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean
) {
	val state by viewModel.state.collectAsState()
	var error by remember { mutableStateOf<String?>(null) }

	LaunchedEffect(state.success) {
		if (state.success)
			onSignInClick()
	}

	LaunchedEffect(error) {
		error?.let {
			onShowSnackBar(it,null)
			viewModel.onEvent(ForgotPasswordEvent.ResetState)
		}
	}

	error = if (state.error != null){
		state.error!!.asString()
	}else{
		null
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = {},
			navigationIcon = {
				IconButton(onClick = onBackClick) {
					Icon(
						painter = painterResource(R.drawable.ic_arrow_back),
						contentDescription = null
					)
				}
			}
		)
		ForgotPasswordForm(
			state = state,
			onEvent = {
				viewModel.onEvent(it)
			},
			onSignInClick = onSignInClick
		)
	}
}

@Composable
private fun ForgotPasswordForm(
	state: ForgotPasswordState,
	onEvent: (ForgotPasswordEvent) -> Unit,
	onSignInClick: () -> Unit,
	modifier: Modifier = Modifier
){
	Column (
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 16.dp)
			.fillMaxSize(),
	) {
		Spacer(Modifier.height(32.dp))
		Text(
			text = stringResource(R.string.ForgotPassword),
			style = MaterialTheme.typography.titleLarge
		)
		Spacer(Modifier.height(16.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.email),
			text = state.email,
			onValueChange = {
				onEvent(ForgotPasswordEvent.EmailChange(it))
			},
			keyboardType = KeyboardType.Text,
			isError = state.emailError != null,
			errorMessage = state.emailError?.asString(),
			imeAction = ImeAction.Done
		)
		Spacer(Modifier.height(16.dp))
		ButtonWithProgressIndicator(
			onClick = {
				onEvent(ForgotPasswordEvent.ForgotPassword)
			},
			isLoading = state.isLoading,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f),
		) {
			Text(text = stringResource(R.string.submit))
		}
		Spacer(Modifier.height(16.dp))
		Row (verticalAlignment = Alignment.CenterVertically){
			Text(
				text = stringResource(R.string.alreadyHavePassword),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.outline
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.signIn),
				modifier = Modifier
					.clip(RoundedCornerShape(16.dp))
					.clickable {
						onSignInClick()
					},
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.primary
			)
		}
		Spacer(Modifier.height(16.dp))
	}
}