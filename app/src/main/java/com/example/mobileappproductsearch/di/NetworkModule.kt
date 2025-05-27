package com.example.testwireless.di

import com.example.mobileappproductsearch.domain.repository.AuthRepository
import com.example.mobileappproductsearch.data.repository.AuthRepositoryImpl
import com.example.mobileappproductsearch.data.repository.CategoryRepositoryImpl
import com.example.mobileappproductsearch.data.repository.ProductSearchRepositoryImpl
import com.example.mobileappproductsearch.domain.repository.CategoryRepository
import com.example.mobileappproductsearch.domain.repository.ProductSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindSearchProductRepository(
        impl: ProductSearchRepositoryImpl
    ): ProductSearchRepository

    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

}
