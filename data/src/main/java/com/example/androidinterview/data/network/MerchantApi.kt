package com.example.androidinterview.data.network

import com.example.androidinterview.data.model.MerchantDto
import com.example.androidinterview.data.model.PaginatedActivityResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

internal interface MerchantApi {
    @GET("api/merchant")
    suspend fun getMerchant(): MerchantDto

    @GET("api/merchant/activity")
    suspend fun getActivity(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 15,
    ): PaginatedActivityResponseDto
}
