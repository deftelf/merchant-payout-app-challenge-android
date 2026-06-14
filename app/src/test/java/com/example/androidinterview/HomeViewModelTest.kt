package com.example.androidinterview

import com.example.androidinterview.data.model.Currency
import com.example.androidinterview.data.model.MerchantData
import com.example.androidinterview.data.network.MerchantApi
import com.example.androidinterview.data.repository.MerchantRepository
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

    private val fakeData = MerchantData(
        available_balance = 500000,
        pending_balance = 25000,
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

    private fun fakeRepo(api: MerchantApi) = MerchantRepository(api, testDispatcher)

    @Test
    fun `initial state is Loading`() = runTest {
        val fakeApi = object : MerchantApi {
            override suspend fun getMerchant() = fakeData
        }
        val vm = HomeViewModel(fakeRepo(fakeApi))
        // Coroutine is scheduled but not yet run with StandardTestDispatcher
        assertEquals(HomeUiState.Loading, vm.uiState.value)
    }

    @Test
    fun `state is Success after successful load`() = runTest {
        val fakeApi = object : MerchantApi {
            override suspend fun getMerchant() = fakeData
        }
        val vm = HomeViewModel(fakeRepo(fakeApi))
        advanceUntilIdle()
        assertEquals(HomeUiState.Success(fakeData), vm.uiState.value)
    }

    @Test
    fun `state is Error when repository throws`() = runTest {
        val fakeApi = object : MerchantApi {
            override suspend fun getMerchant(): MerchantData = throw IOException("no network")
        }
        val vm = HomeViewModel(fakeRepo(fakeApi))
        advanceUntilIdle()
        val state = vm.uiState.value
        assertTrue("Expected Error state but got $state", state is HomeUiState.Error)
        assertEquals("no network", (state as HomeUiState.Error).message)
    }
}
