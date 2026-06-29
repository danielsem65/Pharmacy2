package com.semdev.pharma.inv

import android.app.Application
import com.semdev.pharma.inv.data.local.AppDatabase
import com.semdev.pharma.inv.data.repository.MedicineRepository

class PharmacyApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: MedicineRepository by lazy { MedicineRepository(database) }
}
