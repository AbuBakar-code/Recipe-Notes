package com.example.recipenotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipenotes.dataClasses.Meal

@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase: RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object{
        @Volatile
        var INSTANCE: MealDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MealDatabase {
          if (INSTANCE == null){
              INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            MealDatabase::class.java,
                            "meal_database"
                        ).fallbackToDestructiveMigration(false).build()
          }
            return INSTANCE as MealDatabase
        }
    }
}