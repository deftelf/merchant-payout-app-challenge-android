package com.example.androidinterview.ui.activity

import androidx.lifecycle.ViewModel
import com.example.androidinterview.domain.model.Merchant
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = RecentActivityViewModelFactory::class)
class RecentActivityViewModel @AssistedInject constructor(
    @Assisted merchant: Merchant,
    mapper: RecentActivityUiModelMapper,
) : ViewModel() {

    val uiState: StateFlow<RecentActivityUiState> = MutableStateFlow(mapper(merchant)).asStateFlow()
}

@AssistedFactory
interface RecentActivityViewModelFactory {
    fun create(merchant: Merchant): RecentActivityViewModel
}
