package com.semdev.pharma.inv

import android.app.Application
import com.semdev.pharma.inv.data.repository.MedicineRepository

class PharmacyApp : Application() {

    lateinit var repository: MedicineRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = MedicineRepository(this)
    }
}
