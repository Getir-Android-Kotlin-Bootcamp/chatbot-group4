package com.getir.patika.chatapp

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.fake.FakeChatDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ChatRepositoryTest {
    private lateinit var chatRepository: ChatRepository

    @Before
    fun setUp() {
        chatRepository = FakeChatDataSource()
    }

    @Test
    fun `sendMessage should add message to repository`() = runTest {
        val messageContent = "Hello, World!"

        val result = chatRepository.sendMessage(messageContent)

        assert(result.isSuccess)
        val messagesResult = chatRepository.getAllMessages()
        assert(messagesResult.isSuccess)
        val messagesFlow = messagesResult.getOrThrow()
        val messages = messagesFlow.first()
        assertEquals(1, messages.size)
        assertEquals(messageContent, messages.first().message)
    }

    @Test
    fun `getAllMessages should return all messages`() = runTest {
        val messageContents = listOf("Hello", "World", "Test")
        messageContents.forEach { chatRepository.sendMessage(it) }

        val result = chatRepository.getAllMessages()

        assert(result.isSuccess)
        val messagesFlow = result.getOrThrow()
        val messages = messagesFlow.first()
        assertEquals(messageContents.size, messages.size)
        messageContents.forEachIndexed { index, content ->
            assertEquals(content, messages[index].message)
        }
    }

    @Test
    fun `getAllMessages should return empty list when no messages`() = runTest {
        val result = chatRepository.getAllMessages()

        assert(result.isSuccess)

        val messagesFlow = result.getOrThrow()
        val messages = messagesFlow.first()
        assertEquals(0, messages.size)
    }

    @Test
    fun `sendMessage should return error when repository fails`() = runTest {
        val fakeChatDataSource = FakeChatDataSource(shouldFail = true)
        val messageContent = "Hello, World!"

        val result = fakeChatDataSource.sendMessage(messageContent)

        assert(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("Failed to send message", exception?.message)
    }

    @Test
    fun `getAllMessages should return error when repository fails`() = runTest {
        val fakeChatDataSource = FakeChatDataSource(shouldFail = true)

        val result = fakeChatDataSource.getAllMessages()

        assert(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertEquals("Failed to retrieve messages", exception?.message)
    }
}
