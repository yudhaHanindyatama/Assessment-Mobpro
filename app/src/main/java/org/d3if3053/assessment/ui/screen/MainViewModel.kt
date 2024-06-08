package org.d3if3053.assessment.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.d3if3053.assessment.database.KeuanganDao

class MainViewModel(dao: KeuanganDao) : ViewModel() {
    val data: StateFlow<List<Keuangan>> = dao.getKeuangan().stateIn(
        scope =viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}