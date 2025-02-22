package com.example.deniseshop.ui.mapper

interface BaseMapper<I, O> {
	fun map(input: I):O
}