package com.example.deniseshop.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun FooterErrorUI(
	onErrorAction: () -> Unit
) {
	Column (
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	){
		Text(
			text = stringResource(R.string.anErrorOccurred),
			style = MaterialTheme.typography.titleMedium
		)
		Button(
			onClick = { onErrorAction() },
			shape = RoundedCornerShape(16.dp)
		) {
			Text(text = stringResource(R.string.retry))
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun PreviewFT(){
	FooterErrorUI {

	}
}