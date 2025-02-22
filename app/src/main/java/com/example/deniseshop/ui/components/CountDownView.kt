package com.example.deniseshop.ui.components

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.deniseshop.ui.theme.Purple100
import com.example.deniseshop.utils.datetimeToMillisecond
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

enum class CountDownType(private val type: Int) {
	DD_HH_MM_SS(0x00001111),
	HH_MM_SS(0x00000111),
	HH_MM(0x00000110),
	MM_SS(0x00000011);

	private val day = 0x00001000
	private val hour = 0x00000100
	private val minute = 0x00000010
	private val second = 0x00000001

	fun dayEnabled() = type.and(day) == day
	fun hourEnabled() = type.and(hour) == hour
	fun minuteEnabled() = type.and(minute) == minute
	fun secondEnabled() = type.and(second) == second
}

data class CountDownData(val millis: Long = 0) {
	val dayTens: String
	val dayZero: String
	val hourTens: String
	val hourZero: String
	val minuteTens: String
	val minuteZero: String
	val secondTens: String
	val secondZero: String

	init {
		var millisRemains = millis
		val days = TimeUnit.MILLISECONDS.toDays(millisRemains)
		dayTens = days.div(10).coerceIn(0L..9L).toString()
		dayZero = days.rem(10).coerceIn(0L..9L).toString()
		millisRemains -= TimeUnit.DAYS.toMillis(days)

		val hours = TimeUnit.MILLISECONDS.toHours(millisRemains)
		hourTens = hours.div(10).coerceIn(0L..9L).toString()
		hourZero = hours.rem(10).coerceIn(0L..9L).toString()
		millisRemains -= TimeUnit.HOURS.toMillis(hours)

		val minute = TimeUnit.MILLISECONDS.toMinutes(millisRemains)
		minuteTens = minute.div(10).coerceIn(0L..9L).toString()
		minuteZero = minute.rem(10).coerceIn(0L..9L).toString()
		millisRemains -= TimeUnit.MINUTES.toMillis(minute)

		val seconds = TimeUnit.MILLISECONDS.toSeconds(millisRemains)
		secondTens = seconds.div(10).coerceIn(0L..9L).toString()
		secondZero = seconds.rem(10).coerceIn(0L..9L).toString()
	}
}

@Composable
fun CountDownView(
	type: CountDownType,
	countDownData: CountDownData,
	modifier: Modifier = Modifier,
	componentModifier: Modifier = Modifier,
	digitModifier: Modifier = Modifier,
	digitStyle: TextStyle = LocalTextStyle.current,
	separator: String = ":",
	showLabel: Boolean = false,
	separatorModifier: Modifier = Modifier,
	separatorStyle: TextStyle = LocalTextStyle.current,
	labelModifier: Modifier = Modifier,
	labelStyle: TextStyle = LocalTextStyle.current
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		if(type.dayEnabled()){
			Row(
				modifier = componentModifier,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(text = countDownData.dayTens, modifier = digitModifier, style = digitStyle)
				Text(text = countDownData.dayZero, modifier = digitModifier, style = digitStyle)
				if (showLabel){
					Text(text = "d", modifier = labelModifier, style = labelStyle)
				}
			}
		}
		if(type.dayEnabled() && type.hourEnabled()){
			Text(text = separator, modifier = separatorModifier, style = separatorStyle)
		}
		if (type.hourEnabled()) {
			Row(
				modifier = componentModifier,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(text = countDownData.hourTens, modifier = digitModifier, style = digitStyle)
				Text(text = countDownData.hourZero, modifier = digitModifier, style = digitStyle)
				if (showLabel){
					Text(text = "h", modifier = labelModifier, style = labelStyle)
				}
			}
		}
		if (type.hourEnabled() && type.minuteEnabled()) {
			Text(text = separator, modifier = separatorModifier, style = separatorStyle)
		}
		if (type.minuteEnabled()) {
			Row(
				modifier = componentModifier,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(text = countDownData.minuteTens, modifier = digitModifier, style = digitStyle)
				Text(text = countDownData.minuteZero, modifier = digitModifier, style = digitStyle)
				if (showLabel) {
					Text(text = "m", modifier = labelModifier, style = labelStyle)
				}
			}
		}
		if (type.minuteEnabled() && type.secondEnabled()) {
			Text(text = separator, modifier = separatorModifier, style = separatorStyle)
		}
		if (type.secondEnabled()) {
			Row(
				modifier = componentModifier,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(text = countDownData.secondTens, modifier = digitModifier, style = digitStyle)
				Text(text = countDownData.secondZero, modifier = digitModifier, style = digitStyle)
				if (showLabel){
					Text(text = "s", modifier = labelModifier, style = labelStyle)
				}
			}
		}
	}
}

@Composable
fun FlashSaleCountDown(
	endDate: String,
	countDownType: CountDownType,
	showLabel: Boolean = false
){
	val todayMax = datetimeToMillisecond(endDate)

	var countDownData by remember {
		mutableStateOf(CountDownData(todayMax.minus(Calendar.getInstance().timeInMillis)))
	}

	CountDownView(
		type = countDownType,
		countDownData = countDownData,
		showLabel = showLabel,
		modifier = Modifier
			.background(
				color = Purple100,
				shape = RoundedCornerShape(16.dp)
			)
			.padding(horizontal = 8.dp, vertical = 4.dp),
		componentModifier = Modifier.padding(horizontal = 4.dp),
		digitStyle = TextStyle(fontWeight = FontWeight.Bold),
		separatorModifier = Modifier.padding(horizontal = 4.dp),
		separatorStyle = TextStyle(fontWeight = FontWeight.Bold),
	)

	LaunchedEffect(key1 = 0) {
		launch {
			while(true){
				if(countDownData.millis < 1000) break
				val next = countDownData.millis.minus(1000)
				countDownData = countDownData.copy(millis = next)
				delay(1000L)
			}
		}
	}
}
