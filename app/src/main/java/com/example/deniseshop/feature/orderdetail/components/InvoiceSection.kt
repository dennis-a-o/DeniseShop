package com.example.deniseshop.feature.orderdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.deniseshop.R

@Composable
fun InvoiceSection(
	isDownloading: Boolean,
	onDownloadClick: () -> Unit,
	modifier: Modifier = Modifier,
){
	Row (
		modifier = modifier
			.shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
			.background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
			.clickable {
				if (!isDownloading) {
					onDownloadClick()
				}
			}
			.padding(16.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	){
		Row {
			if (isDownloading) {
				CircularProgressIndicator(modifier = Modifier.size(16.dp))
			}else{
				Icon(
					painter = painterResource(R.drawable.ic_receipt),
					contentDescription = "",
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(Modifier.width(16.dp))
			Text(
				text = stringResource(R.string.download_invoice),
				style = MaterialTheme.typography.bodyMedium
			)
		}
		Icon(
			painter = painterResource(R.drawable.ic_arrow_forward_ios),
			contentDescription = "" ,
			modifier = Modifier.size(14.dp),
			tint = MaterialTheme.colorScheme.primary
		)
	}
}