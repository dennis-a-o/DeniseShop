package com.example.deniseshop.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.DeniseShopTextField
import com.example.deniseshop.ui.screens.auth.viewModels.SignInEvent
import com.example.deniseshop.ui.screens.auth.viewModels.SignInViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: SignInViewModel = hiltViewModel()
){
	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current
	val signInSuccess by viewModel.signInSuccess.collectAsState()
	val signInError by viewModel.signInError.collectAsState()
	val signInMessage by viewModel.signInMessage.collectAsState()

	LaunchedEffect(signInError) {
		if (signInError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = signInMessage,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.signInErrorReset()
		}
	}

	LaunchedEffect(signInSuccess) {
		if (signInSuccess) {
			Toast.makeText(context, signInMessage, Toast.LENGTH_LONG).show()
			viewModel.signInSuccessReset()
			onNavigateUp()
		}
	}

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = {},
				navigationIcon = {
					IconButton(onClick = { onNavigateUp() }) {
						Icon(painter = painterResource(R.drawable.ic_arrow_back),  contentDescription = "" )
					}
				}
			)
		},
		snackbarHost = {
			SnackbarHost(hostState = snackBarHostState)
		}
	){ paddingValues ->
		Box(modifier = Modifier
			.padding(paddingValues)
			.fillMaxSize()
		){
			LoginForm(
				onClickSignUp = {
					onNavigate(Routes.SignUpScreen.route, null)
				},
				onClickForgotPassword = {
					onNavigate(Routes.ForgotPassword.route, null)
				},
				viewModel = viewModel
			)
		}
	}
}

@Composable
private fun LoginForm(
	onClickSignUp: () -> Unit,
	onClickForgotPassword: () -> Unit,
	viewModel: SignInViewModel
){
	Column (
		modifier = Modifier
			.verticalScroll(rememberScrollState())
			.padding(16.dp)
			.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	){
		Spacer(Modifier.height(32.dp))
		Column (
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		){
			Text(
				text = stringResource(R.string.app_name),
				style = MaterialTheme.typography.titleLarge.copy(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.Bold
				)
			)
		}
		Spacer(Modifier.height(16.dp))
		Column (
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		){
			Text(
				text = stringResource(R.string.signIn),
				style = MaterialTheme.typography.titleLarge
			)
		}
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.email),
			text = viewModel.signInState.value.email,
			onValueChange = {
				viewModel.onEvent(SignInEvent.EmailChanged(it))
			},
			keyboardType = KeyboardType.Email,
			isError = viewModel.signInState.value.emailError != null,
			errorMessage = viewModel.signInState.value.emailError
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.password),
			text = viewModel.signInState.value.password,
			onValueChange = {
				viewModel.onEvent(SignInEvent.PasswordChanged(it))
			},
			keyboardType = KeyboardType.Password,
			trailingIcon = {
				CompositionLocalProvider {
					IconButton(
						onClick = {
							viewModel.onEvent(SignInEvent.VisiblePassword(!(viewModel.signInState.value.isVisiblePassword)))
						},
						modifier = Modifier.size(20.dp)
					) {
						Icon(
							painter = if(viewModel.signInState.value.isVisiblePassword){
								painterResource(R.drawable.ic_visibility_off)
							} else{
								painterResource(R.drawable.ic_visibility)
							},
							contentDescription = "",
							tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
							modifier = Modifier.size(20.dp)
						)
					}
				}
			},
			isError = viewModel.signInState.value.passwordError != null,
			errorMessage = viewModel.signInState.value.passwordError,
			isVisible = viewModel.signInState.value.isVisiblePassword,
			imeAction = ImeAction.Done
		)
		Spacer(Modifier.height(8.dp))
		Column (
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.Start
		){
			Text(
				text = stringResource(R.string.forgotPassword),
				modifier = Modifier
					.clip(RoundedCornerShape(16.dp))
					.clickable {
						onClickForgotPassword()
					},
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				)
			)
		}
		Spacer(Modifier.height(8.dp))
		ButtonWithProgressIndicator(
			onClick = {
				viewModel.onEvent(SignInEvent.Submit)
			},
			isLoading = viewModel.signInState.value.isLoading,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f),
		) {
			Text(text = stringResource(R.string.signIn))
		}
		Spacer(Modifier.height(8.dp))
		Row (verticalAlignment = Alignment.CenterVertically){
			Text(
				text = stringResource(R.string.dontHaveAnAccount),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				)
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = stringResource(R.string.signUp),
				modifier = Modifier
					.clip(RoundedCornerShape(16.dp))
					.clickable {
						onClickSignUp()
					},
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.primary
				)
			)
		}
		Spacer(Modifier.height(8.dp))
	}
}