package com.example.news.di

import android.content.Context
import androidx.room3.Room
import com.example.news.data.local.NewsDao
import com.example.news.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    companion object {
        @Singleton
        @Provides
        fun provideNewsDatabase(
            @ApplicationContext context: Context
        ): NewsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = NewsDatabase::class.java,
                name = "news.db",
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        @Singleton
        @Provides
        fun provideNewsDao(
            database: NewsDatabase
        ): NewsDao = database.newsDao()
    }
}