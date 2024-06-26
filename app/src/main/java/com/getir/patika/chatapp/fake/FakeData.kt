package com.getir.patika.chatapp.fake

import com.getir.patika.chatapp.data.model.MessageEntity
import com.getir.patika.chatapp.data.model.Role
import com.getir.patika.chatapp.data.model.toMessage
import java.util.UUID
import kotlin.random.Random

/**
 * List of fake [MessageEntity] objects for testing purposes.
 */
val FakeMessageEntities = List(20) { createRandomMessageEntity() }

/**
 * List of fake [Message] objects converted from [FakeMessageEntities] for testing purposes.
 */
val FakeMessages = FakeMessageEntities.map(MessageEntity::toMessage)

/**
 * Creates a random [MessageEntity] object.
 *
 * @return A random [MessageEntity].
 */
fun createRandomMessageEntity(): MessageEntity {
    val messageId = UUID.randomUUID().toString()
    val role = if (Random.nextBoolean()) Role.MODEL else Role.USER
    val message = listOf(
        "Hello, how are you?",
        "What's the weather like today?",
        "I'm an AI, ask me anything!",
        "Do you like music?",
        "I'm a chatbot, how can I help you?",
    ).random()
    val timestamp = System.currentTimeMillis()

    return MessageEntity(
        messageId = messageId,
        role = role,
        message = message,
        timestamp = timestamp,
        isLoaded = true
    )
}
