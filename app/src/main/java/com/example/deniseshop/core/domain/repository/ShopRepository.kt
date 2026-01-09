package com.example.deniseshop.core.domain.repository

import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.Result

interface ShopRepository {
	suspend fun getHome(): Result<Home,  DataError.Remote>
	suspend fun getCategories(): Result<List<Category>, DataError.Remote>
}