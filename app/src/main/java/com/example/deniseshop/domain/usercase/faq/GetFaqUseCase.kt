package com.example.deniseshop.domain.usercase.faq

import androidx.paging.PagingSource
import com.example.deniseshop.ui.models.UiFaq

interface GetFaqUseCase {
    operator fun  invoke(): PagingSource<Int, UiFaq>
}