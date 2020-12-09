package com.tourtrek;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiObjectNotFoundException;

import com.tourtrek.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tourtrek.EspressoExtensions.nestedScrollTo;
import static com.tourtrek.EspressoExtensions.waitForView;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;


public class AttractionTest {

    public static final String TAG = "AttractionTest";
    private ActivityScenario mainActivityScenario;
    private String name = "madison";

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException, UiObjectNotFoundException {

        mainActivityScenario = mainActivityScenarioRule.getScenario();

        login();

        create_tour();

        add_attraction(name);

        sleep(2500);
        onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(100)));
        onView(withId(R.id.navigation_profile)).perform(click());
        sleep(500);
        onView(withId(R.id.navigation_tours)).perform(click());
    }


    @Test
    public void editTest() throws InterruptedException {

        sleep(2000);

        edit_attraction("togo");
        sleep(2000);
        onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(100)));
        onView(withId(R.id.navigation_profile)).perform(click());
        onView(isRoot()).perform(waitForView(R.id.navigation_tours, TimeUnit.SECONDS.toMillis(100)));
        onView(withId(R.id.navigation_tours)).perform(click());

        try {
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(name))));
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(name)), click()));
        } catch (Exception e) {
            sleep(100);
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }


        sleep(1000);

        onView(withId(R.id.tour_attractions_rv)).perform(nestedScrollTo());
        onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        sleep(1000);
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).check(matches(withText("food")));


        onView(withId(R.id.attraction_delete_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_delete_btn)).perform(click());

        sleep(2000);
    }

    @After
    public void destroy() throws InterruptedException {
        sleep(2000);
        onView(withId(R.id.navigation_profile)).perform(click());
        sleep(100);
        onView(withId(R.id.navigation_tours)).perform(click());
        sleep(2500);

        try {
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(name))));
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(name)), click()));
        } catch (Exception e) {
            sleep(2000);
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }

        sleep(500);

        onView(withId(R.id.tour_delete_btn)).perform(nestedScrollTo());
        onView(withId(R.id.tour_delete_btn)).perform(click());
        sleep(2000);
    }

    public void login() throws InterruptedException, UiObjectNotFoundException {

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

        }
    }


    public void create_tour() {
        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(100)));
        onView(withId(R.id.personal_future_tours_title_btn)).perform(click());


        //enter info to create tour
        onView(withId(R.id.tour_name_et)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.tour_location_et)).perform(typeText("Madison, WI, USA"), closeSoftKeyboard());
        onView(withId(R.id.tour_cost_et)).perform(typeText("0"), closeSoftKeyboard());

        onView(withId(R.id.tour_start_date_btn)).perform(nestedScrollTo());

        onView(withId(R.id.tour_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 12, 20));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.tour_end_date_btn)).perform(nestedScrollTo());

        onView(withId(R.id.tour_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022, 12, 29));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.tour_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.tour_update_btn)).perform(click());
    }

    public void add_attraction(String key) throws InterruptedException {
        sleep(2500);
        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_rv, TimeUnit.SECONDS.toMillis(1000)));

        try {
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("sorting"))));
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("sorting")), click()));
        } catch (Exception e) {
            sleep(2000);
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }

        onView(isRoot()).perform(waitForView(R.id.tour_attractions_rv, TimeUnit.SECONDS.toMillis(1000)));

        onView(withId(R.id.tour_add_attraction_btn)).perform(nestedScrollTo());
        onView(withId(R.id.tour_add_attraction_btn)).perform(click());
        sleep(50);

        onView(withId(R.id.attraction_search_ib)).perform(click());
        sleep(1000);
        onView(withId(R.id.places_autocomplete_search_bar)).perform(replaceText(key));
        sleep(1000);
        onView(isRoot()).perform(waitForView(R.id.places_autocomplete_content, TimeUnit.SECONDS.toMillis(30)));
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.places_autocomplete_list),
                        childAtPosition(
                                withId(R.id.places_autocomplete_content),
                                3)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));



        //enter info to create attraction
        sleep(2000);

        onView(isRoot()).perform(waitForView(R.id.attraction_cover_iv, TimeUnit.SECONDS.toMillis(100)));

        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("600"), closeSoftKeyboard());

        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 12, 25));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11,00));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 12, 26));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11,00));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("nice food"), closeSoftKeyboard());

        sleep(1500);

        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        try {
            onView(withId(R.id.attraction_update_btn)).perform(click());
        } catch (Exception e) {

        }
        sleep(3000);
    }



    public void edit_attraction(String key) throws InterruptedException {
        sleep(2500);
        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_rv, TimeUnit.SECONDS.toMillis(1000)));

        try {
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(name))));
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("name")), click()));
        } catch (Exception e) {
            sleep(2000);
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }

        onView(isRoot()).perform(waitForView(R.id.tour_attractions_rv, TimeUnit.SECONDS.toMillis(1000)));

        onView(withId(R.id.tour_attractions_rv)).perform(nestedScrollTo());

        try {
            onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Madison Park"))));
            onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Madison Park")), click()));
        } catch (Exception e) {
            sleep(2000);
            onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }

        sleep(50);

        onView(withId(R.id.attraction_search_ib)).perform(click());
        sleep(1000);
        onView(withId(R.id.places_autocomplete_search_bar)).perform(replaceText(key));
        sleep(1000);
        onView(isRoot()).perform(waitForView(R.id.places_autocomplete_content, TimeUnit.SECONDS.toMillis(30)));
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.places_autocomplete_list),
                        childAtPosition(
                                withId(R.id.places_autocomplete_content),
                                3)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));



        //enter info to create attraction
        sleep(2000);

        onView(isRoot()).perform(waitForView(R.id.attraction_cover_iv, TimeUnit.SECONDS.toMillis(100)));

        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_cost_et)).perform(typeText("50"), closeSoftKeyboard());

        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_date_btn)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 10, 20));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_start_time_btn)).perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10,20));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 12, 26));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_end_time_btn)).perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11,5));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_description_et)).perform(typeText("food"), closeSoftKeyboard());

        sleep(1500);

        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        try {
            onView(withId(R.id.attraction_update_btn)).perform(click());
        } catch (Exception e) {

        }
        sleep(2500);
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