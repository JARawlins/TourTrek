package com.tourtrek;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.tourtrek.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tourtrek.EspressoExtensions.nestedScrollTo;
import static com.tourtrek.EspressoExtensions.waitForView;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class AddFriendToTourFragmentTest {
    public static final String TAG = "AddFriendToTourFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void setup() throws InterruptedException {

        mainActivityScenario = mainActivityScenarioRule.getScenario();

        // If any user is logged in, make sure to log them out
        if(MainActivity.user != null){
            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(1)));
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.profile_logout_btn, TimeUnit.SECONDS.toMillis(1)));
            onView(withId(R.id.profile_logout_btn)).perform(click());
        }
            onView(withId(R.id.navigation_tours)).perform(click());
            onView(withId(R.id.login_email_et)).perform(typeText("testingaccount@gmail.com"), ViewActions.closeSoftKeyboard());
            onView(withId(R.id.login_password_et)).perform(typeText("password"), ViewActions.closeSoftKeyboard());
            onView(withId(R.id.login_login_btn)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.personal_past_tours_rv, TimeUnit.SECONDS.toMillis(1000)));
            sleep(1000);
            onView(withId(R.id.navigation_tours)).perform(click());


    }

    /**
     *This test checks to make sure that you can click add friend to tour and then are able to search for a friend.
     * It does this by checking to make sure that the friend that was searched for appears on the screen with the correct name
     **/
    @Test
    public void canSearchForFriendToAdd(){

        //click on a tour
        onView(withId(R.id.personal_current_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //click on add friend to tour button
        onView(isRoot()).perform(waitForView(R.id.tour_add_friend_btn, TimeUnit.SECONDS.toMillis(1000)));
        onView(withId(R.id.tour_add_friend_btn)).perform(nestedScrollTo());
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.tour_add_friend_btn), withText("Add Friend"),
                        childAtPosition(
                                allOf(withId(R.id.tour_buttons_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                9)),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        //type in friend email to search
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_email_et, TimeUnit.SECONDS.toMillis(1000)));
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.add_friend_to_tour_email_et),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("michael@gmail.com"), closeSoftKeyboard());

        //click on search
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_friend_to_tour_search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton3.perform(click());

        //make sure friend that was searched for appears
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_friendName_tv, TimeUnit.SECONDS.toMillis(1000)));
        ViewInteraction textView = onView(
                allOf(withId(R.id.add_friend_to_tour_friendName_tv), withText("Michael"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Michael")));
    }

    /**
     *Checks to make sure someone not on the users friends list cant be added by them.
     **/
    @Test
    public void cantAddStrangerToTour(){

        //click on a tour
        onView(withId(R.id.personal_current_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //click on add friend to tour button
        onView(isRoot()).perform(waitForView(R.id.tour_add_friend_btn, TimeUnit.SECONDS.toMillis(1000)));
        onView(withId(R.id.tour_add_friend_btn)).perform(nestedScrollTo());
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.tour_add_friend_btn), withText("Add Friend"),
                        childAtPosition(
                                allOf(withId(R.id.tour_buttons_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                9)),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        //type in friend email to search
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_email_et, TimeUnit.SECONDS.toMillis(1000)));
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.add_friend_to_tour_email_et),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test2@gmail.com"), closeSoftKeyboard());

        //click on search
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_friend_to_tour_search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton3.perform(click());

        //make sure error message appears
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_friendName_tv, TimeUnit.SECONDS.toMillis(1000)));
        onView(withId(R.id.add_friend_to_tour_error_tv)).check(matches(withText("Cannot find user with email entered on friends list")));
    }

    /**
     *Checks to make sure someone not on the users friends list cant be added by them.
     **/
    @Test
    public void cantSearchForInvalidUser(){

        //click on a tour
        onView(withId(R.id.personal_current_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //click on add friend to tour button
        onView(isRoot()).perform(waitForView(R.id.tour_add_friend_btn, TimeUnit.SECONDS.toMillis(1000)));
        onView(withId(R.id.tour_add_friend_btn)).perform(nestedScrollTo());
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.tour_add_friend_btn), withText("Add Friend"),
                        childAtPosition(
                                allOf(withId(R.id.tour_buttons_container),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                9)),
                                0),
                        isDisplayed()));
        appCompatButton2.perform(click());

        //type in friend email to search
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_email_et, TimeUnit.SECONDS.toMillis(1000)));
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.add_friend_to_tour_email_et),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("asdsa@asdsa.casd"), closeSoftKeyboard());

        //click on search
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_friend_to_tour_search_btn), withText("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton3.perform(click());

        //make sure error message appears
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_friendName_tv, TimeUnit.SECONDS.toMillis(1000)));
        onView(withId(R.id.add_friend_to_tour_error_tv)).check(matches(withText("Cannot find user with email entered on friends list")));
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
