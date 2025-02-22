package com.example.deniseshop.domain.usercase.faq

import androidx.paging.PagingSource
import com.example.deniseshop.data.repository.ApiRepository
import com.example.deniseshop.ui.models.UiFaq
import javax.inject.Inject

class GetFaqUseCaseImpl @Inject constructor(
    private val apiRepository: ApiRepository
): GetFaqUseCase {
    override fun invoke(): PagingSource<Int, UiFaq> {
        return apiRepository.getFaqs()
    }
}