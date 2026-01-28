package com.example.deniseshop.feature.editprofile.presentation

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ButtonWithProgressIndicator
import com.example.deniseshop.core.presentation.components.DeniseShopTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBottomSheet(
	viewModel: EditProfileViewModel = hiltViewModel(),
	onDismiss: () -> Unit,
	onShowSnackBar:  suspend (String, String?) -> Boolean
) {
	val state by viewModel.state.collectAsState()
	var error by remember {  mutableStateOf<String?>(null) }

	val context = LocalContext.current

	val galleryLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent(),
		onResult = { uri ->
			uri?.let {
				viewModel.onEvent(EditProfileEvent.ImageChange(it,context))
			}
		}
	)


	if (state.success){
		Toast.makeText(context, stringResource(R.string.success),Toast.LENGTH_LONG).show()
		viewModel.onEvent(EditProfileEvent.ResetErrorSuccessState)
	}

	LaunchedEffect(error) {
		error?.let {
			onShowSnackBar(it, null)
			viewModel.onEvent(EditProfileEvent.ResetErrorSuccessState)
		}
	}

	error = if (state.error != null){
		state.error!!.asString()
	} else null

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
	) {
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
					text = stringResource(R.string.edit_profile),
					style = MaterialTheme.typography.titleLarge
				)
			}
			Spacer(Modifier.height(32.dp))
			EditProfileForm(
				state = state,
				onEvent = {
					viewModel.onEvent(it)
				},
				onSelectImage = {
					galleryLauncher.launch("image/*")
				}
			)
		}
	}
}

@Composable
private fun EditProfileForm(
	state: EditProfileState,
	onEvent: (EditProfileEvent) -> Unit,
	onSelectImage: () -> Unit,
	modifier: Modifier = Modifier
){
	Column(
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.fillMaxSize()
	) {
		Row (
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.Center
		){
			Box{
				AsyncImage(
					model = state.imageUri ?: state.image,
					contentDescription = "",
					error = painterResource(R.drawable.ic_account_filled),
					fallback = painterResource(R.drawable.ic_account_filled),
					modifier = Modifier
						.width(70.dp)
						.height(70.dp)
						.shadow(elevation = 1.dp, shape = CircleShape)
						.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
					contentScale = ContentScale.Crop
				)
				if (state.isUploading){
					CircularProgressIndicator(
						modifier = Modifier
							.align(Alignment.Center)
							.zIndex(2.0f)
					)
				}else {
					IconButton(
						onClick = onSelectImage,
						modifier = Modifier
							.align(Alignment.BottomEnd)
							.clip(CircleShape)
							.background(MaterialTheme.colorScheme.primary)
							.size(24.dp)
					) {
						Icon(
							painter = painterResource(R.drawable.ic_photo_camera_back_outlined),
							tint = MaterialTheme.colorScheme.surfaceContainerLowest,
							contentDescription = "",
							modifier = Modifier.size(14.dp)
						)
					}
				}
			}
		}
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.firstName),
			text = state.firstName,
			onValueChange = {
				onEvent(EditProfileEvent.FirstNameChange(it))
			},
			keyboardType = KeyboardType.Text,
			imeAction = ImeAction.Next,
			isError = state.firstNameError != null,
			errorMessage = state.firstNameError?.asString()
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.lastName),
			text = state.lastName,
			onValueChange = {
				onEvent(EditProfileEvent.LastNameChange(it))
			},
			keyboardType = KeyboardType.Text,
			imeAction = ImeAction.Next,
			isError = state.lastNameError != null,
			errorMessage = state.lastNameError?.asString()
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.email),
			text = state.email,
			onValueChange = {
				onEvent(EditProfileEvent.EmailChange(it))
			},
			keyboardType = KeyboardType.Email,
			imeAction = ImeAction.Next,
			isError = state.emailError != null,
			errorMessage = state.emailError?.asString()
		)
		Spacer(Modifier.height(8.dp))
		DeniseShopTextField(
			placeholder = stringResource(R.string.phoneNumber),
			text = state.phone,
			onValueChange = {
				onEvent(EditProfileEvent.PhoneChange(it))
			},
			keyboardType = KeyboardType.Phone,
			imeAction = ImeAction.Done,
			isError = state.phoneError != null,
			errorMessage = state.phoneError?.asString()
		)
		Spacer(Modifier.height(8.dp))
		ButtonWithProgressIndicator(
			onClick = { onEvent(EditProfileEvent.Update) },
			isLoading = state.isLoading,
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(16.dp),
			progressIndicatorModifier = Modifier.scale(0.8f)
		) {
			Text(text = stringResource(R.string.update))
		}
		Spacer(Modifier.height(14.dp))
	}
}