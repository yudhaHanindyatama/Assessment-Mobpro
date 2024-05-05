package org.d3if3053.assessment.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if3053.assessment.model.Keuangan

@Database(entities = [Keuangan::class], version = 1, exportSchema = false)
abstract class KeuanganDb : RoomDatabase() {

    abstract val dao: KeuanganDao
    companion object {
        @Volatile
        private var INSTANCE: KeuanganDb? = null

        fun getInstance(context: Context): KeuanganDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        KeuanganDb::class.java,
                        "keuangan.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}