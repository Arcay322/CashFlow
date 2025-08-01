package com.example.cashflow.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cashflow.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        // Usamos Provider para evitar una dependencia circular
        categoryDaoProvider: Provider<CategoryDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cashflow_db"
        )
        .fallbackToDestructiveMigration()
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Usamos un scope de corutina para insertar datos en un hilo de fondo
                CoroutineScope(Dispatchers.IO).launch {
                    val categoryDao = categoryDaoProvider.get()
                    val defaultCategories = listOf(
                        Category(name = "Comida", icon = "fastfood", type = "Gasto"),
                        Category(name = "Transporte", icon = "directions_car", type = "Gasto"),
                        Category(name = "Salario", icon = "work", type = "Ingreso"),
                        Category(name = "Facturas", icon = "receipt", type = "Gasto"),
                        Category(name = "Ocio", icon = "sports_esports", type = "Gasto"),
                        Category(name = "Salud", icon = "healing", type = "Gasto")
                    )
                    defaultCategories.forEach { categoryDao.insertCategory(it) }
                }
            }
        })
        .build()
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()

    @Provides
    fun provideBudgetDao(db: AppDatabase) = db.budgetDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao()

    // Hilt ahora puede inyectar los repositorios directamente gracias al @Inject constructor,
    // por lo que no es estrictamente necesario proveerlos aquí.
}
