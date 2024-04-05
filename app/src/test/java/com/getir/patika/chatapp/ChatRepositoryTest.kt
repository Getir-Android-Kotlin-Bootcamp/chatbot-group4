package com.getir.patika.chatapp

import com.getir.patika.chatapp.data.ChatRepository
import com.getir.patika.chatapp.fake.FakeChatDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ChatRepository].
 */
class ChatRepositoryTest {
    private lateinit var chatRepository: ChatRepository

    /**
     * Set up the test environment before each test.
     */
    @Before
    fun setUp() {
        chatRepository = FakeChatDataSource()
    }

    /**
     * Test to verify that sending a message adds it to the repository.
     */
    @Test
    fun `sendMessage should add message to repository`() = runTest {
        val messageContent = "Hello, World!"

        val result = chatRepository.sendMessage(messageContent)

        assert(result.isSuccess)
        val messagesResult = chatRepository.getAllMessages()
        val messages = messagesResult.first()
        assertEquals(1, messages.size)
        assertEquals(messageContent, messages.first().message)
    }

    /**
     * Test to verify that getAllMessages returns all messages in the repository.
     */
    @Test
    fun `getAllMessages should return all messages`() = runTest {
        val messageContents = listOf("Hello", "World", "Test")
        messageContents.forEach { chatRepository.sendMessage(it) }

        val result = chatRepository.getAllMessages()

        val messages = result.first()
        assertEquals(messageContents.size, messages.size)
        messageContents.forEachIndexed { index, content ->
            assertEquals(content, messages[index].message)
        }
    }

    /**
     * Test to verify that getAllMessages returns an empty list when there are no messages.
     */
    @Test
    fun `getAllMessages should return empty list when no messages`() = runTest {
        val result = chatRepository.getAllMessages()

        val messages = result.first()
        assertEquals(0, messages.size)
    }

    /**
     * Test to verify that sendMessage returns an error when the repository fails.
     */
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
}
