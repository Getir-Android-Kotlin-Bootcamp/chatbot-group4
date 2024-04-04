package com.getir.patika.chatapp.ext

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


fun View.keyboardVisibilityFlow(): Flow<Pair<Boolean, Int>> = callbackFlow {
    val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        getWindowVisibleDisplayFrame(rect)
        val screenHeight = rootView.height
        val keypadHeight = screenHeight - rect.bottom
        val isKeyboardVisible = keypadHeight > screenHeight * 0.15
        trySend(isKeyboardVisible to keypadHeight)
    }

    viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

    awaitClose {
        viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}

fun View.makeToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
