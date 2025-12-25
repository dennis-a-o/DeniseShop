package com.example.deniseshop.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun isLoggedIn(): Boolean = false

fun datetimeToMillisecond(datetime: String): Long{
	val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
	try {
		val dateObject = inputFormat.parse(datetime)
		return dateObject!!.time
	}catch(e :Exception){
		return 0L
	}
}