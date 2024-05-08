package org.d3if3053.assessment.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3053.assessment.database.KeuanganDao
import org.d3if3053.assessment.model.Keuangan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: KeuanganDao) : ViewModel() {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(pilihan: String, deskripsi: String, jumlahUang: String) {
        val keuangan = Keuangan(
            tanggal = formatter.format(Date()),
            pilihan = pilihan,
            deskripsi = deskripsi,
            jumlahUang = jumlahUang
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(keuangan)
        }
    }

    suspend fun getKeuangan(id: Long): Keuangan? {
        return dao.getKeuanganById(id)
    }

    fun update(id: Long, pilihan: String, deskripsi: String, jumlahUang: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingKeuangan = dao.getKeuanganById(id)
            existingKeuangan?.let {
                val keuangan = Keuangan(
                    id = id,
                    tanggal = it.tanggal,
                    pilihan = pilihan,
                    deskripsi = deskripsi,
                    jumlahUang = jumlahUang
                )
                dao.update(keuangan)
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}