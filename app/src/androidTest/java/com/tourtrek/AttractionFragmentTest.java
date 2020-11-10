package com.tourtrek;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TimePicker;

import com.tourtrek.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

import androidx.core.widget.NestedScrollView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ScrollToAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.tourtrek.EspressoExtensions.nestedScrollTo;
import static com.tourtrek.EspressoExtensions.waitForView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

/*
This test file assumes that AddAttractionFragment is reached through selecting a user's tour through the personal tour tab
 */
public class AttractionFragmentTest {

    public static final String TAG = "AttractionFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException, UiObjectNotFoundException {

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
            onView(isRoot()).perform(waitForView(R.id.login_email_et, TimeUnit.SECONDS.toMillis(15)));
            onView(withId(R.id.login_email_et)).perform(typeText("jrawlins@wisc.edu"), closeSoftKeyboard());
            onView(withId(R.id.login_password_et)).perform(typeText("123456"), closeSoftKeyboard());
            onView(withId(R.id.login_login_btn)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(15)));
            sleep(1000); // sleep so that the recycler view to click is loaded
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            // onView(withId(R.id.tour_add_attraction_btn)).perform(scrollTo());
            //onView(withId(R.id.tour_buttons_container)).perform(scrollTo());
            //onView(isRoot()).perform(waitForView(R.id.attraction_name_et, TimeUnit.SECONDS.toMillis(15)));
            onView(withId(R.id.tour_add_attraction_btn)).perform(nestedScrollTo());
            onView(withId(R.id.tour_add_attraction_btn)).perform(click());
        }
    }

    /**
     * An error message should display when the user inputs no name, but every other field
     * https://stackoverflow.com/questions/44835094/check-datepicker-calendar-value-in-android-espresso-framework#44840330
     * https://stackoverflow.com/questions/43149728/select-date-from-calendar-in-android-espresso/43180527
     */
    @Test
    public void noAttractionName() throws InterruptedException {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText(""));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }


    /**
     * An error message should appear when the user inputs no location, but every other field
     */
    @Test
    public void noLocation() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some kind of attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void noCost() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some kind of attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("Nowhere"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    /**
     * An error message should appear when the user inputs no start time, but every other field
     */
    @Test
    public void noStartDate() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    /**
     * An error message should appear when the user inputs no location, but every other field
     */
    @Test
    public void noStartTime() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some kind of attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("Nowhere"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void noEndDate() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some kind of attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("Nowhere"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }




    /**
     * An error message should appear when the user inputs no end time, but every other field
     */
    @Test
    public void noEndTime() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some kind of attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("Nowhere"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void noDescription() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText(""), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    /**
     * test to check the toast for a successful attraction update
     */
    @Test
    public void updateSuccessful() {
        // populate edit text fields
        onView(withId(R.id.attraction_name_et)).perform(typeText("Some attraction"));
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_location_et)).perform(typeText("330 N. Orchard St., Madison, WI, USA"), closeSoftKeyboard());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("Random sample description"), closeSoftKeyboard());

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
        onView(withId(android.R.id.button1)).perform(click());

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
        onView(withId(android.R.id.button1)).perform(click());

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the toast message
        onView(withText("Successfully Updated Attraction")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    // TODO test to check that an attraction is successfully added to the recycler view of the current tour following addition, then updating

    // TODO test to check that the attraction edit has stuck after leaving the tour and then going back to it



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

    /**
     * Helper method to minimize duplicate code
     * @param condition
     */
    private void attractionConditionsTest(String condition){


    }


}