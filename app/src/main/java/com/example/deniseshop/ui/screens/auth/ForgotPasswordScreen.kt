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
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.DeniseShopTextField
import com.example.deniseshop.ui.screens.auth.viewModels.ForgotPasswordEvent
import com.example.deniseshop.ui.screens.auth.viewModels.ForgotPasswordViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
	onNavigateUp: () -> Unit,
	viewModel: ForgotPasswordViewModel = hiltViewModel()
){
	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current
	val forgotPasswordSuccess by viewModel.forgotPasswordSuccess.collectAsState()
	val forgotPasswordError by viewModel.forgotPasswordError.collectAsState()
	val forgotPasswordMessage by viewModel.forgotPasswordMessage.collectAsState()

	LaunchedEffect(forgotPasswordError) {
		if (forgotPasswordError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = forgotPasswordMessage,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.forgotPasswordErrorReset()
		}
	}

	LaunchedEffect(forgotPasswordSuccess) {
		if (forgotPasswordSuccess) {
			Toast.makeText(context, forgotPasswordMessage, Toast.LENGTH_LONG).show()
			viewModel.forgotPasswordSuccessReset()
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
			ForgotPasswordForm(
				onClickSignIn = {
					onNavigateUp()
				},
				viewModel = viewModel
			)
		}
	}
}

@Composable
private fun ForgotPasswordForm(
	onClickSignIn: () -> Unit,
	viewModel: ForgotPasswordViewModel
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
				text = stringResource(R.string.ForgotPassword),
				style = MaterialTheme.typography.titleLarge
			)
		}
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.email),
			text = viewModel.forgotPasswordState.value.email,
			onValueChange = {
				viewModel.onEvent(ForgotPasswordEvent.EmailChanged(it))
			},
			keyboardType = KeyboardType.Email,
			imeAction = ImeAction.Done,
			isError = viewModel.forgotPasswordState.value.emailError != null,
			errorMessage = viewModel.forgotPasswordState.value.emailError
		)
		Spacer(Modifier.height(8.dp))
		ButtonWithProgressIndicator(
			onClick = {
				viewModel.onEvent(ForgotPasswordEvent.Submit)
			},
			isLoading = viewModel.forgotPasswordState.value.isLoading,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f)
		) {
			Text(text = stringResource(R.string.reset))
		}
		Spacer(Modifier.height(8.dp))
		Row (verticalAlignment = Alignment.CenterVertically){
			Text(
				text = stringResource(R.string.alreadyHavePassword),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				)
			)
			Text(
				text = stringResource(R.string.signIn),
				modifier = Modifier
					.clip(RoundedCornerShape(16.dp))
					.clickable {
						onClickSignIn()
					},
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.primary
				)
			)
		}
		Spacer(Modifier.height(8.dp))
	}
}