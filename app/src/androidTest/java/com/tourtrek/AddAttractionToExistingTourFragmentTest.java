//package com.tourtrek;
//
//import com.tourtrek.activities.MainActivity;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//
//import static androidx.test.espresso.action.ViewActions.click;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static androidx.test.espresso.matcher.ViewMatchers.withText;
//
//
///*
//This test file assumes that AddAttractionFragment is reached through selecting a user's tour through the personal tour tab
// */
//public class AddAttractionToExistingTourFragmentTest {
//
//    public static final String TAG = "AddAttractionFragmentTest";
//    private ActivityScenario mainActivityScenario;
//
//    @Rule
//    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Before
//    public void setup() {
//
////        mainActivityScenario = mainActivityScenarioRule.getScenario();
////
////        // log out of any current account, log into the test account, navigate to the personal tours tab, and select the first tour in the future tours section
////        try {
////            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(15)));
////            onView(withId(R.id.navigation_profile)).perform(click());
////            onView(withId(R.id.profile_logout_btn)).perform(click());
////        } catch (Exception NoMatchingViewException) {
////            Log.w(TAG, "Not logged in");
////        } finally {
////            onView(withId(R.id.navigation_tours)).perform(click());
////            onView(withId(R.id.login_email_et)).perform(typeText("jrawlins@wisc.edu"), closeSoftKeyboard());
////            onView(withId(R.id.login_password_et)).perform(typeText("123456"), closeSoftKeyboard());
////            onView(withId(R.id.login_login_btn)).perform(click());
////            onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(15)));
////            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
////            onView(withId(R.id.tour_add_attraction_btn)).perform(scrollTo());
////            onView(withId(R.id.tour_add_attraction_btn)).perform(click());
////        }
////        Random rand = new Random();
//
//
//    }
//
//    /**
//     * An error message should display when the user inputs no name, but every other field
//     */
//    @Test
//    public void noAttractionName() {
////        // populate edit text fields
////        onView(withId(R.id.add_attraction_name_et)).perform(typeText(""));
////        onView(withId(R.id.add_attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(typeText("11-11-2019T08:10"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_end_et)).perform(typeText("11-11-2019T10:10"), closeSoftKeyboard());
////
////        // scroll to the "add attraction" button and click it
////        onView(withId(R.id.add_attraction_add_btn)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_add_btn)).perform(click());
////
////        // final check to determine pass or fail
////        onView(withId(R.id.add_attraction_error_tv)).check(matches(withText("Enter at least name, location, and start and end time information in the indicated formats")));
//    }
//
//
//    /**
//     * An error message should appear when the user inputs no location, but every other field
//     */
//    @Test
//    public void noLocation() {
////        // populate edit text fields
////        onView(withId(R.id.add_attraction_name_et)).perform(typeText("Wisconsin Institute for Discovery"));
////        onView(withId(R.id.add_attraction_location_et)).perform(typeText(""), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(typeText("11-11-2019T08:10"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_end_et)).perform(typeText("11-11-2019T10:10"), closeSoftKeyboard());
////
////        // scroll to the "add attraction" button and click it
////        onView(withId(R.id.add_attraction_add_btn)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_add_btn)).perform(click());
////
////        // final check to determine pass or fail
////        onView(withId(R.id.add_attraction_error_tv)).check(matches(withText("Enter at least name, location, and start and end time information in the indicated formats")));
//    }
//
//    /**
//     * An error message should appear when the user inputs no start time, but every other field
//     */
//    @Test
//    public void noStartTime() {
////        // populate edit text fields
////        onView(withId(R.id.add_attraction_name_et)).perform(typeText("Wisconsin Institute for Discovery"));
////        onView(withId(R.id.add_attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(typeText(""), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_end_et)).perform(typeText("11-11-2019T10:10"), closeSoftKeyboard());
////
////        // scroll to the "add attraction" button and click it
////        onView(withId(R.id.add_attraction_add_btn)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_add_btn)).perform(click());
////
////        // final check to determine pass or fail
////        onView(withId(R.id.add_attraction_error_tv)).check(matches(withText("Enter at least name, location, and start and end time information in the indicated formats")));
//    }
//
//
//    /**
//     * An error message should appear when the user inputs no end time, but every other field
//     */
//    @Test
//    public void noEndTime() {
////        // populate edit text fields
////        onView(withId(R.id.add_attraction_name_et)).perform(typeText("Wisconsin Institute for Discovery"));
////        onView(withId(R.id.add_attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(typeText("11-11-2019T08:10"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_end_et)).perform(typeText(""), closeSoftKeyboard());
////
////        // scroll to the "add attraction" button and click it
////        onView(withId(R.id.add_attraction_add_btn)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_add_btn)).perform(click());
////
////        // final check to determine pass or fail
////        onView(withId(R.id.add_attraction_error_tv)).check(matches(withText("Enter at least name, location, and start and end time information in the indicated formats")));
//    }
//
//
//    /**
//     * Clicking the "add attraction" button, assuming that name, location, and time fields have been properly entered,
//     * should take the user back to the edit tour screen
//     */
//    @Test
//    public void backToEditTour() {
////        // populate edit text fields
////        onView(withId(R.id.add_attraction_name_et)).perform(typeText("Wisconsin Institute for Discovery"));
////        onView(withId(R.id.add_attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_time_start_et)).perform(typeText("11-11-2019T08:10"), closeSoftKeyboard());
////        onView(withId(R.id.add_attraction_time_end_et)).perform(typeText("11-11-2019T10:10"), closeSoftKeyboard());
////
////        // scroll to the "add attraction" button and click it
////        onView(withId(R.id.add_attraction_add_btn)).perform(scrollTo());
////        onView(withId(R.id.add_attraction_add_btn)).perform(click());
////
////        // final check to determine pass or fail
////        // this check will only pass if we have successfully returned to the edit tour page
////        onView(withId(R.id.tour_update_btn)).check(matches(withText("Update Tour")));
//    }
//
//}