package com.example.androidinterview.data.network

import com.example.androidinterview.data.model.MerchantData
import retrofit2.http.GET

interface MerchantApi {
    @GET("api/merchant")
    suspend fun getMerchant(): MerchantData
}
