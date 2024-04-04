package com.getir.patika.chatapp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.getir.patika.chatapp.data.model.Message
import com.getir.patika.chatapp.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT 10")
    suspend fun getLastMessages(): List<MessageEntity>

    @Insert
    suspend fun insertMessage(message: MessageEntity): Long

    @Query("UPDATE messages SET message = :message, isLoaded = :isLoaded WHERE id = :messageId")
    suspend fun updateMessage(messageId: Int, message: String, isLoaded: Boolean)

    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Int)

    @Transaction
    suspend fun insertModelAndUserMessage(userMessage: MessageEntity, modelMessage: MessageEntity) {
        insertMessage(userMessage)
        insertMessage(modelMessage)
    }
}
