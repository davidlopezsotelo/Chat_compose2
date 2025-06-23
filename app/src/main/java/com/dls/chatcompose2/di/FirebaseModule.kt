package com.dls.chatcompose2.di

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Módulo de Hilt que proporciona instancias singleton de los servicios de Firebase
/**
 * Este módulo se utiliza para proporcionar instancias singleton de los servicios de Firebase a través de Hilt.
 * Cada vez que Hilt necesita una instancia de un servicio, como FirebaseAuth, FirebaseFirestore, etc., se proporcionará
 * esta instancia singleton.
 */

/**
 * Módulo de Hilt que proporciona instancias singleton de los servicios de Firebase.
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    private const val TAG = "FirebaseModule"

    /**
     * Provee la instancia de FirebaseAuth (Autenticación)
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        val instance = FirebaseAuth.getInstance()
        Log.d(TAG, "✅ FirebaseAuth proporcionado: ${instance.app.name}")
        return instance
    }

    /**
     * Provee la instancia de FirebaseFirestore (Base de datos no relacional de documentos)
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        val instance = FirebaseFirestore.getInstance()
        Log.d(TAG, "✅ FirebaseFirestore proporcionado: ${instance.app.name}")
        return instance
    }

    /**
     * Provee la instancia de FirebaseDatabase (Realtime Database, ideal para chat)
     */
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        val instance = FirebaseDatabase.getInstance()
        Log.d(TAG, "✅ FirebaseDatabase proporcionado: ${instance.app.name}")
        return instance
    }

    /**
     * Provee la instancia de FirebaseStorage (almacenamiento multimedia)
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        val instance = FirebaseStorage.getInstance()
        Log.d(TAG, "✅ FirebaseStorage proporcionado: ${instance.app.name}")
        return instance
    }
}



