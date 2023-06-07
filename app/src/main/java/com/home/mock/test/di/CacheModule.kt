package com.home.mock.test.di

import android.content.Context
import androidx.room.Room
import com.home.mock.test.data.cache.database.HomeMockTestDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HomeMockTestDatabase =
        Room
            .databaseBuilder(
                context,
                HomeMockTestDatabase::class.java,
                HomeMockTestDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFavoriteDao(database: HomeMockTestDatabase) =
        database.favoriteDao

    @Provides
    @Singleton
    fun provideUserDao(database: HomeMockTestDatabase) =
        database.userDao
}
