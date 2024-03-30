package com.getir.patika.chatapp.data.di

import android.content.Context
import androidx.room.Room
import com.getir.patika.chatapp.data.room.ChatDao
import com.getir.patika.chatapp.data.room.ChatRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ChatRoomDatabase =
        Room.databaseBuilder(context, ChatRoomDatabase::class.java, DB_NAME).build()

    @Singleton
    @Provides
    fun provideChatDao(database: ChatRoomDatabase): ChatDao = database.chatDao()

    private const val DB_NAME = "chat"
}
