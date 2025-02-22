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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.DeniseShopTextField
import com.example.deniseshop.ui.screens.auth.viewModels.SignUpEvent
import com.example.deniseshop.ui.screens.auth.viewModels.SignUpViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	onNavigateUp: () -> Unit,
	viewModel: SignUpViewModel = hiltViewModel()
){
	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current
	val signUpSuccess by viewModel.signUpSuccess.collectAsState()
	val signUpError by viewModel.signUpError.collectAsState()
	val signUpMessage by viewModel.signUpMessage.collectAsState()

	LaunchedEffect(signUpError) {
		if (signUpError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = signUpMessage,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.signUpErrorReset()
		}
	}

	LaunchedEffect(signUpSuccess) {
		if (signUpSuccess) {
			Toast.makeText(context, signUpMessage, Toast.LENGTH_LONG).show()
			viewModel.signUpSuccessReset()
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
			SignUpForm(
				onClickSignIn = { onNavigateUp() },
				onClickTerm = {
					onNavigate("${Routes.PageScreen.route}/Terms  Conditions", null)
				},
				viewModel = viewModel
			)
		}
	}
}

@Composable
private fun SignUpForm(
	onClickSignIn: () -> Unit,
	onClickTerm: () -> Unit,
	viewModel: SignUpViewModel,
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
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
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
				text = stringResource(R.string.signUp),
				style = MaterialTheme.typography.titleLarge
			)
		}
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.firstName),
			text = viewModel.signUpState.value.firstName,
			onValueChange = { viewModel.onEvent(SignUpEvent.FirstNameChanged(it)) },
			keyboardType = KeyboardType.Text,
			imeAction = ImeAction.Next,
			isError = viewModel.signUpState.value.firstNameError != null,
			errorMessage = viewModel.signUpState.value.firstNameError
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.lastName),
			text = viewModel.signUpState.value.lastName,
			onValueChange = { viewModel.onEvent(SignUpEvent.LastNameChanged(it)) },
			keyboardType = KeyboardType.Text,
			imeAction = ImeAction.Next,
			isError = viewModel.signUpState.value.lastNameError != null,
			errorMessage = viewModel.signUpState.value.lastNameError
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.email),
			text = viewModel.signUpState.value.email,
			onValueChange = { viewModel.onEvent(SignUpEvent.EmailChanged(it)) },
			keyboardType = KeyboardType.Email,
			imeAction = ImeAction.Next,
			isError = viewModel.signUpState.value.emailError != null,
			errorMessage = viewModel.signUpState.value.emailError
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.phoneNumber),
			text = viewModel.signUpState.value.phone,
			onValueChange = { viewModel.onEvent(SignUpEvent.PhoneChanged(it)) },
			keyboardType = KeyboardType.Phone,
			imeAction = ImeAction.Next,
			isError = viewModel.signUpState.value.phoneError != null,
			errorMessage = viewModel.signUpState.value.phoneError
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.password),
			text = viewModel.signUpState.value.password,
			onValueChange = { viewModel.onEvent(SignUpEvent.PasswordChanged(it)) },
			keyboardType = KeyboardType.Password,
			imeAction = ImeAction.Done,
			isError = viewModel.signUpState.value.passwordError != null,
			errorMessage = viewModel.signUpState.value.passwordError,
			trailingIcon = {
				CompositionLocalProvider {
					IconButton(
						onClick = {
							viewModel.onEvent(SignUpEvent.VisiblePassword(!(viewModel.signUpState.value.isVisiblePassword)))
						},
						modifier = Modifier.size(20.dp)
					) {
						Icon(
							painter = if(viewModel.signUpState.value.isVisiblePassword){
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
			isVisible = viewModel.signUpState.value.isVisiblePassword
		)
		Spacer(Modifier.height(8.dp))
		Column (
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.Start
		){
			Row (verticalAlignment = Alignment.CenterVertically){
				Checkbox(
					checked = viewModel.signUpState.value.acceptTerms,
					onCheckedChange = {
						viewModel.onEvent(SignUpEvent.AcceptTermChanged(it))
					},
					modifier = Modifier.scale(0.8f)
				)
				Text(
					text = stringResource(R.string.iAcceptThe),
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
					)
				)
				Text(
					text = stringResource(R.string.termsandconditons),
					modifier = Modifier.clickable { onClickTerm() },
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.primary,
						textDecoration = TextDecoration.Underline
					)
				)
			}
			if (viewModel.signUpState.value.acceptTermsError != null){
				Text(
					text = viewModel.signUpState.value.acceptTermsError?:"",
					style = MaterialTheme.typography.bodySmall.copy(
						color = MaterialTheme.colorScheme.error
					)
				)
			}
		}
		Spacer(Modifier.height(8.dp))
		ButtonWithProgressIndicator(
			onClick = {
				viewModel.onEvent(SignUpEvent.Submit)
			},
			isLoading = viewModel.signUpState.value.isLoading,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f),
		) {
			Text(text = stringResource(R.string.signUp))
		}
		Spacer(Modifier.height(8.dp))
		Row (verticalAlignment = Alignment.CenterVertically){
			Text(
				text = stringResource(R.string.alreadyHaveAnAccount),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
				)
			)
			TextButton(onClick = { onClickSignIn() }) {
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
		}
		Spacer(Modifier.height(8.dp))
	}
}
