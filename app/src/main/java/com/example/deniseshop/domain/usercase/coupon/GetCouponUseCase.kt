package com.example.deniseshop.domain.usercase.coupon

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.UiCoupon

interface GetCouponUseCase {
    operator fun invoke(): PagingSource<Int, UiCoupon>
}