package com.example.deniseshop.feature.changepassword.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ButtonWithProgressIndicator
import com.example.deniseshop.core.presentation.components.DeniseShopTextField

@Composable
fun ChangePasswordScreen(
	viewModel: ChangePasswordViewModel = hiltViewModel(),
	onShowSnackBar:  suspend (String, String?) -> Boolean
) {
	val state by viewModel.state.collectAsState()
	var error by remember { mutableStateOf<String?>(null) }

	val context = LocalContext.current

	if (state.success) {
		Toast.makeText(context, stringResource(R.string.success), Toast.LENGTH_LONG).show()
		viewModel.onEvent(ChangePasswordEvent.ResetErrorSuccessState)
	}

	LaunchedEffect(error) {
		error?.let {
			onShowSnackBar(it, null)
			viewModel.onEvent(ChangePasswordEvent.ResetErrorSuccessState)
		}
	}

	error = if (state.error != null) {
		state.error!!.asString()
	} else null

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center
		) {
			Text(
				text = stringResource(R.string.changePassword),
				style = MaterialTheme.typography.titleLarge
			)
		}
		Spacer(Modifier.height(16.dp))
		ChangePasswordForm(
			state = state,
			onEvent = {
				viewModel.onEvent(it)
			}
		)
		Spacer(Modifier.height(16.dp))
	}

}

@Composable
private fun ChangePasswordForm(
	state: ChangePasswordState,
	onEvent: (ChangePasswordEvent) -> Unit,
	modifier: Modifier = Modifier
){
	Column(
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.fillMaxSize()
	) {
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.currentPassword),
			text = state.currentPassword,
			onValueChange = {
				onEvent(ChangePasswordEvent.CurrentPasswordChange(it))
			},
			keyboardType = KeyboardType.Password,
			imeAction = ImeAction.Next,
			isError = state.currentPasswordError != null,
			errorMessage = state.currentPasswordError?.asString()
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.newPassword),
			text = state.newPassword,
			onValueChange = {
				onEvent(ChangePasswordEvent.NewPasswordChange(it))
			},
			keyboardType = KeyboardType.Password,
			imeAction = ImeAction.Done,
			isError = state.newPasswordError != null,
			errorMessage = state.newPasswordError?.asString()
		)
		Spacer(Modifier.height(8.dp))
		ButtonWithProgressIndicator(
			onClick = { onEvent(ChangePasswordEvent.ChangePassword) },
			isLoading = state.isLoading,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f)
		) {
			Text(text = stringResource(R.string.submit))
		}
	}
}