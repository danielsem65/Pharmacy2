package com.semdev.pharma.inv.data.remote

import com.google.gson.annotations.SerializedName
import com.semdev.pharma.inv.data.local.MedicineEntity

data class MedicineDto(
    @SerializedName("Name") val name: String?,
    @SerializedName("Sales Price") val salesPrice: String?
) {
    fun toEntity(): MedicineEntity {
        return MedicineEntity(
            name = name ?: "Unknown",
            salesPrice = salesPrice ?: "0.00"
        )
    }
}
