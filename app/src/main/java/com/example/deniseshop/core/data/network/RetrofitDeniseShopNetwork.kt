package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.BrandDto
import com.example.deniseshop.core.data.dto.CategoryDto
import com.example.deniseshop.core.data.dto.HomeDto
import com.example.deniseshop.core.data.dto.ImageDto
import com.example.deniseshop.core.data.dto.ProductDto
import com.example.deniseshop.core.data.dto.ProductFilterDto
import com.example.deniseshop.core.data.dto.UserCredentialDto
import com.example.deniseshop.core.data.dto.UserDto
import com.example.deniseshop.core.data.dto.WishlistDto
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.ProductFilterParams
import com.example.deniseshop.core.domain.model.Result
import com.example.deniseshop.core.domain.model.User
import com.example.deniseshop.core.domain.model.UserSignUp
import okhttp3.MultipartBody
import retrofit2.awaitResponse
import javax.inject.Inject

class RetrofitDeniseShopNetwork @Inject constructor(
	private val api: RetrofitDeniseShopNetworkApi
): RemoteDeniseShopDataSource {
	override suspend fun signUp(user: UserSignUp): Result<Unit, DataError> {
		return safeCallResponse <Unit> {
			api.signUp(
				firstName = user.firstName,
				lastName = user.lastName,
				email = user.email,
				phone = user.phone,
				password = user.password,
				acceptTerms = user.acceptTerms
			).awaitResponse()
		}
	}

	override suspend fun signIn(
		email: String,
		password: String
	): Result<UserCredentialDto, DataError> {
		return safeCallResponse <UserCredentialDto> {
			api.signIn(
				email = email,
				password = password
			).awaitResponse()
		}
	}

	override suspend fun forgotPassword(email: String): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.forgotPassword(email)
		}
	}

	override suspend fun getUser(): Result<UserDto, DataError.Remote> {
		return safeCall<UserDto> {
			api.getUser()
		}
	}

	override suspend fun updateUser(user: User): Result<UserDto, DataError> {
		return safeCallResponse<UserDto> {
			api.updateUser(
				firstName = user.firstName,
				lastName = user.lastName,
				email = user.email,
				phone = user.phone,
			).awaitResponse()
		}
	}

	override suspend fun uploadUserImage(body: MultipartBody.Part): Result<ImageDto, DataError.Remote> {
		return safeCall<ImageDto> {
			api.uploadUserImage(body)
		}
	}

	override suspend fun changePassword(
		currentPassword: String,
		newPassword: String
	): Result<Unit, DataError> {
		return safeCallResponse<Unit> {
			api.changePassword(
				currentPassword = currentPassword,
				newPassword = newPassword
			).awaitResponse()
		}
	}

	override suspend fun logout(): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.logout()
		}
	}

	override suspend fun deleteUser(): Result<Unit, DataError.Remote> {
		return safeCall<Unit> {
			api.deleteAccount()
		}
	}

	override suspend fun getHome(): Result<HomeDto, DataError.Remote> {
		return safeCall<HomeDto> {
			api.getHome()
		}
	}

	override suspend fun getCategories(): Result<List<CategoryDto>, DataError.Remote> {
		return safeCall<List<CategoryDto>> {
			api.getCategories()
		}
	}

	override suspend fun getWishlists(
		page: Int,
		pageSize: Int
	): Result<List<WishlistDto>, DataError.Remote> {
		return safeCall {
			api.getWishlists(page = page, pageSize = pageSize)
		}
	}

	override suspend fun addToWishlist(productId: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.addToWishlist(productId)
		}
	}

	override suspend fun removeWishlist(id: Long): Result<Unit, DataError.Remote> {
		return safeCall {
			api.removeWishlist(id)
		}
	}

	override suspend fun getProducts(filterParams: ProductFilterParams): Result<List<ProductDto>, DataError.Remote> {
		return safeCall<List<ProductDto>> {
			api.getProducts(
				query = filterParams.query,
				page = filterParams.page,
				pageSize = filterParams.pageSize,
				sortBy = filterParams.sortBy.value,
				minPrice = filterParams.minPrice,
				maxPrice = filterParams.maxPrice,
				categories = filterParams.categories,
				brands = filterParams.brands,
				colors = filterParams.categories,
				sizes = filterParams.sizes,
				rating = filterParams.rating
			)
		}
	}

	override suspend fun getProductFilter(
		categoryId: Long,
		brandId: Long
	): Result<ProductFilterDto, DataError.Remote> {
		return safeCall<ProductFilterDto> {
			api.getProductFilter(
				category = categoryId,
				brand = brandId
			)
		}
	}

	override suspend fun getCategory(id: Long): Result<CategoryDto, DataError.Remote> {
		return safeCall<CategoryDto> {
			api.getCategory(id)
		}
	}

	override suspend fun getBrand(id: Long): Result<BrandDto, DataError.Remote> {
		return safeCall<BrandDto> {
			api.getBrand(id)
		}
	}

	override suspend fun getCategoryBrands(categoryId: Long): Result<List<BrandDto>, DataError.Remote> {
		return safeCall<List<BrandDto>> {
			api.getCategoryBrands(categoryId)
		}
	}
}