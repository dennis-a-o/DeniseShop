package com.example.deniseshop.ui.mapper

import com.example.deniseshop.data.models.ApiFaq
import com.example.deniseshop.ui.models.UiFaq
import javax.inject.Inject

class FaqListApiToUiMapper @Inject constructor(): BaseListMapper<ApiFaq, UiFaq> {
    override fun map(input: List<ApiFaq>): List<UiFaq> {
        return input.map {
            UiFaq(
                id = it.id,
                question = it.question,
                answer = it.answer
            )
        }
    }
}