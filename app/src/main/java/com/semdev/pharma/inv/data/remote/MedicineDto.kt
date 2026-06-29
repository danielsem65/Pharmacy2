package com.semdev.pharma.inv.data.remote

import com.google.gson.annotations.SerializedName
import com.semdev.pharma.inv.data.Medicine

data class MedicineDto(
    @SerializedName("Name") val name: String?,
    @SerializedName("Sales Price") val salesPrice: String?
) {
    fun toMedicine(): Medicine {
        return Medicine(
            name = name ?: "Unknown",
            salesPrice = salesPrice ?: "0.00"
        )
    }
}
