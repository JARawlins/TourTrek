package com.tourtrek;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

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


public class TourMarketFragmentTest2 {

    public static final String TAG = "TourMarketFragmentTest2";
    private ActivityScenario mainActivityScenario;
    private  String name = "0";

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException, UiObjectNotFoundException {

        mainActivityScenario = mainActivityScenarioRule.getScenario();

        login();

        create_tour(name);
    }


    @Test
    public void tourSearchTest() throws InterruptedException {
        onView(isRoot()).perform(waitForView(R.id.navigation_tour_market, TimeUnit.SECONDS.toMillis(15)));
        onView(withId(R.id.navigation_tour_market)).perform(click());
        sleep(2000);
        test(0);
        test(1);
        test(2);
        test(3);
        test(4);
        test(5);
        test(6);
        test(7);

        search(name);

        sleep(1000);

        sortBy(0);

        sleep(2000);

        try {
            onView(withId(R.id.tour_market_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(name))));
            onView(withId(R.id.tour_market_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(name)), click()));
        } catch (Exception e) {
            sleep(2000);
            onView(withId(R.id.tour_market_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }

        onView(isRoot()).perform(waitForView(R.id.tour_name_et, TimeUnit.SECONDS.toMillis(30)));
        sleep(1000);
        onView(withId(R.id.tour_name_et)).perform(nestedScrollTo());
        onView(withId(R.id.tour_name_et)).check(matches(withText(name)));
    }


    @After
    public void destroy() throws InterruptedException {
        sleep(3000);

        onView(withId(R.id.tour_public_cb)).perform(nestedScrollTo());
        onView(withId(R.id.tour_public_cb)).perform(click());

        sleep(500);

        onView(withId(R.id.tour_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.tour_update_btn)).perform(click());

        sleep(2000);

        onView(withId(R.id.navigation_tours)).perform(click());
        onView(isRoot()).perform(waitForView(R.id.navigation_tours, TimeUnit.SECONDS.toMillis(30)));


        delete_tour("0");

        sleep(1000);
    }

    private void test(int pos) throws InterruptedException {

        sleep(2000);

        sortBy(pos);

        sleep(2000);
    }

    public void login() throws InterruptedException, UiObjectNotFoundException {

        // log out of any current account, log into the test account, navigate to the personal tours tab, and select the first tour in the future tours section
        try {
            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(30)));
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(withId(R.id.profile_logout_btn)).perform(click());
        } catch (Exception NoMatchingViewException) {
            Log.w(TAG, "Not logged in");
        } finally {
            onView(withId(R.id.navigation_tours)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.login_email_et, TimeUnit.SECONDS.toMillis(30)));
            onView(withId(R.id.login_email_et)).perform(typeText("user@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.login_password_et)).perform(typeText("000000"), closeSoftKeyboard());
            onView(withId(R.id.login_login_btn)).perform(click());

        }
    }



    public void delete_tour(String name) throws InterruptedException {

        onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(30)));
        onView(withId(R.id.navigation_profile)).perform(click());
        sleep(500);
        onView(withId(R.id.navigation_tours)).perform(click());

        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_rv, TimeUnit.SECONDS.toMillis(30)));
        sleep(2000);

        try {
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(name))));
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(name)), click()));

        } catch (androidx.test.espresso.PerformException e) {
            sleep(2000);
            onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        } finally {
            onView(isRoot()).perform(waitForView(R.id.tour_attractions_rv, TimeUnit.SECONDS.toMillis(30)));

            //delete tour
            onView(withId(R.id.tour_delete_btn)).perform(nestedScrollTo());
            onView(withId(R.id.tour_delete_btn)).perform(click());
        }
    }

    public void create_tour(String name) {
        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(30)));
        onView(withId(R.id.personal_future_tours_title_btn)).perform(click());


        //enter info to create tour
        onView(withId(R.id.tour_name_et)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.tour_location_et)).perform(typeText("Madison, Wi"), closeSoftKeyboard());
        onView(withId(R.id.tour_cost_et)).perform(typeText("0"), closeSoftKeyboard());

        onView(withId(R.id.tour_start_date_btn)).perform(nestedScrollTo());

        onView(withId(R.id.tour_start_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 12, 21));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.tour_end_date_btn)).perform(nestedScrollTo());

        onView(withId(R.id.tour_end_date_btn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 12, 29));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.tour_public_cb)).perform(nestedScrollTo());
        onView(withId(R.id.tour_public_cb)).perform(click());

        onView(withId(R.id.tour_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.tour_update_btn)).perform(click());
    }



    public void sortBy(int pos) {
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.tour_market_btn), withText("Sort By"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(pos);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton3.perform(scrollTo(), click());
    }

    private void search(String key) throws InterruptedException {
        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.search_button), withContentDescription("Search"),
                        childAtPosition(
                                allOf(withId(R.id.search_bar),
                                        childAtPosition(
                                                withId(R.id.tour_market_search_itm),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction searchAutoComplete = onView(
                allOf(withId(R.id.search_src_text),
                        childAtPosition(
                                allOf(withId(R.id.search_plate),
                                        childAtPosition(
                                                withId(R.id.search_edit_frame),
                                                1)),
                                0),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("pas"));

        sleep(2500);

        onView(withId(R.id.search_edit_frame)).perform(ViewActions.pressKey(KeyEvent.KEYCODE_ENTER));
        sleep(2500);
        searchAutoComplete.perform(replaceText(key), closeSoftKeyboard());
        sleep(2000);
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