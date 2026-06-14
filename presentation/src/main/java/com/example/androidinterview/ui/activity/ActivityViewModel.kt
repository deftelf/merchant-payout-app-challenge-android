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

@HiltViewModel(assistedFactory = ActivityViewModelFactory::class)
class ActivityViewModel @AssistedInject constructor(
    @Assisted merchant: Merchant,
    mapper: ActivityUiModelMapper,
) : ViewModel() {

    val uiState: StateFlow<ActivityUiState> = MutableStateFlow(mapper(merchant)).asStateFlow()
}

@AssistedFactory
interface ActivityViewModelFactory {
    fun create(merchant: Merchant): ActivityViewModel
}
