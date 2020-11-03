package com.tourtrek;

import com.tourtrek.activities.MainActivity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test updating an existing tour
 */
public class TourFragmentTest {

    public static final String TAG = "TourFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {

//        mainActivityScenario = mainActivityScenarioRule.getScenario();
//
//        // log out of any current account, log into the test account, navigate to the personal tours tab, and select the first tour in the future tours section
//        try {
//            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(15)));
//            onView(withId(R.id.navigation_profile)).perform(click());
//            onView(withId(R.id.profile_logout_btn)).perform(click());
//        } catch (Exception NoMatchingViewException) {
//            Log.w(TAG, "Not logged in");
//        } finally {
//            onView(withId(R.id.navigation_tours)).perform(click());
//            onView(withId(R.id.login_email_et)).perform(typeText("jrawlins@wisc.edu"), closeSoftKeyboard());
//            onView(withId(R.id.login_password_et)).perform(typeText("123456"), closeSoftKeyboard());
//            onView(withId(R.id.login_login_btn)).perform(click());
//            onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(15)));
//            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//        }
    }

    /**
     * Updating fields should stick
     */
    @Test
    public void updateSuccessful() {
//        /** Update 1 */
//        // populate edit text fields
//        onView(withId(R.id.tour_name_et)).perform(typeText("Brighton Snowboard 2"));
//        onView(withId(R.id.tour_location_et)).perform(typeText("Brighton, UT 2"), closeSoftKeyboard());
//        onView(withId(R.id.tour_cost_et)).perform(typeText("5"), closeSoftKeyboard());
//        onView(withId(R.id.tour_length_et)).perform(typeText("20"), closeSoftKeyboard());
//
//        // scroll to the tour update button and click it
//        updateClick();
//
//        /** Update 2 */
//        // navigate away so that the tours must refresh
//        refresh();
//
//        // populate edit text fields
//        onView(withId(R.id.tour_name_et)).perform(typeText("Brighton Snowboard 3"));
//        onView(withId(R.id.tour_location_et)).perform(typeText("Brighton, UT 3"), closeSoftKeyboard());
//        onView(withId(R.id.tour_cost_et)).perform(typeText("15"), closeSoftKeyboard());
//        onView(withId(R.id.tour_length_et)).perform(typeText("30"), closeSoftKeyboard());
//
//        // scroll to the tour update button and click it
//        updateClick();
//
//        /** Final check */
//        // navigate away so that the tours must refresh
//        refresh();
//
//        // check the text fields
//        onView(isRoot()).perform(waitForView(R.id.tour_name_et, TimeUnit.SECONDS.toMillis(5)));
//        onView(withId(R.id.tour_name_et)).check(matches(withText("Brighton Snowboard 3")));
//        onView(withId(R.id.tour_location_et)).check(matches(withText("Location:Brighton, UT 3")));
//        onView(withId(R.id.tour_cost_et)).check(matches(withText("Cost($): 15.0")));
//        onView(withId(R.id.tour_length_et)).check(matches(withText("Length: 30")));
    }

    private void updateClick(){
//        onView(withId(R.id.tour_update_btn)).perform(scrollTo());
//        onView(withId(R.id.tour_update_btn)).perform(click());
    }

    private void refresh(){
//        onView(withId(R.id.navigation_profile)).perform(click());
//        onView(withId(R.id.navigation_tours)).perform(click());
//        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(5)));
//        onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
