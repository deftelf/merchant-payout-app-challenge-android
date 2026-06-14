package com.example.androidinterview.data.repository

import com.example.androidinterview.data.model.MerchantData
import com.example.androidinterview.data.network.MerchantApi
import com.example.androidinterview.data.network.RetrofitClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MerchantRepository(
    private val api: MerchantApi = RetrofitClient.merchantApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun getMerchant(): Result<MerchantData> = withContext(ioDispatcher) {
        runCatching { api.getMerchant() }
    }
}
