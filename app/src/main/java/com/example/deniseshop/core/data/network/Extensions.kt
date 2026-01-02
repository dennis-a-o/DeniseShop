package com.example.deniseshop.core.data.network

import com.example.deniseshop.core.data.dto.ErrorDto
import com.example.deniseshop.core.domain.model.DataError
import com.example.deniseshop.core.domain.model.Result
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

suspend  inline fun <reified T> safeCall(
	action: () -> T
): Result<T, DataError.Remote> {
	return try {
		val res = action()
		Result.Success(res)
	}catch (e: SocketTimeoutException) {
		Result.Error(DataError.Remote.REQUEST_TIMEOUT)
	}catch (e: UnknownHostException){
		Result.Error(DataError.Remote.NO_INTERNET)
	}catch (e: UnresolvedAddressException) {
		Result.Error(DataError.Remote.NO_INTERNET)
	}catch (e: SerializationException){
			Result.Error(DataError.Remote.SERIALIZATION)
	}catch (e: Exception){
		currentCoroutineContext().ensureActive()
		Result.Error(DataError.Remote.UNKNOWN)
	}
}

suspend  inline fun <reified T> safeCallResponse(
	action: () -> Response<T>
): Result<T, DataError> {
	return try {
		val response = action()
		responseResult(response)
	}catch (e: SocketTimeoutException) {
		Result.Error(DataError.Remote.REQUEST_TIMEOUT)
	}catch (e: UnknownHostException){
		Result.Error(DataError.Remote.NO_INTERNET)
	}catch (e: UnresolvedAddressException){
		Result.Error(DataError.Remote.NO_INTERNET)
	}catch (e: Exception){
		currentCoroutineContext().ensureActive()
		Result.Error(DataError.Remote.UNKNOWN)
	}
}

inline fun <reified T> responseResult(
	response: Response<T>
): Result<T, DataError>{
	return if(response.isSuccessful){
		Result.Success(response.body()!!)
	}else{
		when(response.code()){
			408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
			422  -> {
				val errorJson = response.errorBody()?.source()?.readUtf8() ?: "{}"
				try {
					val errorDto = Json.decodeFromString<ErrorDto>(errorJson)
					Result.Error(DataError.RemoteFormError(errorDto.error))
				}catch (e: SerializationException){
					Result.Error(DataError.Remote.SERIALIZATION)
				}
			}
			429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
			in 500..599 -> Result.Error(DataError.Remote.SERVER)
			else -> Result.Error(DataError.Remote.UNKNOWN)
		}
	}
}