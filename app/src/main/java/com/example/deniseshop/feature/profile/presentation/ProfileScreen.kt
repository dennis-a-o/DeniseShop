package com.example.deniseshop.feature.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.deniseshop.R
import com.example.deniseshop.core.domain.model.PageType
import com.example.deniseshop.feature.profile.presentation.components.DeleteAccountDialog
import com.example.deniseshop.feature.profile.presentation.components.ProfileItem
import com.example.deniseshop.feature.profile.presentation.components.ProfileUser
import com.example.deniseshop.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
	viewModel: ProfileViewModel = hiltViewModel(),
	onNavigate: (String) -> Unit,
	onShowEditAccountBottomSheet: () -> Unit,
	onShowChangePasswordBottomSheet: () -> Unit,
	onShowThemeBottomSheet: () -> Unit,
	onShowSnackBar: suspend (String, String?) -> Boolean
) {
	val state by viewModel.state.collectAsState()
	var error by remember { mutableStateOf<String?>(null) }
	var showDeleteDialog by remember { mutableStateOf(false) }

	LaunchedEffect(error) {
		error?.let {
			onShowSnackBar(it,null)
			viewModel.onEvent(ProfileEvent.ResetErrorState)
		}
	}

	error = if (state.error != null){
		state.error!!.asString()
	}else{
		null
	}

	if (showDeleteDialog){
		DeleteAccountDialog(
			onAccept = {
				viewModel.onEvent(ProfileEvent.DeleteAccount)
			},
			onCancel = {
				showDeleteDialog = false
			}
		)
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		TopAppBar(
			title = {
				Text(stringResource(R.string.profile))
			}
		)
		ProfileScreen(
			state = state,
			onNavigate = onNavigate,
			onEditProfileClick = onShowEditAccountBottomSheet,
			onChangePasswordClick = onShowChangePasswordBottomSheet,
			onChangeThemeClick = onShowThemeBottomSheet,
			onDeleteProfileClick = {
				showDeleteDialog = true
			},
			onEvent = {
				viewModel.onEvent(it)
			}
		)
	}
}

@Composable
private fun ProfileScreen(
	state: ProfileState,
	onNavigate: (String) -> Unit,
	onEditProfileClick: () -> Unit,
	onChangePasswordClick: () -> Unit,
	onChangeThemeClick: () -> Unit,
	onDeleteProfileClick: () -> Unit,
	onEvent: (ProfileEvent) -> Unit,
	modifier: Modifier = Modifier
){
	PullToRefreshBox(
		isRefreshing = state.isRefreshing,
		onRefresh = {
			onEvent(ProfileEvent.Refresh)
		}
	) {
		Column(
			modifier = modifier
				.padding(horizontal = 8.dp)
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			ProfileUser(
				state = state,
				onEdit = onEditProfileClick,
				onSignIn = { onNavigate(Routes.SignInScreen.route) }
			)
			Spacer(Modifier.height(8.dp))
			if (state.user != null){
				ProfileItem(
					name = stringResource(R.string.orders),
					iconResourceId = R.drawable.ic_inventory,
					onClick = {
						onNavigate(Routes.Orders.route)
					}
				)
				Spacer(Modifier.height(8.dp))
				ProfileItem(
					name = stringResource(R.string.myAddress),
					iconResourceId = R.drawable.ic_person_pin_circle,
					onClick = {
						onNavigate(Routes.AllAddress.route)
					}
				)
				Spacer(Modifier.height(8.dp))
				ProfileItem(
					name = stringResource(R.string.promoCodes),
					iconResourceId = R.drawable.ic_discount,
					onClick = {
						onNavigate(Routes.PromoCodes.route)
					}
				)
				Spacer(Modifier.height(8.dp))
				ProfileItem(
					name = stringResource(R.string.recentViewed),
					iconResourceId = R.drawable.ic_timeline,
					onClick = {
						onNavigate(Routes.RecentlyViewed.route)
					}
				)
				Spacer(Modifier.height(8.dp))
				ProfileItem(
					name = stringResource(R.string.changePassword),
					iconResourceId = R.drawable.ic_password,
					onClick = onChangePasswordClick
				)
				Spacer(Modifier.height(8.dp))
			}
			Spacer(Modifier.height(8.dp))
			ProfileItem(
				name = stringResource(R.string.changeTheme),
				iconResourceId = R.drawable.ic_style,
				onClick = onChangeThemeClick
			)
			Spacer(Modifier.height(8.dp))
			ProfileItem(
				name = stringResource(R.string.aboutus),
				iconResourceId = R.drawable.ic_info,
				onClick = {
					onNavigate("${Routes.PageScreen.route}/${PageType.ABOUT_US}")
				}
			)
			Spacer(Modifier.height(8.dp))
			ProfileItem(
				name = stringResource(R.string.contactUs),
				iconResourceId = R.drawable.ic_contact_support,
				onClick = {
					onNavigate(Routes.ContactScreen.route)
				}
			)
			Spacer(Modifier.height(8.dp))
			ProfileItem(
				name = stringResource(R.string.faqs),
				iconResourceId = R.drawable.ic_question_answer,
				onClick = {
					onNavigate(Routes.FaqsScreen.route)
				}
			)
			Spacer(Modifier.height(8.dp))
			ProfileItem(
				name = stringResource(R.string.privacyPolicy),
				iconResourceId = R.drawable.ic_privacy_tip,
				onClick = {
					onNavigate("${Routes.PageScreen.route}/${PageType.PRIVACY_POLICY}")
				}
			)
			Spacer(Modifier.height(8.dp))
			ProfileItem(
				name = stringResource(R.string.termsAndConditons),
				iconResourceId = R.drawable.ic_policy,
				onClick = {
					onNavigate("${Routes.PageScreen.route}/${PageType.TERMS_CONDITIONS}")
				}
			)
			Spacer(Modifier.height(8.dp))
			ProfileItem(
				name = stringResource(R.string.shippingPolicy),
				iconResourceId = R.drawable.ic_local_shipping,
				onClick = {
					onNavigate("${Routes.PageScreen.route}/${PageType.SHIPPING_POLICY}")
				}
			)
			Spacer(Modifier.height(8.dp))
			if (state.user != null){
				ProfileItem(
					name = stringResource(R.string.deleteAccount),
					iconResourceId = R.drawable.ic_delete,
					onClick = onDeleteProfileClick
				)
				Spacer(Modifier.height(8.dp))
				ProfileItem(
					name = stringResource(R.string.logout),
					iconResourceId = R.drawable.ic_logout,
					onClick = {
						onEvent(ProfileEvent.Logout)
					}
				)
				Spacer(Modifier.height(8.dp))
			}
		}
	}
}