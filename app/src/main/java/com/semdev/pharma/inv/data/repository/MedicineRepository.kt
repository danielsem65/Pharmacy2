package com.semdev.pharma.inv.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.semdev.pharma.inv.data.Medicine
import com.semdev.pharma.inv.data.remote.RetrofitClient
import java.io.File

class MedicineRepository(private val context: Context) {

    private val api = RetrofitClient.apiService
    private val gson = Gson()

    private val cacheFile: File
        get() = File(context.filesDir, "pharma_cache.json")

    sealed class Result {
        data class Success(val data: List<Medicine>) : Result()
        data class Error(val message: String) : Result()
    }

    suspend fun getMedicines(): Result {
        return try {
            val response = api.getMedicines()
            val medicines = response.map { it.toMedicine() }
            saveCache(medicines)
            Result.Success(medicines)
        } catch (e: Exception) {
            val cached = loadCache()
            if (cached.isNotEmpty()) {
                Result.Success(cached)
            } else {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getCachedMedicines(): List<Medicine> {
        return loadCache()
    }

    private fun saveCache(medicines: List<Medicine>) {
        try {
            cacheFile.writeText(gson.toJson(medicines))
        } catch (_: Exception) {
        }
    }

    private fun loadCache(): List<Medicine> {
        return try {
            if (!cacheFile.exists()) return emptyList()
            val json = cacheFile.readText()
            if (json.isBlank()) return emptyList()
            val type = object : TypeToken<List<Medicine>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }
}
