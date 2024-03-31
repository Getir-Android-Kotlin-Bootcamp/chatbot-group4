package com.getir.patika.chatapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.getir.patika.chatapp.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT 10")
    suspend fun getLastMessages(): List<MessageEntity>

    @Insert
    suspend fun insertMessage(message: MessageEntity)
    @Transaction
    suspend fun insertModelAndUserMessage(userMessage: MessageEntity, modelMessage: MessageEntity) {
        insertMessage(userMessage)
        insertMessage(modelMessage)
    }
}
