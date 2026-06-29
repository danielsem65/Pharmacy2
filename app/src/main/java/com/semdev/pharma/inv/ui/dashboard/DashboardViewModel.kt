package com.semdev.pharma.inv.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.semdev.pharma.inv.PharmacyApp
import com.semdev.pharma.inv.data.Medicine
import com.semdev.pharma.inv.data.repository.MedicineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedicineRepository = (application as PharmacyApp).repository

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allMedicines: List<Medicine> = emptyList()

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val medicines: List<Medicine>) : UiState()
        data class Error(val message: String) : UiState()
        data class Offline(val medicines: List<Medicine>) : UiState()
    }

    init {
        loadMedicines()
    }

    fun loadMedicines() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val cached = repository.getCachedMedicines()
            if (cached.isNotEmpty()) {
                allMedicines = cached
                _uiState.value = UiState.Offline(cached)
            }

            when (val result = repository.getMedicines()) {
                is MedicineRepository.Result.Success -> {
                    allMedicines = result.data
                    _uiState.value = UiState.Success(result.data)
                }
                is MedicineRepository.Result.Error -> {
                    if (cached.isEmpty()) {
                        _uiState.value = UiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filterMedicines(query)
    }

    private fun filterMedicines(query: String) {
        val filtered = if (query.isBlank()) {
            allMedicines
        } else {
            allMedicines.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        when (val current = _uiState.value) {
            is UiState.Success -> _uiState.value = current.copy(medicines = filtered)
            is UiState.Offline -> _uiState.value = current.copy(medicines = filtered)
            else -> {}
        }
    }
}
