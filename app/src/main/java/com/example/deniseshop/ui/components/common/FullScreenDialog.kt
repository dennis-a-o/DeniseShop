package com.example.deniseshop.ui.components.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider

@Composable
fun FullScreenDialog(
	onDismiss: () -> Unit,
	content: @Composable () -> Unit
){
	Dialog(
		onDismissRequest = { onDismiss() },
		properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
	) {
		//removes strange black border at bottom navigation
		val view = (LocalView.current.parent as DialogWindowProvider)
		DisposableEffect(key1 = 0) {
			val window = view.window
			window.navigationBarColor = Color.Transparent.toArgb()
			onDispose { }
		}
		content()
	}
}


