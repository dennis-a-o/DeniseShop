package com.example.deniseshop.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingUi(
	modifier: Modifier = Modifier
){
	Box(
		modifier = modifier
			.fillMaxWidth(),
		contentAlignment = Alignment.Center
	){
		CircularProgressIndicator(
			modifier = Modifier.align(Alignment.Center),
			color = MaterialTheme.colorScheme.primary
		)
	}
}