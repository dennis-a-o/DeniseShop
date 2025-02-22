package com.example.deniseshop.ui.screens.order.viewModels

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deniseshop.common.state.DownloadState
import com.example.deniseshop.common.state.NetworkResponseState
import com.example.deniseshop.common.state.ScreenState
import com.example.deniseshop.data.models.ApiOrderDetail
import com.example.deniseshop.domain.usercase.ValidateInputUseCase
import com.example.deniseshop.domain.usercase.order.AddReviewUseCase
import com.example.deniseshop.domain.usercase.order.DownloadInvoiceUseCase
import com.example.deniseshop.domain.usercase.order.DownloadOrderItemUseCase
import com.example.deniseshop.domain.usercase.order.GetOrderDetailUseCase
import com.example.deniseshop.ui.mapper.BaseMapper
import com.example.deniseshop.ui.models.ReviewFormState
import com.example.deniseshop.ui.models.UiOrderDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
	private val getOrderDetailUseCase: GetOrderDetailUseCase,
	private val orderDetailApiToUiMapper: BaseMapper<ApiOrderDetail, UiOrderDetail>,
	private val validateInputUseCase: ValidateInputUseCase,
	private val addReviewUseCase: AddReviewUseCase,
	private val downloadInvoiceUseCase: DownloadInvoiceUseCase,
	private val downloadOrderItemUseCase: DownloadOrderItemUseCase,
	@ApplicationContext val context: Context,
	savedStateHandle: SavedStateHandle
): ViewModel() {
	private val orderId: Long = savedStateHandle["orderId"]?:0
	private val _orderDetailState = MutableStateFlow<ScreenState<UiOrderDetail>>(ScreenState.Loading)
	private val _isReviewFormVisible = MutableStateFlow(false)
	private val _reviewFormState = MutableStateFlow(ReviewFormState())
	private val _downloadInvoiceState = MutableStateFlow<DownloadState<String>>(DownloadState.Idle)
	private val _downloadItemState = MutableStateFlow<DownloadState<String>>(DownloadState.Idle)

	val orderDetailState = _orderDetailState.asStateFlow()
	val isReviewFormVisible = _isReviewFormVisible.asStateFlow()
	val reviewFormState = _reviewFormState.asStateFlow()
	val downloadInvoiceState = _downloadInvoiceState.asStateFlow()
	val downloadItemState = _downloadItemState.asStateFlow()

	init {
		getOrderDetail()
	}

	fun onEvent(event: ReviewFormEvent){
		when(event){
			ReviewFormEvent.Close -> {
				_isReviewFormVisible.value = false
			}
			is ReviewFormEvent.Open -> {
				_reviewFormState.value = _reviewFormState.value.copy(productId = event.productId)
				_isReviewFormVisible.value = true
			}
			is ReviewFormEvent.ReviewChanged -> {
				_reviewFormState.value = _reviewFormState.value.copy(review = event.review)
			}
			is ReviewFormEvent.StarChanged -> {
				_reviewFormState.value = _reviewFormState.value.copy(rating = event.rating)
			}
			ReviewFormEvent.Submit -> {
				validate()
			}
			ReviewFormEvent.ResetMessage -> {
				_reviewFormState.value = _reviewFormState.value.copy(
					isSuccess = false,
					isError = false,
					isLoading = false,
					message = ""
				)
			}
		}
	}

	fun onRetry(){
		getOrderDetail()
	}

	fun onDownloadInvoice(orderId:Long){
		downloadInvoice(orderId)
	}

	fun onDownloadItem(itemId: Long){
		downloadOrderItem(itemId)
	}

	private fun getOrderDetail(){
		getOrderDetailUseCase(orderId).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_orderDetailState.value = ScreenState.Error(it.exception.message.toString())
				}
				is NetworkResponseState.Loading -> {
					_orderDetailState.value = ScreenState.Loading
				}
				is NetworkResponseState.Success -> {
					_orderDetailState.value = ScreenState.Success(orderDetailApiToUiMapper.map(it.result))
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun validate(){
		val ratingResult = validateInputUseCase.validateNumber(_reviewFormState.value.rating.toLong(), min = 1, max = 5)
		val reviewResult = validateInputUseCase.validateString(_reviewFormState.value.review, min = 4, max = 500)

		_reviewFormState.value = _reviewFormState.value.copy(
			ratingError = ratingResult.errorMessage,
			reviewError = reviewResult.errorMessage
		)

		if (ratingResult.successful && reviewResult.successful){
			addReview()
		}
	}

	private fun addReview(){
		addReviewUseCase(
			productId = _reviewFormState.value.productId,
			rating = _reviewFormState.value.rating,
			review = _reviewFormState.value.review
		).onEach {
			when(it){
				is NetworkResponseState.Error -> {
					_reviewFormState.value = _reviewFormState.value.copy(
						isLoading = false,
						isError = true,
						message = it.exception.message.toString()
					)
					_isReviewFormVisible.value = false
				}
				NetworkResponseState.Loading -> {
					_reviewFormState.value = _reviewFormState.value.copy(
						isLoading = true
					)
				}
				is NetworkResponseState.Success -> {
					_reviewFormState.value = _reviewFormState.value.copy(
						isLoading = false,
						isSuccess = true,
						message = it.result
					)
					_isReviewFormVisible.value = false
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun downloadInvoice(orderId: Long) {
		downloadInvoiceUseCase(orderId).onEach {
			when (it) {
				is DownloadState.Downloading -> {
					showNotification(
						context = context,
						filename = "Invoice.pdf",
						progress = it.progress,
						isComplete = false
					)
					_downloadInvoiceState.value = it
				}

				is DownloadState.Error -> {
					_downloadInvoiceState.value = it
				}

				DownloadState.Idle -> {
					_downloadInvoiceState.value = it
				}

				is DownloadState.Loading -> {
					_downloadInvoiceState.value = it
				}

				is DownloadState.Success -> {
					_downloadInvoiceState.value = it
					showNotification(
						context = context,
						filename = "Invoice",
						progress = 100,
						isComplete = true
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun downloadOrderItem(itemId: Long){
		downloadOrderItemUseCase(itemId).onEach {
			when(it){
				is DownloadState.Downloading -> {
					showNotification(
						context = context,
						filename = "OrderFile",
						progress = it.progress,
						isComplete = false
					)
				}
				is DownloadState.Error -> {
					_downloadItemState.value = it
				}
				DownloadState.Idle -> {
					_downloadItemState.value = it
				}
				is DownloadState.Loading -> {
					_downloadItemState.value = it
				}
				is DownloadState.Success -> {
					_downloadItemState.value = it
					showNotification(
						context = context,
						filename = "OrderFile",
						progress = 100,
						isComplete = true
					)
				}
			}
		}.launchIn(viewModelScope)
	}

	private fun showNotification(
		context: Context,
		filename: String,
		progress: Int,
		isComplete: Boolean
	){
		val notificationManager = context.getSystemService(NotificationManager::class.java)

		val builder = NotificationCompat.Builder(context, "download_channel")
			.setSmallIcon(android.R.drawable.stat_sys_download)
			.setContentTitle(if (isComplete) "Download Complete" else "Downloading...")
			.setContentText(if (isComplete) "$filename has been downloaded" else "Downloading $filename")
			.setPriority(NotificationCompat.PRIORITY_LOW)
			.setOnlyAlertOnce(true)

		if (isComplete) {
			val intent = Intent(Intent.ACTION_VIEW).apply {
				type = "*/*"
			}
			val pendingIntent = PendingIntent.getActivity(
				context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
			)
			builder.setContentIntent(pendingIntent).setAutoCancel(true)
		} else {
			builder.setProgress(100, progress, false)
		}

		notificationManager.notify(1, builder.build())
	}

}