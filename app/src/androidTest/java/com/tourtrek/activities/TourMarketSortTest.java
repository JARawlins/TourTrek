package com.tourtrek.activities;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.tourtrek.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tourtrek.EspressoExtensions.waitId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TourMarketSortTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void tourMarketSortTest() {
//        ViewInteraction bottomNavigationItemView = onView(
//                allOf(withId(R.id.navigation_tour_market), withContentDescription("Tour Market"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_view),
//                                        0),
//                                0),
//                        isDisplayed()));
//        bottomNavigationItemView.perform(click());
//        onView(isRoot()).perform(waitId(R.id.tour_market_spinner, TimeUnit.SECONDS.toMillis(1000)));
//        ViewInteraction appCompatSpinner = onView(
//                allOf(withId(R.id.tour_market_spinner),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_host_fragment),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatSpinner.perform(click());
//
//        DataInteraction appCompatCheckedTextView = onData(anything())
//                .inAdapterView(childAtPosition(
//                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
//                        0))
//                .atPosition(1);
//        appCompatCheckedTextView.perform(click());
//
//        onView(isRoot()).perform(waitId(R.id.tour_market_spinner, TimeUnit.SECONDS.toMillis(1000)));
//        ViewInteraction appCompatSpinner2 = onView(
//                allOf(withId(R.id.tour_market_spinner),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_host_fragment),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatSpinner2.perform(click());
//
//        DataInteraction appCompatCheckedTextView2 = onData(anything())
//                .inAdapterView(childAtPosition(
//                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
//                        0))
//                .atPosition(3);
//        appCompatCheckedTextView2.perform(click());
//
//
//        onView(isRoot()).perform(waitId(R.id.tour_market_spinner, TimeUnit.SECONDS.toMillis(1000)));
//        ViewInteraction appCompatSpinner3 = onView(
//                allOf(withId(R.id.tour_market_spinner),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.nav_host_fragment),
//                                        0),
//                                2),
//                        isDisplayed()));
//        appCompatSpinner3.perform(click());
//
//        DataInteraction appCompatCheckedTextView3 = onData(anything())
//                .inAdapterView(childAtPosition(
//                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
//                        0))
//                .atPosition(5);
//        appCompatCheckedTextView3.perform(click());
//
//        ViewInteraction textView = onView(
//                allOf(withId(android.R.id.text1), withText("Location Descending"),
//                        withParent(allOf(withId(R.id.tour_market_spinner),
//                                withParent(IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class)))),
//                        isDisplayed()));
//        textView.check(matches(withText("Location Descending")));
//    }
//
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
    }
}
