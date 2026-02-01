package com.example.deniseshop.feature.profile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.deniseshop.R
import com.example.deniseshop.feature.profile.presentation.ProfileState

@Composable
fun ProfileUser(
	state: ProfileState,
	onSignIn: () -> Unit,
	onEdit: () -> Unit,
	modifier: Modifier = Modifier
) {
	Row (
		modifier = modifier
			.fillMaxWidth()
			.padding(8.dp),
		verticalAlignment = Alignment.CenterVertically
	){
		Box {
			AsyncImage(
				model = state.user?.image,
				contentDescription = "",
				fallback = painterResource(R.drawable.ic_account_filled),
				error = painterResource(R.drawable.ic_account_filled),
				modifier = Modifier
					.width(80.dp)
					.height(80.dp)
					.shadow(elevation = 1.dp, shape = CircleShape)
					.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
				contentScale = ContentScale.Crop
			)
			IconButton(
				onClick = onEdit,
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
		}
		Spacer(Modifier.width(8.dp))
		Column {
			if(state.user != null) {
				Text(
					text = state.user.firstName + " " + state.user.lastName,
					style = MaterialTheme.typography.titleMedium,
					minLines = 1
				)
				Text(
					text = state.user.phone,
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