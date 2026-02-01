package com.example.deniseshop.feature.orderdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R
import com.example.deniseshop.core.presentation.components.ButtonWithProgressIndicator
import com.example.deniseshop.core.presentation.components.DeniseShopTextField
import com.example.deniseshop.core.presentation.components.RatingStar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewFormBottomSheet(
	isLoading: Boolean,
	itemId: Long,
	onDismiss: () -> Unit,
	onSubmit: (Long, Int, String) -> Unit,
	modifier: Modifier = Modifier,
){
	var rating by remember { mutableIntStateOf(3) }
	var review by remember { mutableStateOf("") }
	var reviewError by remember { mutableStateOf<String?>(null) }

	val reviewErrorMessage = stringResource(R.string.review_required)

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		modifier = modifier,
		sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp)
		){
			Row (
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			){
				Text(
					text = stringResource(R.string.product_review),
					style = MaterialTheme.typography.titleLarge
				)
			}
			Spacer(Modifier.height(16.dp))
			RatingStar(
				rating = rating,
				onStartClick = { rating = it },
				isIndicator = true,
				starModifier = Modifier
					.size(32.dp)
			)
			Spacer(Modifier.height(8.dp))
			DeniseShopTextField(
				placeholder =stringResource(R.string.review),
				text = review,
				onValueChange = { review = it },
				isError = reviewError != null,
				errorMessage = reviewError,
				maxLine = 8,
				minLine = 4
			)
			Spacer(Modifier.height(8.dp))
			ButtonWithProgressIndicator(
				onClick = {
					if (review.isEmpty()){
						reviewError = reviewErrorMessage
					}else{
						onSubmit(itemId, rating, review)
					}
				},
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(16.dp),
				isLoading = isLoading,
				progressIndicatorModifier = Modifier.scale(0.8f)
			) {
				Text(text = stringResource(R.string.submit))
			}
		}
	}
}