package com.getir.patika.chatapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.getir.patika.chatapp.data.model.MessageEntity

@Database(entities = [MessageEntity::class], version = 1)
abstract class ChatRoomDatabase: RoomDatabase() {
    abstract fun chatDao(): ChatDao
}
