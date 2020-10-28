package com.tourtrek

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tourtrek.fragments.LoginFragment
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith(AndroidJUnit4::class)
class MyTestSuite {

    @Test
    fun testEventFragment() {

//        val scenario = launchFragmentInContainer<LoginFragment>()
//        onView(withId(R.id.text)).check(matches(withText("Hello World!")))
    }
}