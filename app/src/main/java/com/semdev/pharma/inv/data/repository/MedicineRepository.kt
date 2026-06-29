package com.semdev.pharma.inv.data.repository

import com.semdev.pharma.inv.data.local.AppDatabase
import com.semdev.pharma.inv.data.local.MedicineEntity
import com.semdev.pharma.inv.data.remote.RetrofitClient

class MedicineRepository(private val database: AppDatabase) {

    private val dao = database.medicineDao()
    private val api = RetrofitClient.apiService

    sealed class Result {
        data class Success(val data: List<MedicineEntity>) : Result()
        data class Error(val message: String) : Result()
    }

    suspend fun getMedicines(): Result {
        return try {
            val response = api.getMedicines()
            val entities = response.map { it.toEntity() }
            dao.deleteAll()
            dao.insertAll(entities)
            Result.Success(entities)
        } catch (e: Exception) {
            val cached = dao.getAll()
            if (cached.isNotEmpty()) {
                Result.Success(cached)
            } else {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getCachedMedicines(): List<MedicineEntity> {
        return dao.getAll()
    }
}
