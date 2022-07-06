package uz.impulse.impulse.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.impulse.impulse.data.local.dao.ContactDao
import uz.impulse.impulse.data.local.dao.IllnessDao
import uz.impulse.impulse.data.local.dao.MessageDao
import uz.impulse.impulse.data.local.entity.Contact
import uz.impulse.impulse.data.local.entity.Message
import uz.impulse.impulse.data.remote.model.Illness


@Database(entities = [Contact::class, Message::class, Illness::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun messageDao(): MessageDao
    abstract fun illnessDao(): IllnessDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "contact_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}