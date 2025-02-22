package com.example.deniseshop.ui

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import com.example.deniseshop.navigation.NavGraph
import com.example.deniseshop.ui.theme.DeniseShopTheme

@Composable
fun DeniseShopApp(
	darkTheme: Boolean,
	viewIntentData: Uri?,
	onClearIntentData:() -> Unit
){
	DeniseShopTheme(darkTheme = darkTheme) {
		NavGraph(
			viewIntentData = viewIntentData,
			onClearIntentData = { onClearIntentData() }
		)
	}
}

