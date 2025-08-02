package com.example.cashflow.di

import android.content.Context
import androidx.room.Room
import com.example.cashflow.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cashflow_db"
        )
        // Eliminamos la pre-población para evitar ciclos de dependencia en la compilación.
        // La gestión de datos iniciales se puede manejar de otra forma si es necesario.
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    fun provideBudgetDao(db: AppDatabase): BudgetDao = db.budgetDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    // Los repositorios no necesitan @Provides si usan @Inject en su constructor.
    // Hilt sabe cómo crearlos automáticamente.
}
