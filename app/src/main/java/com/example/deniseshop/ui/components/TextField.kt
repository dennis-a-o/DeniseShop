package com.example.deniseshop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun DeniseShopTextField(
	modifier: Modifier = Modifier,
	placeholder: String,
	showLabel:Boolean = true,
	text: String = "",
	onValueChange: (String) -> Unit = {},
	keyboardType: KeyboardType = KeyboardType.Text,
	imeAction: ImeAction = ImeAction.Done,
	readOnly:Boolean = false,
	errorMessage: String? = null,
	isError: Boolean = false,
	isVisible: Boolean = false,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	singleLine: Boolean = false,
	maxLine: Int = 1,
	minLine: Int = 1
){
	val isKeyboardTypeNumber = keyboardType == KeyboardType.Phone || keyboardType == KeyboardType.Number
	val interactionSource = remember { MutableInteractionSource() }
	val isFocused by interactionSource.collectIsFocusedAsState()
	val focusRequester = remember { FocusRequester() }

	Column (
		modifier = Modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.padding(8.dp)
	){
		if(showLabel) {
			if (isFocused || text.isNotEmpty()) {
				Text(
					text = placeholder,
					style = MaterialTheme.typography.bodySmall.copy(
						color = MaterialTheme.colorScheme.primary
					)
				)
			}
		}
		BasicTextField(
			modifier = modifier,
			value = if (isKeyboardTypeNumber){
				if(text.isDigitsOnly()) text else ""
			}else text,
			onValueChange = {
				if (isKeyboardTypeNumber){
					if(text.isDigitsOnly()) onValueChange(it)
				}else onValueChange(it)
			},
			textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
			maxLines = maxLine,
			readOnly = readOnly,
			minLines = minLine,
			singleLine = singleLine,
			interactionSource = interactionSource,
			visualTransformation = if (keyboardType == KeyboardType.Password){
				if(isVisible) VisualTransformation.None else PasswordVisualTransformation()
			}else{
				VisualTransformation.None
			},
			keyboardOptions = KeyboardOptions(
				keyboardType = keyboardType,
				imeAction = imeAction
			),
			cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
			decorationBox = { innerTextField ->
				Row (
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.focusRequester(focusRequester)
				){
					if (leadingIcon != null){
						leadingIcon()
					}
					Box(
						modifier = Modifier
							.weight(1.0f)
							.padding(vertical = 8.dp)
					){
						if (text.isEmpty() && !isFocused){
							Text(
								text = placeholder,
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
							)
						}
						Box(modifier = Modifier.fillMaxWidth()){
							innerTextField()
						}
					}
					if (trailingIcon != null){
						trailingIcon()
					}
				}
			}
		)
		if (isError){
			Text(
				text = errorMessage?: "",
				color = MaterialTheme.colorScheme.error,
				style = MaterialTheme.typography.bodySmall,
				modifier = Modifier
			)
		}
	}
}