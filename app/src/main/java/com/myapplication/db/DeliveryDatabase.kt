package com.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapplication.model.DeliveryItem

@Database(entities = [DeliveryItem::class], version = 1)
abstract class DeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDAO

    companion object {
        @Volatile
        private var INSTANCE: DeliveryDatabase? = null

        fun getDatabase(context: Context): DeliveryDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context, DeliveryDatabase::class.java, "deliveryDB")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}