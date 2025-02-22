package com.example.deniseshop.domain.usercase.address

import com.example.deniseshop.common.state.NetworkResponseState
import kotlinx.coroutines.flow.Flow

interface SetAddressDefaultUseCase {
	operator fun invoke(id: Long): Flow<NetworkResponseState<String>>
}