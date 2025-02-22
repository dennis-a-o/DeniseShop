package com.example.deniseshop.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun ErrorUi(
	onErrorAction: () -> Unit,
	title: String = stringResource(R.string.anErrorOccurred),
	text: String = stringResource(R.string.pleaseCheckYourConnectionAgain),
	buttonText: String = stringResource(R.string.tryAgain)
){
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	){
		Column (
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		){
			Text(
				text = title,
				style = MaterialTheme.typography.titleMedium
			)
			Spacer(Modifier.height(8.dp))
			Text(
				text = text,
				style = MaterialTheme.typography.bodyMedium.copy(
					textAlign = TextAlign.Center
				)
			)
			Spacer(Modifier.height(8.dp))
			Button(
				onClick = { onErrorAction() },
				shape = RoundedCornerShape(16.dp)
			) {
				Text(text = buttonText)
			}
		}
	}
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorPreview(){
	ErrorUi(onErrorAction = {})
}