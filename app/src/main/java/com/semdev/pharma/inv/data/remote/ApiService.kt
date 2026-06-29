package com.semdev.pharma.inv.data.remote

import retrofit2.http.GET

interface ApiService {

    @GET("public/5b34b860-5791-454a-9838-c30b35d3883f")
    suspend fun getMedicines(): List<MedicineDto>
}
