package com.example.deniseshop.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deniseshop.ui.components.DeniseShopTextField
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.models.ChangePasswordState
import com.example.deniseshop.ui.screens.profile.viewModels.ChangePasswordEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordModal(
	modifier: Modifier = Modifier,
	onClose: () -> Unit,
	onEvent: (ChangePasswordEvent) -> Unit,
	changePasswordState: ChangePasswordState
){
	val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
	val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

	ModalBottomSheet(
		onDismissRequest = { onClose() },
		sheetState = bottomSheetState,
	) {
		Column(
			modifier = modifier
				.fillMaxWidth()
				.heightIn(max = 600.dp)
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
			DeniseShopTextField(
				placeholder = stringResource(R.string.currentPassword),
				text = changePasswordState.currentPassword,
				onValueChange = { onEvent(ChangePasswordEvent.CurrentPasswordChanged(it)) },
				keyboardType = KeyboardType.Password,
				imeAction = ImeAction.Next,
				trailingIcon = {
					CompositionLocalProvider {
						IconButton(
							onClick = {
								onEvent(ChangePasswordEvent.CurrentPasswordVisible(!(changePasswordState.currentPasswordVisible)))
							},
							modifier = Modifier.size(20.dp)
						) {
							Icon(
								painter = if(changePasswordState.currentPasswordVisible){
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
				isError = changePasswordState.currentPasswordError != null,
				errorMessage = changePasswordState.currentPasswordError,
				isVisible = changePasswordState.currentPasswordVisible
			)
			Spacer(Modifier.height(8.dp))
			DeniseShopTextField(
				placeholder = stringResource(R.string.newPassword),
				text = changePasswordState.newPassword,
				onValueChange = { onEvent(ChangePasswordEvent.NewPasswordChanged(it)) },
				keyboardType = KeyboardType.Password,
				trailingIcon = {
					CompositionLocalProvider() {
						IconButton(
							onClick = {
								onEvent(ChangePasswordEvent.NewPasswordVisible(!(changePasswordState.newPasswordVisible)))
							},
							modifier = Modifier.size(20.dp)
						) {
							Icon(
								painter = if(changePasswordState.newPasswordVisible){
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
				isError = changePasswordState.newPasswordError != null,
				errorMessage = changePasswordState.newPasswordError,
				isVisible = changePasswordState.newPasswordVisible
			)
			Spacer(Modifier.height(8.dp))
			ButtonWithProgressIndicator(
				onClick = { onEvent(ChangePasswordEvent.Submit) },
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(16.dp),
				progressIndicatorModifier = Modifier.scale(0.8f),
				isLoading = changePasswordState.isLoading
			) {
				Text(text = stringResource(R.string.submit))
			}
			Spacer(Modifier.height(14.dp))
		}
	}
}
