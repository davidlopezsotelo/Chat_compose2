package com.dls.chatcompose2.di

import com.dls.chatcompose2.data.repository.AuthRepositoryImpl
import com.dls.chatcompose2.data.repository.UserRepositoryImpl
import com.dls.chatcompose2.domain.repository.AuthRepository
import com.dls.chatcompose2.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt para enlazar la implementación concreta de AuthRepository
 * con su interfaz en toda la app (Singleton)
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

}
