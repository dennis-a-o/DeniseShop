package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiUser
import com.example.deniseshop.domain.models.PrefUser
import com.example.deniseshop.ui.models.UiUser
import javax.inject.Inject

class UserApiToUiMapper @Inject constructor(): BaseMapper<ApiUser, UiUser>{
	override fun map(input: ApiUser): UiUser {
		return UiUser(
			id = input.id?: 0,
			firstName = input.firstName?: "",
			lastName = input.lastName?: "",
			email = input.email?:"",
			phone = input.phone?:"",
			image = input.image?:""
		)
	}
}

class UserPrefToUiMapper @Inject constructor(): BaseMapper<PrefUser, UiUser>{
	override fun map(input: PrefUser): UiUser {
		return UiUser(
			id = input.id?: 0,
			firstName = input.firstName?: "",
			lastName = input.lastName?: "",
			email = input.email?:"",
			phone = input.phone?:"",
			image = input.image?:""
		)
	}
}