package com.home.mock.test.di

import com.home.mock.test.data.repository.FavoriteRepositoryImpl
import com.home.mock.test.data.repository.UserRepositoryImpl
import com.home.mock.test.domain.repository.FavoriteRepository
import com.home.mock.test.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    @ViewModelScoped
    fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    @ViewModelScoped
    fun bindFavoriteRepository(repository: FavoriteRepositoryImpl): FavoriteRepository
}
