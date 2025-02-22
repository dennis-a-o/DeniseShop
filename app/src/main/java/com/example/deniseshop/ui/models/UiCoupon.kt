package com.example.deniseshop.ui.models

data class UiCoupon(
    val id: Long,
    val code: String,
    val value: Int,
    val type: String,
    val description: String?
)

