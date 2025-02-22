package com.example.deniseshop.common.state

sealed class DownloadState <out T : Any>{
	data object Idle: DownloadState<Nothing>()
	data class Loading(val id: Long = 0) : DownloadState<Nothing>()
	data class Downloading(val progress: Int): DownloadState<Nothing>()
	data class Success<out  T: Any>(val result: T): DownloadState<T>()
	data class  Error(val message: String): DownloadState<Nothing>()
}