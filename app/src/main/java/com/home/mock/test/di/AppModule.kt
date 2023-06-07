package com.home.mock.test.di

import com.home.mock.test.utils.DefaultDispatchers
import com.home.mock.test.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDispatchers(): DispatcherProvider =
        DefaultDispatchers
}
