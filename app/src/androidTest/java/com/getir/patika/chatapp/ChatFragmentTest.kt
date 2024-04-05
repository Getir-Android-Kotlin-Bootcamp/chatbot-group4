package com.getir.patika.chatapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ChatFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun buttons_shouldBeDisplayedAndClickable() {
        onView(withId(R.id.btn_back))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))

        onView(withId(R.id.btn_robot))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_ring))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
    }


    @Test
    fun sendMessage_WhenMessageIsTyped() {
        val message = "Hello, Espresso!"

        onView(withId(R.id.editTextMessage))
            .perform(typeText(message), closeSoftKeyboard())

        onView(withId(R.id.sendButton))
            .perform(click())

        onView(withId(R.id.rv_messages))
            .check(matches(hasMessage(message)))
    }
}

fun hasMessage(expectedMessage: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("Has message with text: $expectedMessage")
        }

        override fun matchesSafely(recycleView: View?): Boolean {
            if (recycleView !is RecyclerView) return false
            val adapter = recycleView.adapter ?: return false

            for (pos in 0 until adapter.itemCount) {
                val viewHolder = recycleView.findViewHolderForAdapterPosition(pos)
                val textView = viewHolder?.itemView?.findViewById<TextView>(R.id.tv_bubble_user)
                if (textView?.text.toString() == expectedMessage) {
                    return false
                }
            }
            return false
        }
    }
}
