package com.tourtrek.activities;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.tourtrek.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TourMarketSpinnerTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void tourMarketSpinnerTest() {
//        ViewInteraction bottomNavigationItemView = onView(
//                allOf(withId(R.id.navigation_tour_market), withContentDescription("Tour Market"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_view),
//                                        0),
//                                0),
//                        isDisplayed()));
//        bottomNavigationItemView.perform(click());
//
//        ViewInteraction bottomNavigationItemView2 = onView(
//                allOf(withId(R.id.navigation_tours), withContentDescription("Tours"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_view),
//                                        0),
//                                1),
//                        isDisplayed()));
//        bottomNavigationItemView2.perform(click());
//
//        ViewInteraction bottomNavigationItemView3 = onView(
//                allOf(withId(R.id.navigation_profile), withContentDescription("Profile"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_view),
//                                        0),
//                                2),
//                        isDisplayed()));
//        bottomNavigationItemView3.perform(click());
//
//        ViewInteraction bottomNavigationItemView4 = onView(
//                allOf(withId(R.id.navigation_tours), withContentDescription("Tours"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_view),
//                                        0),
//                                1),
//                        isDisplayed()));
//        bottomNavigationItemView4.perform(click());
//
//        ViewInteraction bottomNavigationItemView5 = onView(
//                allOf(withId(R.id.navigation_tour_market), withContentDescription("Tour Market"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_view),
//                                        0),
//                                0),
//                        isDisplayed()));
//        bottomNavigationItemView5.perform(click());
//
//        ViewInteraction spinner = onView(
//                allOf(withId(R.id.tour_market_spinner),
//                        withParent(withParent(withId(R.id.nav_host_fragment))),
//                        isDisplayed()));
//        spinner.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
