package com.mytest.android.citiesapp.data.city

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CityEntity::class], version = CityDatabase.DATABASE_VERSION, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {

        private const val CITY_DATABASE = "city_database"
        const val DATABASE_VERSION = 2

        @Volatile
        private var INSTANCE: CityDatabase? = null

        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.delete(CityEntity.CITY_TABLE, null, null)
                database.execSQL("ALTER TABLE city_table ADD COLUMN lat REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE city_table ADD COLUMN lng REAL NOT NULL DEFAULT 0.0")
            }
        }

        fun getDatabase(context: Context): CityDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    CITY_DATABASE
                ).addMigrations(migration_1_2).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}