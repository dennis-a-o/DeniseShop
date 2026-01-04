package com.example.deniseshop.feature.signup.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ButtonWithProgressIndicator
import com.example.deniseshop.core.presentation.components.DeniseShopTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
	viewModel: SignUpViewModel = hiltViewModel(),
	onBackClick: () -> Unit,
	onTermsClick: () -> Unit,
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
			viewModel.onEvent(SignUpEvent.ResetState)
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
		SignUpForm(
			state = state,
			onEvent = {
				viewModel.onEvent(it)
			},
			onTermsClick = onTermsClick,
			onSingInClick = onSignInClick
		)
	}
}

@Composable
private fun SignUpForm(
	state: SignUpState,
	modifier: Modifier = Modifier,
	onEvent: (SignUpEvent) -> Unit,
	onSingInClick: () -> Unit,
	onTermsClick: () -> Unit
){
	Column (
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 16.dp)
			.fillMaxSize(),
	){
		Spacer(Modifier.height(32.dp))
		Text(
			text = stringResource(R.string.signUp),
			style = MaterialTheme.typography.titleLarge
		)
		Spacer(Modifier.height(16.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.firstName),
			text = state.firstName,
			onValueChange = {
				onEvent(SignUpEvent.FirstNameChange(it))
			},
			keyboardType = KeyboardType.Text,
			isError = state.firstNameError != null,
			errorMessage = state.firstNameError?.asString(),
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(16.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.lastName),
			text = state.lastName,
			onValueChange = {
				onEvent(SignUpEvent.LastNameChange(it))
			},
			keyboardType = KeyboardType.Text,
			isError = state.lastNameError != null,
			errorMessage = state.lastNameError?.asString(),
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(16.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.email),
			text = state.email,
			onValueChange = {
				onEvent(SignUpEvent.EmailChange(it))
			},
			keyboardType = KeyboardType.Email,
			isError = state.emailError != null,
			errorMessage = state.emailError?.asString(),
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(16.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.phoneNumber),
			text = state.phone,
			onValueChange = {
				onEvent(SignUpEvent.PhoneChange(it))
			},
			keyboardType = KeyboardType.Phone,
			isError = state.phoneError != null,
			errorMessage = state.phoneError?.asString(),
			imeAction = ImeAction.Next
		)
		Spacer(Modifier.height(16.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.password),
			text = state.password,
			onValueChange = {
				onEvent(SignUpEvent.PasswordChange(it))
			},
			keyboardType = KeyboardType.Password,
			isError = state.passwordError != null,
			errorMessage = state.passwordError?.asString(),
		)
		Spacer(Modifier.height(16.dp))
		Column (
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.Start
		){
			Row (
				verticalAlignment = Alignment.CenterVertically
			){
				Checkbox(
					checked = state.acceptTerms,
					onCheckedChange = {
						onEvent(SignUpEvent.AcceptTermChange(it))
					},
					modifier = Modifier.size(20.dp)
				)
				Spacer(Modifier.width(16.dp))
				Text(
					text = stringResource(R.string.iAcceptThe),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.outline
				)
				Text(
					text = stringResource(R.string.termsandconditons),
					modifier = Modifier.clickable { onTermsClick() },
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.primary,
						textDecoration = TextDecoration.Underline
					)
				)
			}
			if (state.acceptTermsError != null){
				Spacer(Modifier.height(8.dp))
				Text(
					text = state.acceptTermsError.asString(),
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.error
				)
			}
		}
		Spacer(Modifier.height(16.dp))
		ButtonWithProgressIndicator(
			onClick = {
				onEvent(SignUpEvent.SignUp)
			},
			isLoading = state.isLoading,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f),
		) {
			Text(text = stringResource(R.string.signUp))
		}
		Spacer(Modifier.height(16.dp))
		Row (verticalAlignment = Alignment.CenterVertically){
			Text(
				text = stringResource(R.string.alreadyHaveAnAccount),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.outline
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.signUp),
				modifier = Modifier
					.clip(RoundedCornerShape(16.dp))
					.clickable {
						onSingInClick()
					},
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.primary
			)
		}
		Spacer(Modifier.height(16.dp))
	}
}