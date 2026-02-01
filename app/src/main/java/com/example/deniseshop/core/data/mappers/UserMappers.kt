package com.example.deniseshop.core.data.mappers

import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.domain.model.User

fun UserDto.toUser() = User(
	id = id,
	firstName = firstName,
	lastName = lastName,
	email = email,
	phone = phone,
	image = image ?: ""
)