package com.example.deniseshop.core.data.repository

import com.example.deniseshop.core.data.mappers.toCategory
import com.example.deniseshop.core.data.mappers.toHome
import com.example.deniseshop.core.data.network.RemoteDeniseShopDataSource
import com.example.deniseshop.core.domain.model.Category
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Home
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.repository.ShopRepository
import javax.inject.Inject

class RemoteShopRepository @Inject constructor(
	private val remoteDeniseShopDataSource: RemoteDeniseShopDataSource
): ShopRepository {
	override suspend fun getHome(): Result<Home, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getHome()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success(res.data.toHome())
		}
	}

	override suspend fun getCategories(): Result<List<Category>, DataError.Remote> {
		return when(val res = remoteDeniseShopDataSource.getCategories()) {
			is Result.Error -> Result.Error(res.error)
			is Result.Success -> Result.Success( data = res.data.map { it.toCategory() })
		}
	}
}