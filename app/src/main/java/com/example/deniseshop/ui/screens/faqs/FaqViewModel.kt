package com.example.deniseshop.ui.screens.faqs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.deniseshop.domain.usercase.faq.GetFaqUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    private val getFaqUseCase: GetFaqUseCase
): ViewModel() {

    val pager = Pager(PagingConfig(pageSize = 20, initialLoadSize = 20)){
        getFaqUseCase()
    }.flow.cachedIn(viewModelScope)

}