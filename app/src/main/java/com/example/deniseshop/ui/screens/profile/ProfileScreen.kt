package com.example.deniseshop.ui.screens.profile

import android.widget.Toast
import androidx.collection.objectListOf
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import coil.compose.AsyncImage
import com.example.deniseshop.R
import com.example.deniseshop.navigation.Routes
import com.example.deniseshop.ui.components.bars.BottomNavBar
import com.example.deniseshop.ui.models.ProfileState
import com.example.deniseshop.ui.models.UiUser
import com.example.deniseshop.ui.screens.profile.viewModels.ChangePasswordEvent
import com.example.deniseshop.ui.screens.profile.viewModels.EditUserEvent
import com.example.deniseshop.ui.screens.profile.viewModels.ProfileEvent
import com.example.deniseshop.ui.screens.profile.viewModels.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	wishlistBadgeCount: Int,
	viewModel: ProfileViewModel = hiltViewModel()
){
	val snackBarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()
	val context = LocalContext.current

	val uiUser = viewModel.uiUser.collectAsState()
	val uiSetting = viewModel.uiSetting.collectAsState()
	val profileState = viewModel.profileState.collectAsState()
	val editUserState = viewModel.editUserState.collectAsState()
	val changePasswordState  = viewModel.changePasswordState.collectAsState()

	var deleteAccount by remember { mutableStateOf(false) }

	LaunchedEffect(
		key1 = profileState.value.isError,
		key2 = editUserState.value.isError,
		key3 = changePasswordState.value.isError
	) {
		if (profileState.value.isError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = profileState.value.message,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.onProfileEvent(ProfileEvent.Reset)
		}
		if (editUserState.value.isError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = editUserState.value.message,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.onEditUserEvent(EditUserEvent.Reset)
		}
		if (changePasswordState.value.isError) {
			coroutineScope.launch {
				snackBarHostState.showSnackbar(
					message = changePasswordState.value.message,
					duration = SnackbarDuration.Long,
				)
			}
			viewModel.onChangePasswordEvent(ChangePasswordEvent.Reset)
		}
	}

	LaunchedEffect(
		key1 = profileState.value.isSuccess,
		key2 = editUserState.value.isSuccess,
		key3 = changePasswordState.value.isSuccess
	) {
		if (profileState.value.isSuccess) {
			Toast.makeText(context,profileState.value.message, Toast.LENGTH_LONG).show()

			viewModel.onProfileEvent(ProfileEvent.Reset)
		}
		if (editUserState.value.isSuccess) {
			Toast.makeText(context, editUserState.value.message, Toast.LENGTH_LONG).show()

			viewModel.onEditUserEvent(EditUserEvent.Reset)
		}
		if (changePasswordState.value.isSuccess) {
			Toast.makeText(context, changePasswordState.value.message, Toast.LENGTH_LONG).show()

			viewModel.onChangePasswordEvent(ChangePasswordEvent.Reset)
		}
	}

	Scaffold(
		modifier = Modifier,
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.profile)) },
				modifier = Modifier.shadow(elevation = 1.dp)
			)
		},
		bottomBar = {
			BottomNavBar(
				onNavigate = { route, options -> onNavigate(route, options) },
				currentRoute = Routes.Profile.route,
				wishlistBadgeCount = wishlistBadgeCount
			)
		},
		snackbarHost = {
			SnackbarHost(
				hostState = snackBarHostState,
				modifier = Modifier
					.zIndex(2.0f)
			)
		}
	){ paddingValues ->
		Box(modifier = Modifier
			.padding(paddingValues)
			.fillMaxSize()
		){
			ProfileScreen(
				onNavigate = {route, options -> onNavigate(route, options) },
				uiUser = uiUser.value,
				onDeleteAccount = { deleteAccount = true },
				onProfileEvent = { viewModel.onProfileEvent(it) },
				profileState = profileState.value
			)
		}

		if (profileState.value.changeTheme) {
			ChangeThemeModal(
				onClose = { viewModel.onProfileEvent(ProfileEvent.ChangeTheme(false)) },
				onSelect = { viewModel.onChangeTheme(it) },
				uiSetting = uiSetting.value
			)
		}
		if (profileState.value.changePassword) {
			ChangePasswordModal(
				onClose = { viewModel.onProfileEvent(ProfileEvent.ChangePassword(false)) },
				onEvent = { viewModel.onChangePasswordEvent(it) },
				changePasswordState = changePasswordState.value
			)
		}
		if(profileState.value.editUser) {
			EditProfileModal(
				onClose = { viewModel.onProfileEvent(ProfileEvent.EditUser(false)) },
				onEvent = { viewModel.onEditUserEvent(it) },
				editProfileState = editUserState.value
			)
		}
		if (deleteAccount) {
			DeleteAccountDialog(
				onAccept = {
					deleteAccount = false
					viewModel.onProfileEvent(ProfileEvent.DeleteProfile)
				},
				onCancel = {
					deleteAccount = false
				}
			)
		}
	}
}

@Composable
private fun ProfileScreen(
	onNavigate: (String, NavOptions?) -> Unit,
	uiUser: UiUser,
	onDeleteAccount: () -> Unit,
	onProfileEvent: (ProfileEvent) -> Unit,
	profileState: ProfileState,
){
	Column (
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 8.dp)
	){

		UserProfile(
			onEditProfile = {
				onProfileEvent(ProfileEvent.EditUser(true))
			},
			onSignIn = {
				onNavigate(Routes.SignInScreen.route, null)
			},
			uiUser = uiUser,
			profileState = profileState
		)
		Spacer(Modifier.height(8.dp))
		if (profileState.isLoggedIn) {
			MyOrders {
				onNavigate(Routes.Orders.route, null)
			}
			Spacer(Modifier.height(8.dp))
			MyAddress {
				onNavigate(Routes.AllAddress.route,null)
			}
			Spacer(Modifier.height(8.dp))
			PromoCodes {
				onNavigate(Routes.PromoCodes.route, null)
			}
			Spacer(Modifier.height(8.dp))
			RecentlyViewed {
				onNavigate(Routes.RecentlyViewed.route, null)
			}
			Spacer(Modifier.height(8.dp))
			ChangePassword { onProfileEvent(ProfileEvent.ChangePassword(true)) }
		}
		Spacer(Modifier.height(8.dp))
		ChangeTheme { onProfileEvent(ProfileEvent.ChangeTheme(true)) }
		Spacer(Modifier.height(8.dp))
		AboutUs (
			onClick = {
				onNavigate("${Routes.PageScreen.route}/About Us", null)
			}
		)
		Spacer(Modifier.height(8.dp))
		ContactUs {
			onNavigate(Routes.ContactScreen.route, null)
		}
		Spacer(Modifier.height(8.dp))
		Faqs {
			onNavigate(Routes.FaqsScreen.route, null)
		}
		Spacer(Modifier.height(8.dp))
		PrivacyPolicy(
			onClick = {
				onNavigate("${Routes.PageScreen.route}/Privacy Policy", null)
			}
		)
		Spacer(Modifier.height(8.dp))
		TermsAndConditions (
			onClick = {
				onNavigate("${Routes.PageScreen.route}/Terms Conditions", null)
			}
		)
		Spacer(Modifier.height(8.dp))
		ShippingPolicy(
			onClick =  {
				onNavigate("${Routes.PageScreen.route}/Shipping Policy", null)
			}
		)
		Spacer(Modifier.height(8.dp))
		if (profileState.isLoggedIn) {
			DeleteAccount { onDeleteAccount() }
			Spacer(Modifier.height(8.dp))
			Logout { onProfileEvent(ProfileEvent.Logout) }
			Spacer(Modifier.height(8.dp))
		}
	}
}

@Composable
private fun UserProfile(
	modifier: Modifier = Modifier,
	onEditProfile: () -> Unit,
	onSignIn: () -> Unit,
	uiUser: UiUser,
	profileState: ProfileState
){
	Row (
		modifier = modifier
			.fillMaxWidth()
			.padding(8.dp),
		verticalAlignment = Alignment.CenterVertically
	){
		Box {
			if (profileState.isLoggedIn) {
				AsyncImage(
					model = uiUser.image,
					contentDescription = "",
					modifier = Modifier
						.width(80.dp)
						.height(80.dp)
						.shadow(elevation = 1.dp, shape = CircleShape)
						.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
					contentScale = ContentScale.Crop
				)
				IconButton(
					onClick = { onEditProfile() },
					modifier = Modifier
						.align(alignment = Alignment.BottomEnd)
						.clip(CircleShape)
						.background(MaterialTheme.colorScheme.primary)
						.size(28.dp)
				) {
					Icon(
						painter = painterResource(R.drawable.ic_edit_filled),
						contentDescription = "",
						modifier = Modifier.scale(0.5f),
						tint = MaterialTheme.colorScheme.surfaceContainerLowest
					)
				}
			}else{
				Image(
					painter = painterResource(R.drawable.ic_account_filled),
					contentDescription = "",
					modifier = Modifier
						.width(80.dp)
						.height(80.dp)
						.clip(CircleShape),
					contentScale = ContentScale.Crop
				)
			}
		}
		Spacer(Modifier.width(8.dp))
		Column {
			if(profileState.isLoggedIn) {
				Text(
					text = uiUser.firstName + " " + uiUser.lastName,
					style = MaterialTheme.typography.titleMedium,
					minLines = 1
				)
				Text(
					text = uiUser.phone,
					style = MaterialTheme.typography.bodySmall,
					minLines = 1
				)
			}else{
				Text(
					text = stringResource(R.string.helloGuest),
					style = MaterialTheme.typography.titleMedium,
					minLines = 1
				)
				Text(
					text = stringResource(R.string.signInSignUp),
					modifier = Modifier
						.clip(RoundedCornerShape(16.dp))
						.clickable { onSignIn() },
					style = MaterialTheme.typography.bodyMedium.copy(
						color = MaterialTheme.colorScheme.primary
					)
				)
			}
		}
	}
}

@Composable
private fun MyOrders(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row (
			verticalAlignment = Alignment.CenterVertically,
		){
			Icon(
				painter = painterResource(R.drawable.ic_inventory),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.myOrders),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun MyAddress(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_person_pin_circle),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.manageAddress),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun PromoCodes(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_discount),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.promoCodes),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun RecentlyViewed(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_timeline),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.recentViewed),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun ChangeTheme(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_style),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.changeTheme),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun ChangePassword(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_password),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.changePassword),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun AboutUs(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_info),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.aboutus),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun ContactUs(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_contact_support),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.contactUs)
				,
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun Faqs(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_question_answer),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.faqs),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun PrivacyPolicy(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_privacy_tip),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.privacyPolicy),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun TermsAndConditions(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_policy),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.termsAndConditons),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun ShippingPolicy(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_local_shipping),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.shippingPolicy),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun DeleteAccount(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				onClick()
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_delete),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.deleteAccount),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun Logout(
	modifier: Modifier = Modifier,
	onClick: () -> Unit
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable { onClick() }
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			Icon(
				painter = painterResource(R.drawable.ic_logout),
				contentDescription = "",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.logout),
				style = MaterialTheme.typography.bodyMedium.copy(
					color = MaterialTheme.colorScheme.onBackground
				)
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "",
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}

@Composable
private fun DeleteAccountDialog(
	onAccept: () -> Unit,
	onCancel: () -> Unit,
){
	AlertDialog(
		onDismissRequest = { onCancel() },
		confirmButton = {
			Button(
				onClick = { onAccept() },
				shape = RoundedCornerShape(16.dp),
				colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
			) {
				Text(text = stringResource(R.string.delete))
			}
		},
		dismissButton = {
			OutlinedButton(
				onClick = { onCancel() },
				shape = RoundedCornerShape(16.dp)
			) {
				Text(text = stringResource(R.string.cancel))
			}
		},
		title = {
			Text(text = stringResource(R.string.deleteAccountDialogTitle))
		},
		text = {
			Text(text = stringResource(R.string.deleteAccountDialogText))
		}
	)
}
