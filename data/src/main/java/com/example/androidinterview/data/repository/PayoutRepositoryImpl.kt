package com.example.androidinterview.data.repository

import com.example.androidinterview.data.device.DeviceIdProvider
import com.example.androidinterview.data.di.IoDispatcher
import com.example.androidinterview.data.model.PayoutRequestDto
import com.example.androidinterview.data.model.toDomain
import com.example.androidinterview.data.model.toDto
import com.example.androidinterview.data.network.RetrofitClient
import com.example.androidinterview.domain.model.Currency
import com.example.androidinterview.domain.model.Payout
import com.example.androidinterview.domain.repository.PayoutRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class PayoutRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val deviceIdProvider: DeviceIdProvider,
) : PayoutRepository {

    private val api = RetrofitClient.merchantApi

    override suspend fun createPayout(amount: Int, currency: Currency, iban: String): Result<Payout> =
        withContext(ioDispatcher) {
            runCatching {
                val deviceId = deviceIdProvider.getDeviceId()
                api.createPayout(
                    PayoutRequestDto(
                        amount = amount,
                        currency = currency.toDto(),
                        iban = iban,
                        deviceId = deviceId,
                    )
                ).toDomain()
            }
        }
}
