package com.example.deniseshop.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

@Composable
fun ButtonWithProgressIndicator(
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	isLoading: Boolean = false,
	progressIndicatorModifier: Modifier = Modifier,
	progressIndicatorColor: Color = ProgressIndicatorDefaults.circularColor,
	strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
	trackColor: Color =  ProgressIndicatorDefaults.circularIndeterminateTrackColor,
	strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
	enabled: Boolean = true,
	shape: Shape = ButtonDefaults.shape,
	colors: ButtonColors = ButtonDefaults.buttonColors(),
	elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
	border: BorderStroke? = null,
	contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
	interactionSource: MutableInteractionSource? = null,
	content: @Composable() (RowScope.() -> Unit)
){
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		if (isLoading){
			CircularProgressIndicator(
				modifier = progressIndicatorModifier,
				color = progressIndicatorColor,
				strokeWidth = strokeWidth,
				trackColor = trackColor,
				strokeCap = strokeCap
			)
		}else{
			Button(
				onClick = onClick,
				modifier = modifier,
				enabled = enabled,
				shape = shape,
				colors = colors,
				elevation = elevation,
				border = border,
				contentPadding = contentPadding,
				interactionSource = interactionSource
			) {
				content()
			}
		}
	}
}




@Preview(showBackground = true)
@Composable
private fun ButtonPreview(){
ButtonWithProgressIndicator(
	isLoading = true,
	onClick = {}
){}
}