package com.example.androidinterview

import com.example.androidinterview.domain.model.Currency
import com.example.androidinterview.domain.model.Merchant
import com.example.androidinterview.domain.repository.MerchantRepository
import com.example.androidinterview.ui.home.HomeUiState
import com.example.androidinterview.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val fakeMerchant = Merchant(
        availableBalance = 500000,
        pendingBalance = 25000,
        currency = Currency.GBP,
        activity = emptyList(),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val vm = HomeViewModel(FakeMerchantRepository(Result.success(fakeMerchant)))
        // Coroutine is scheduled but not yet run with StandardTestDispatcher
        assertEquals(HomeUiState.Loading, vm.uiState.value)
    }

    @Test
    fun `state is Success after successful load`() = runTest {
        val vm = HomeViewModel(FakeMerchantRepository(Result.success(fakeMerchant)))
        advanceUntilIdle()
        assertEquals(HomeUiState.Success(fakeMerchant), vm.uiState.value)
    }

    @Test
    fun `state is Error when repository throws`() = runTest {
        val vm = HomeViewModel(FakeMerchantRepository(Result.failure(IOException("no network"))))
        advanceUntilIdle()
        val state = vm.uiState.value
        assertTrue("Expected Error state but got $state", state is HomeUiState.Error)
        assertEquals("no network", (state as HomeUiState.Error).message)
    }
}

private class FakeMerchantRepository(private val result: Result<Merchant>) : MerchantRepository {
    override suspend fun getMerchant(): Result<Merchant> = result
}
