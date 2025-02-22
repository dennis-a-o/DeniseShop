package com.example.deniseshop.domain.usercase.coupon

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.UiCoupon
import javax.inject.Inject

class GetCouponUseCaseImpl @Inject constructor(
    private val apiRepository: ApiRepository
): GetCouponUseCase {
    override fun invoke(): PagingSource<Int, UiCoupon> {
        return apiRepository.getCoupons()
    }
}