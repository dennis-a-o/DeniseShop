package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiCoupon
import com.example.deniseshop.ui.models.UiCoupon
import javax.inject.Inject

class CouponListApiToUiMapper @Inject constructor(): BaseListMapper<ApiCoupon, UiCoupon> {
    override fun map(input: List<ApiCoupon>): List<UiCoupon> {
        return input.map {
            UiCoupon(
                id = it.id,
                code = it.code,
                value = it.value,
                type = it.type,
                description = it.description
            )
        }
    }
}