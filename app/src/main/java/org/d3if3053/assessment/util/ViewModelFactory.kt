package org.d3if3053.assessment.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3053.assessment.database.KeuanganDao
import org.d3if3053.assessment.ui.screen.DetailViewModel
import org.d3if3053.assessment.ui.screen.MainViewModel

class ViewModelFactory (
    private val dao: KeuanganDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((MainViewModel::class.java))) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom((DetailViewModel::class.java))) {
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}