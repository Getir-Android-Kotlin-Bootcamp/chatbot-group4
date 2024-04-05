package com.getir.patika.chatapp

import com.getir.patika.chatapp.fake.FakeChatDataSource
import com.getir.patika.chatapp.ui.ChatViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ChatViewModelTest {
    private lateinit var chatRepository: FakeChatDataSource
    private lateinit var chatViewModel: ChatViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        chatRepository = FakeChatDataSource(shouldFail = false)
        chatViewModel = ChatViewModel(chatRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when onSend is called, message should be saved and sent`() = runTest {
        val testMessage = "Test Message"

        chatViewModel.onSend(testMessage)

        val messages = chatViewModel.messages.first()
        assertTrue(
            "Message list should contain the sent message",
            messages.any { it.message == testMessage })
    }

    @Test
    fun `when onSend is called with failure, error should be logged`() = runTest {
        chatRepository = FakeChatDataSource(shouldFail = true)
        chatViewModel = ChatViewModel(chatRepository)

        val testMessage = "Test Message"

        chatViewModel.onSend(testMessage)

        val messages = chatViewModel.messages.first()
        assertFalse(
            "Message list should not contain the sent message on failure",
            messages.any { it.message == testMessage })
    }

    @Test
    fun `when getFirstMessageState is called, initial message should be added`() = runTest {
        chatViewModel.getFirstMessageState()

        val messages = chatViewModel.messages.first()
        assertTrue(
            "Message list should contain the initial message",
            messages.any { it.message == "Hello, how can I help you?" })
    }

    @Test
    fun `when onQueryChanged is called, UI state should update query`() = runTest {
        val testQuery = "New query"

        chatViewModel.onQueryChanged(testQuery)

        val uiState = chatViewModel.uiState.first()
        assertEquals(
            "UI state should update with the new query",
            testQuery,
            uiState.query
        )
    }

    @Test
    fun `initial UI state should be correct`() = runTest {
        val initialState = chatViewModel.uiState.first()

        assertTrue(
            "Initial query should be empty",
            initialState.query.isEmpty()
        )
        assertTrue(
            "Text field should be initially enabled",
            initialState.isTextFieldEnabled
        )
    }
}
