package com.tourtrek;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.tourtrek.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.tourtrek.EspressoExtensions.nestedScrollTo;
import static com.tourtrek.EspressoExtensions.waitForView;


public class RateTourTest {

    public static final String TAG = "TourFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException, UiObjectNotFoundException {

        mainActivityScenario = mainActivityScenarioRule.getScenario();

        // log out of any current account, log into the test account, navigate to the personal tours tab, and select the first tour in the future tours section
        try {
            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(100)));
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(withId(R.id.profile_logout_btn)).perform(click());
        } catch (Exception NoMatchingViewException) {
            Log.w(TAG, "Not logged in");
        } finally {
            onView(withId(R.id.navigation_tours)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.login_email_et, TimeUnit.SECONDS.toMillis(100)));
            onView(withId(R.id.login_email_et)).perform(typeText("user@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.login_password_et)).perform(typeText("000000"), closeSoftKeyboard());
            onView(withId(R.id.login_login_btn)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(100)));
            onView(withId(R.id.personal_future_tours_title_btn)).perform(click());


            //enter info to create tour
            onView(withId(R.id.tour_name_et)).perform(typeText("my tour"), closeSoftKeyboard());
            onView(withId(R.id.tour_location_et)).perform(typeText("Madison, WI, USA"), closeSoftKeyboard());
            onView(withId(R.id.tour_cost_et)).perform(typeText("0"), closeSoftKeyboard());

            onView(withId(R.id.tour_start_date_btn)).perform(nestedScrollTo());

            onView(withId(R.id.tour_start_date_btn)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 12, 15));
            onView(withId(android.R.id.button1)).perform(click());

            onView(withId(R.id.tour_end_date_btn)).perform(nestedScrollTo());

            onView(withId(R.id.tour_end_date_btn)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 12, 29));
            onView(withId(android.R.id.button1)).perform(click());


            onView(withId(R.id.tour_public_cb)).perform(nestedScrollTo());
            onView(withId(R.id.tour_public_cb)).perform(click());

            onView(withId(R.id.tour_update_btn)).perform(click());
        }
    }

    @Test
    public void rateTourTest() {

        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(100)));
        onView(withId(R.id.personal_future_tours_title_btn)).perform(click());



        onView(withId(R.id.tour_review_btn)).perform(click());
    }

    // TODO check for the navigation toast message when a tour map is displayed
    // TODO check for the no location found toast message when a tour map is displayed and the user has no personal location data
    // TODO check for my navigation markers

    /**
     * For dates and times
     * @param parentMatcher
     * @param position
     * @return
     */
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