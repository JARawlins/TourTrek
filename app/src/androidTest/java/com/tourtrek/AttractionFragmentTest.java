package com.tourtrek;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;
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
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.uiautomator.UiObjectNotFoundException;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.tourtrek.EspressoExtensions.nestedScrollTo;
import static com.tourtrek.EspressoExtensions.waitForView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;


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
            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(30)));
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(withId(R.id.profile_logout_btn)).perform(click());
        } catch (Exception NoMatchingViewException) {
            Log.w(TAG, "Not logged in");
        } finally {
            onView(withId(R.id.navigation_tours)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.login_email_et, TimeUnit.SECONDS.toMillis(30)));
            onView(withId(R.id.login_email_et)).perform(replaceText("jrawlins@wisc.edu"), closeSoftKeyboard());
            onView(withId(R.id.login_password_et)).perform(replaceText("123456"), closeSoftKeyboard());
            onView(withId(R.id.login_login_btn)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(30)));
            sleep(1000); // sleep so that the recycler view to click is loaded
            // onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.personal_future_tours_title_btn)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.tour_name_et, TimeUnit.SECONDS.toMillis(30)));
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
    public void noName() throws InterruptedException {
        // test the case of no attraction name
        attractionConditionsTest("noAttraction");
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();

        // save for the very end
        removeAdded();
    }

    @Test
    public void noLocation() throws InterruptedException {
        attractionConditionsTest("noLocation");
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();
        removeAdded();
    }

    @Test
    public void noCost() throws InterruptedException {
        attractionConditionsTest("noCost");
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();
        removeAdded();
    }

    @Test
    public void noStartDate() throws InterruptedException {
        attractionConditionsTest("noStartDate");
        Espresso.pressBack();
        removeAdded();
    }

    @Test
    public void noStartTime() throws InterruptedException{
        attractionConditionsTest("noStartTime");
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();
        removeAdded();
    }

    @Test
    public void noEndDate() throws InterruptedException{
        attractionConditionsTest("noEndDate");
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();
        removeAdded();
    }

    @Test
    public void noEndTime() throws InterruptedException{
        attractionConditionsTest("noEndTime");
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();
        removeAdded();
    }

    @Test
    public void noDescription() throws InterruptedException{
        attractionConditionsTest("noDescription");
        onView(withText("Not all fields entered")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();
        removeAdded();
    }

    @Test
    public void invalidDatesTest() throws InterruptedException {
        attractionConditionsTest("invalidDates");
        onView(withText("Start dates must be before end dates!")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();
        removeAdded();
    }

    /**
     * test to check the toast for a successful attraction update
     */
    @Test
    public void additionSuccessfulTest() throws InterruptedException {
        attractionConditionsTest("SUCCESSFUL ADDITION");
        removeAdded();
    }


    /**
     * Test for updating an attraction, not making a new one
     */
    @Test
    public void existingAttractionTests() throws InterruptedException {
        attractionConditionsTest("");

        onView(isRoot()).perform(waitForView(R.id.tour_attractions_rv, TimeUnit.SECONDS.toMillis(30)));

        sleep(500); // give time for the recycler view items to load

        // find the newly made attraction and select it
        onView(withId(R.id.tour_attractions_rv)).perform(nestedScrollTo());
        onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Wisconsin Institute for Discovery"))));
        onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Wisconsin Institute for Discovery")), click()));

        // update the attraction name
        onView(isRoot()).perform(waitForView(R.id.attraction_name_et, TimeUnit.SECONDS.toMillis(30)));
        onView(withId(R.id.attraction_name_et)).perform(replaceText("New attraction name"), closeSoftKeyboard());

        // map check
        onView(withId(R.id.attraction_navigation_btn)).perform(nestedScrollTo(), click());
        onView(withText("Tap on a marker for navigation.")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        Espresso.pressBack();

        // scroll to the "update attraction" button and click it
        onView(isRoot()).perform(waitForView(R.id.attraction_name_et, TimeUnit.SECONDS.toMillis(30)));
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());

        // check for the proper toast message
        onView(withText("Successfully Updated Attraction")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // delete the tour and attraction
        sleep(1000);
        Espresso.pressBack();
        removeAdded();
    }

    /**
     * Test for updating an attraction, not making a new one
     */
    @Test
    public void mapTest() throws InterruptedException {
        attractionConditionsTest("");

        onView(isRoot()).perform(waitForView(R.id.tour_attractions_rv, TimeUnit.SECONDS.toMillis(30)));

        sleep(500); // give time for the recycler view items to load

        // find the newly made attraction and select it
        onView(withId(R.id.tour_attractions_rv)).perform(nestedScrollTo());
        onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Wisconsin Institute for Discovery"))));
        onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Wisconsin Institute for Discovery")), click()));

        // map check
        onView(withId(R.id.attraction_navigation_btn)).perform(nestedScrollTo(), click());
        onView(withText("Tap on a marker for navigation.")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        Espresso.pressBack();
        Espresso.pressBack();
        removeAdded();

    }

    /**
     * Test deletion of an attraction
     */
    @Test
    public void deletionTest() throws InterruptedException {
        attractionConditionsTest("");

        onView(isRoot()).perform(waitForView(R.id.tour_attractions_rv, TimeUnit.SECONDS.toMillis(30)));

        sleep(500); // give time for the recycler view items to load

        // find the newly made attraction and select it
        onView(withId(R.id.tour_attractions_rv)).perform(nestedScrollTo());
        onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText("Wisconsin Institute for Discovery"))));
        onView(withId(R.id.tour_attractions_rv)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Wisconsin Institute for Discovery")), click()));

        // update the attraction name
        onView(isRoot()).perform(waitForView(R.id.attraction_name_et, TimeUnit.SECONDS.toMillis(30)));

        // scroll to the "update attraction" button and click it
        onView(withId(R.id.attraction_delete_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_delete_btn)).perform(click());

        // check for the proper toast message
        sleep(500);
        onView(withText("Attraction Deleted")).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // delete the tour and attraction
        removeAdded();
    }

    /**
     *  Increase coverage of FocusChangeListeners by clicking on fields on then clicking away to another before entering anything.
     *  This applies to editable text fields. Hitting the second if statement block with dates and times seems not to be possible.
     * Helper method to minimize duplicate code
     * Possible conditions include:
     * noAttraction
     * noLocation
     * noCost
     * noDescription
     * noStartDate
     * noStartTime
     * noEndDate
     * noEndTime
     * invalidDates
     * @param condition
     */

    private void attractionConditionsTest(String condition) throws InterruptedException {
        // attraction name
        onView(withId(R.id.attraction_name_et)).perform(nestedScrollTo()); // the field to be modified must be in view
        if (condition.equals("noAttraction")){ onView(withId(R.id.attraction_name_et)).perform(replaceText(""), closeSoftKeyboard()); }
        else { onView(withId(R.id.attraction_name_et)).perform(replaceText("Some attraction"), closeSoftKeyboard()); }

        // location
        onView(withId(R.id.attraction_location_et)).perform(nestedScrollTo());
        if (!(condition.equals("noLocation") || condition.equals("noAttraction"))) {
            onView(withId(R.id.attraction_search_ib)).perform(click());
            sleep(1000);
            onView(withId(R.id.places_autocomplete_search_bar)).perform(replaceText("Wisconsin Institute for Discovery"));
            sleep(1000);
            onView(isRoot()).perform(waitForView(R.id.places_autocomplete_content, TimeUnit.SECONDS.toMillis(30)));
            ViewInteraction recyclerView = onView(
                    allOf(withId(R.id.places_autocomplete_list),
                            childAtPosition(
                                    withId(R.id.places_autocomplete_content),
                                    3)));
            recyclerView.perform(actionOnItemAtPosition(0, click()));
        }

        // cost
        onView(isRoot()).perform(waitForView(R.id.attraction_name_et, TimeUnit.SECONDS.toMillis(30)));
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo());
        if (condition.equals("noCost")){onView(withId(R.id.attraction_cost_et)).perform(replaceText(""), closeSoftKeyboard());}
        else {onView(withId(R.id.attraction_cost_et)).perform(replaceText("0"), closeSoftKeyboard());}

        // description

        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        if (condition.equals("noDescription")){onView(withId(R.id.attraction_description_et)).perform(replaceText(""), closeSoftKeyboard());}
        else {onView(withId(R.id.attraction_description_et)).perform(replaceText("Random sample description"), closeSoftKeyboard());}

        // set the start date
        onView(withId(R.id.attraction_start_date_btn)).perform(nestedScrollTo());
        if (!condition.equals("noStartDate")){
            onView(withId(R.id.attraction_start_date_btn)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 10));
            onView(withId(android.R.id.button1)).perform(click());
        }

        // set the start time
        onView(withId(R.id.attraction_start_time_btn)).perform(nestedScrollTo());
        if (!condition.equals("noStartTime")){
            onView(withId(R.id.attraction_start_time_btn)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(6, 30));
            onView(withId(android.R.id.button1)).perform(click());
        }

        // set end date
        onView(withId(R.id.attraction_end_date_btn)).perform(nestedScrollTo());
        if (condition.equals("invalidDates")){
            onView(withId(R.id.attraction_end_date_btn)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 9));
            onView(withId(android.R.id.button1)).perform(click());
        }
        else if (!condition.equals("noEndDate")){
            onView(withId(R.id.attraction_end_date_btn)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 11, 12));
            onView(withId(android.R.id.button1)).perform(click());
        }

        // set the end time
        onView(withId(R.id.attraction_end_time_btn)).perform(nestedScrollTo());
        if (!condition.equals("noEndTime")){
            onView(withId(R.id.attraction_end_time_btn)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(8, 30));
            onView(withId(android.R.id.button1)).perform(click());
        }

        // scroll to the "add attraction" button and click it
        onView(withId(R.id.attraction_update_btn)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_update_btn)).perform(click());
        sleep(1000);
    }

    @Test
    public void textChangeListenerTests(){
        onView(withId(R.id.attraction_name_et)).perform(nestedScrollTo(), click());
        onView(withId(R.id.attraction_cost_et)).perform(nestedScrollTo(), click());
        onView(withId(R.id.attraction_description_et)).perform(nestedScrollTo());
        onView(withId(R.id.attraction_name_et)).perform(nestedScrollTo(), click());
    }

    /**
     *
     */
    private void removeAdded() throws InterruptedException {
        // find the newly made attraction and select it
        onView(isRoot()).perform(waitForView(R.id.tour_name_et, TimeUnit.SECONDS.toMillis(30)));
        onView(withId(R.id.tour_delete_btn)).perform(nestedScrollTo());
        onView(withId(R.id.tour_delete_btn)).perform(click());
    }


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

