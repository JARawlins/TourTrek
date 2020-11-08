package com.tourtrek;

import android.util.Log;
import com.tourtrek.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static com.tourtrek.EspressoExtensions.waitForView;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;

/**
 * Test updating an existing tour
 */
public class TourFragmentTest {

    public static final String TAG = "TourFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    // public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException {

        mainActivityScenario = mainActivityScenarioRule.getScenario();

        // log out of any current account, log into the test account, navigate to the personal tours tab, and select the first tour in the future tours section
        try {
            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(15)));
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(withId(R.id.profile_logout_btn)).perform(click());
        } catch (Exception NoMatchingViewException) {
            Log.w(TAG, "Not logged in");
        } finally {
            onView(withId(R.id.navigation_tours)).perform(click());
            onView(withId(R.id.login_email_et)).perform(typeText("jrawlins@wisc.edu"), closeSoftKeyboard());
            onView(withId(R.id.login_password_et)).perform(typeText("123456"), closeSoftKeyboard());
            onView(withId(R.id.login_login_btn)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.personal_future_tours_rv, TimeUnit.SECONDS.toMillis(15)));
            sleep(10000);
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }
    }

    /**
     * Updated test for editing fields of a tour
     * // TODO I got the set up working, but need to refine the code below for testing the date selector
     */
    @Test
    public void updateSuccessful1() {
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.tour_start_date_btn), withText("11/06/2020"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton3.perform(scrollTo(), click());
    }

    /**
     * Updating fields should stick
     */
    @Test
    public void updateSuccessful() {
        /** Update 1 */
        // populate edit text fields
        onView(withId(R.id.tour_name_et)).perform(typeText("Brighton Snowboard 2"));
        onView(withId(R.id.tour_location_et)).perform(typeText("Brighton, UT 2"), closeSoftKeyboard());
        onView(withId(R.id.tour_cost_et)).perform(typeText("5"), closeSoftKeyboard());
        //onView(withId(R.id.tour_length_et)).perform(typeText("20"), closeSoftKeyboard());

        // scroll to the tour update button and click it
        updateClick();

        /** Update 2 */
        // navigate away so that the tours must refresh
        refresh();

        // populate edit text fields
        onView(withId(R.id.tour_name_et)).perform(typeText("Brighton Snowboard 3"));
        onView(withId(R.id.tour_location_et)).perform(typeText("Brighton, UT 3"), closeSoftKeyboard());
        onView(withId(R.id.tour_cost_et)).perform(typeText("15"), closeSoftKeyboard());
        //onView(withId(R.id.tour_length_et)).perform(typeText("30"), closeSoftKeyboard());

        // scroll to the tour update button and click it
        updateClick();

        /** Final check */
        // navigate away so that the tours must refresh
        refresh();

        // check the text fields
        onView(isRoot()).perform(waitForView(R.id.tour_name_et, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.tour_name_et)).check(matches(withText("Brighton Snowboard 3")));
        onView(withId(R.id.tour_location_et)).check(matches(withText("Location:Brighton, UT 3")));
        onView(withId(R.id.tour_cost_et)).check(matches(withText("Cost($): 15.0")));
        //onView(withId(R.id.tour_length_et)).check(matches(withText("Length: 30")));
    }

    private void updateClick(){
        onView(withId(R.id.tour_update_btn)).perform(scrollTo());
        onView(withId(R.id.tour_update_btn)).perform(click());
    }

    private void refresh(){
        onView(withId(R.id.navigation_profile)).perform(click());
        onView(withId(R.id.navigation_tours)).perform(click());
        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
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
