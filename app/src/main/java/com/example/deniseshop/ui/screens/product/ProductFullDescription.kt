package com.example.deniseshop.ui.screens.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.ui.components.HtmlText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFullDescription(
	onClose: () -> Unit,
	description: String
) {
	Scaffold (
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(R.string.productDescription)) },
				modifier = Modifier.shadow(elevation = 1.dp),
				navigationIcon = {
					IconButton(onClick = { onClose() }) {
						Icon(painter = painterResource(R.drawable.ic_arrow_back), contentDescription = "" )
					}
				}
			)
		}
	){ paddingValues ->
		Column (
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.padding(8.dp)
		){
			HtmlText(
				html = description,
				modifier = Modifier
			)
		}
	}
}