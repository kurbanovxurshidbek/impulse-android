package com.impulse.impulse.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.impulse.impulse.database.dao.ContactDao
import com.impulse.impulse.model.Contact
import com.impulse.impulse.model.ContactsConverter

@Database(entities = [Contact::class], version = 1)
@TypeConverters(ContactsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ContactDao(): ContactDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "contact.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return instance!!
        }
    }
}