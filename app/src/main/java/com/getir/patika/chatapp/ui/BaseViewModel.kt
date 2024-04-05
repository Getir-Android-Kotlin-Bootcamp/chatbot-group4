package com.getir.patika.chatapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Base class for a ViewModel that provides a common coroutine launch mechanism
 * with exception handling.
 *
 * This class extends [ViewModel] and should be used as a base class for other ViewModels
 * to inherit common behavior.
 */
open class BaseViewModel : ViewModel() {

    /**
     * Launches a new coroutine in the [viewModelScope] and catches any exceptions
     * that are thrown in the coroutine block.
     *
     * @param block The suspend lambda block to be executed within the coroutine.
     *              It is a suspending function that will be scoped to [CoroutineScope].
     *              Any uncaught exceptions in this block will be handled by the
     *              [CoroutineExceptionHandler].
     */
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                // for debug purposes
                println("An error occurred: $throwable")
            },
            block = block
        )
}
