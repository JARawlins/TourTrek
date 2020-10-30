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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_tour_market), withContentDescription("Tour Market"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.tour_market_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_tour_name), withText("6"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView.check(matches(withText("6")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.item_tour_name), withText("6"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView2.check(matches(withText("6")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.item_tour_name), withText("Brighton Snowboard"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView3.check(matches(withText("Brighton Snowboard")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.item_tour_name), withText("Capital"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView4.check(matches(withText("Capital")));

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.tour_market_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.item_tour_location), withText("Brighton, UT"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView5.check(matches(withText("Brighton, UT")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.item_tour_location), withText("Janesville"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView6.check(matches(withText("Janesville")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.item_tour_location), withText("Washington D.C."),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView7.check(matches(withText("Washington D.C.")));

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.tour_market_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner3.perform(click());

        DataInteraction appCompatCheckedTextView3 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView3.perform(click());

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.item_tour_name), withText("Madison"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView8.check(matches(withText("Madison")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.item_tour_name), withText("Capital"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView9.check(matches(withText("Capital")));

        ViewInteraction appCompatSpinner4 = onView(
                allOf(withId(R.id.tour_market_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner4.perform(click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(4);
        appCompatCheckedTextView4.perform(click());

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.item_tour_name), withText("Madison"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView10.check(matches(withText("Madison")));

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.item_tour_name), withText("Capital"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView11.check(matches(withText("Capital")));

        ViewInteraction appCompatSpinner5 = onView(
                allOf(withId(R.id.tour_market_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner5.perform(click());

        DataInteraction appCompatCheckedTextView5 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(5);
        appCompatCheckedTextView5.perform(click());

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.item_tour_location), withText("WIsconsin"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView12.check(matches(withText("WIsconsin")));

        ViewInteraction textView13 = onView(
                allOf(withId(R.id.item_tour_location), withText("Washington D.C."),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView13.check(matches(withText("Washington D.C.")));

        ViewInteraction textView14 = onView(
                allOf(withId(R.id.item_tour_location), withText("Janesville"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView14.check(matches(withText("Janesville")));

        ViewInteraction appCompatSpinner6 = onView(
                allOf(withId(R.id.tour_market_spinner),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                2),
                        isDisplayed()));
        appCompatSpinner6.perform(click());

        DataInteraction appCompatCheckedTextView6 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(6);
        appCompatCheckedTextView6.perform(click());

        ViewInteraction textView15 = onView(
                allOf(withId(R.id.item_tour_name), withText("6"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView15.check(matches(withText("6")));

        ViewInteraction textView16 = onView(
                allOf(withId(R.id.item_tour_name), withText("Brighton Snowboard"),
                        withParent(allOf(withId(R.id.item_tour_container),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        textView16.check(matches(withText("Brighton Snowboard")));
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
