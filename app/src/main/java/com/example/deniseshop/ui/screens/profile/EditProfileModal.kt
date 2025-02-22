package com.example.deniseshop.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.ButtonWithProgressIndicator
import com.example.deniseshop.ui.components.DeniseShopTextField
import com.example.deniseshop.ui.models.EditUserState
import com.example.deniseshop.ui.screens.profile.viewModels.EditUserEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileModal(
	modifier: Modifier = Modifier,
	onClose: () -> Unit,
	onEvent: (EditUserEvent) -> Unit,
	editProfileState: EditUserState
){
	val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
	val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
	var imageUrl  by remember { mutableStateOf<Uri?>(null) }

	val galleryLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent(),
		onResult = { uri ->
			uri?.let {
				imageUrl = it
				onEvent(EditUserEvent.ImageChanged(it))
			}
		}
	)

	ModalBottomSheet(
		onDismissRequest = { onClose() },
		sheetState = bottomSheetState,
	) {
		Column(
			modifier = modifier
				.fillMaxWidth()
				.heightIn(max = 800.dp)
				.padding(horizontal = 16.dp),
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = "Edit Profile",
					style = MaterialTheme.typography.titleLarge
				)
			}
			Spacer(Modifier.height(32.dp))
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			){
				Box{
					Image(
						painter = if(imageUrl != null) rememberAsyncImagePainter(model = imageUrl) else painterResource(R.drawable.ic_account_filled),
						contentDescription = "",
						modifier = Modifier
							.width(70.dp)
							.height(70.dp)
							.shadow(elevation = 1.dp, shape = CircleShape)
							.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
						contentScale = ContentScale.Crop
					)
					if (editProfileState.isUploading){
						CircularProgressIndicator(
							modifier = Modifier
								.align(Alignment.Center)
								.zIndex(2.0f)
						)
					}else {
						IconButton(
							onClick = { galleryLauncher.launch("image/*") },
							modifier = Modifier
								.align(Alignment.BottomEnd)
								.clip(CircleShape)
								.background(MaterialTheme.colorScheme.primary)
								.size(24.dp)
						) {
							Icon(
								painter = painterResource(
									id = R.drawable.ic_photo_camera_back_outlined
								),
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
				text = editProfileState.firstName,
				onValueChange = { onEvent(EditUserEvent.FirstNameChanged(it)) },
				keyboardType = KeyboardType.Text,
				imeAction = ImeAction.Next,
				isError = editProfileState.firstNameError != null,
				errorMessage = editProfileState.firstNameError
			)
			Spacer(Modifier.height(8.dp))
			DeniseShopTextField(
				placeholder = stringResource(R.string.lastName),
				text = editProfileState.lastName,
				onValueChange = { onEvent(EditUserEvent.LastNameChanged(it)) },
				keyboardType = KeyboardType.Text,
				imeAction = ImeAction.Next,
				isError = editProfileState.lastNameError != null,
				errorMessage = editProfileState.lastNameError
			)
			Spacer(Modifier.height(8.dp))
			DeniseShopTextField(
				placeholder = stringResource(R.string.email),
				text = editProfileState.email,
				onValueChange = { onEvent(EditUserEvent.EmailChanged(it)) },
				keyboardType = KeyboardType.Email,
				imeAction = ImeAction.Next,
				isError = editProfileState.emailError != null,
				errorMessage = editProfileState.emailError
			)
			Spacer(Modifier.height(8.dp))
			DeniseShopTextField(
				placeholder = stringResource(R.string.phoneNumber),
				text = editProfileState.phone,
				onValueChange = { onEvent(EditUserEvent.PhoneChanged(it)) },
				keyboardType = KeyboardType.Phone,
				imeAction = ImeAction.Done,
				isError = editProfileState.phoneError != null,
				errorMessage = editProfileState.phoneError
			)
			Spacer(Modifier.height(8.dp))
			ButtonWithProgressIndicator(
				onClick = { onEvent(EditUserEvent.Submit) },
				isLoading = editProfileState.isLoading,
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(16.dp),
				progressIndicatorModifier = Modifier.scale(0.8f)
			) {
				Text(text = stringResource(R.string.save))
			}
			Spacer(Modifier.height(14.dp))
		}
	}
}
