package com.example.androidinterview.data.network

import com.example.androidinterview.data.model.MerchantDto
import retrofit2.http.GET

internal interface MerchantApi {
    @GET("api/merchant")
    suspend fun getMerchant(): MerchantDto
}
