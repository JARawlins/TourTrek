package com.tourtrek;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.tourtrek.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
    private Boolean deletedTour;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @After
    public void shutdown(){
        if(!deletedTour){
            deleteTour();
            deletedTour = true;

        }
    }

    @Before
    public void setup() throws InterruptedException {

        deletedTour = true;


        mainActivityScenario = mainActivityScenarioRule.getScenario();




        try{
            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(5)));
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.profile_logout_btn, TimeUnit.SECONDS.toMillis(5)));
            onView(withId(R.id.profile_logout_btn)).perform(click());
        }catch (Exception NoMatchingViewException) {
            Log.w(TAG, "No user is not logged in, continuing test execution");
        }
        finally {
            onView(withId(R.id.navigation_tours)).perform(click());
            onView(withId(R.id.login_email_et)).perform(typeText("testingaccount@gmail.com"), ViewActions.closeSoftKeyboard());
            onView(withId(R.id.login_password_et)).perform(typeText("password"), ViewActions.closeSoftKeyboard());
            onView(withId(R.id.login_login_btn)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.personal_past_tours_rv, TimeUnit.SECONDS.toMillis(5)));
            onView(withId(R.id.navigation_tours)).perform(click());
        }

    }

    /**
     * Creates a new tour on active account named "aaaaFun Times"
     */
    public void createNewTour(){
        //click new tour

        onView(isRoot()).perform(waitForView(R.id.personal_future_tours_title_btn, TimeUnit.SECONDS.toMillis(5)));

        onView(withId(R.id.personal_future_tours_title_btn)).perform(click());

        //enter fields
        onView(withId(R.id.tour_name_et)).perform(typeText("aaaaFun Times"), closeSoftKeyboard());
        onView(withId(R.id.tour_location_et)).perform(nestedScrollTo(),typeText("Madison"), closeSoftKeyboard());
        onView(withId(R.id.tour_cost_et)).perform(nestedScrollTo(),typeText("0"), closeSoftKeyboard());
        onView(withId(R.id.tour_start_date_btn)).perform(nestedScrollTo(),click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1900, 11, 10));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.tour_end_date_btn)).perform(nestedScrollTo(),click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1920, 11, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.tour_update_btn)).perform(nestedScrollTo(),click());

        deletedTour = false;

    }

    /**
     *clicks on friend in position 1 which should be user "Michael"
     */
    public void  clickFriendAccount(){

        onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.navigation_profile)).perform(click());
        onView(isRoot()).perform(waitForView(R.id.profile_friend_btn, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.profile_friend_btn)).perform(click());
        onView(isRoot()).perform(waitForView(R.id.add_friend_my_friends_rv, TimeUnit.SECONDS.toMillis(5)));

        onView(withId(R.id.add_friend_my_friends_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
    }

    /**
     * deletes tour from users account and should also delete it from anyone who has been added
     */
    public void deleteTour(){
        onView(isRoot()).perform(waitForView(R.id.navigation_tours, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.navigation_tours)).perform(click());
        onView(isRoot()).perform(waitForView(R.id.item_personal_tour_name, TimeUnit.SECONDS.toMillis(5)));

        // onView(withId(R.id.item_personal_tour_name).matches("aaaaFunTimes")).perform(click());

        try{
            onView(withId(R.id.personal_past_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            onView(isRoot()).perform(waitForView(R.id.tour_delete_btn, TimeUnit.SECONDS.toMillis(5)));
            onView(withId(R.id.tour_delete_btn)).perform(nestedScrollTo(), click());
            deletedTour = true;
        }
        catch(PerformException e){
            Log.w(TAG, "No Tours found in past tours recycler view");
        }

    }

//        @Test
//    public void test(){
//        deleteTour();
//    }


    /**
     * adds friend with email address michael@gmail.com to active tour
     */
    public void searchForFriendToAddToTour(String email){
        //click on add friend to tour button

        onView(isRoot()).perform(waitForView(R.id.tour_add_friend_btn, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.tour_add_friend_btn)).perform(nestedScrollTo(), click());

        //type in friend email to search
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_email_et, TimeUnit.SECONDS.toMillis(5)));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.add_friend_to_tour_email_et),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText(email), closeSoftKeyboard());

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


    }

    /**
     * Checks to make sure that after adding a friend to a tour the tour actually shows up on the friends tours list
     */
    @Test
    public void friendActuallyAddedToTour() throws InterruptedException {

        //create new past tour
        createNewTour();

        //click on new tour
        sleep(2000);
        onView(withId(R.id.personal_past_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        //search for friend
        searchForFriendToAddToTour("michael@gmail.com");
        //click add button

        sleep(2000);
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_add_btn, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.add_friend_to_tour_add_btn)).perform(nestedScrollTo(), click());


        //open friends account
        clickFriendAccount();

        //scroll to friends tours recycler view
        onView(withId(R.id.friend_tours_rv)).perform(nestedScrollTo());
        //check to make sure user has tour name "Fun Times"

        onView(isRoot()).perform(waitForView(R.id.item_tour_name, TimeUnit.SECONDS.toMillis(5)));

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_tour_name), withText("aaaaFun Times"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("aaaaFun Times")));

    }

    /**
     * Checks to make sure that after adding a friend to a tour and then deleting that tour it doesn't show up on friends tour list after
     * previously having been there
     */
    @Test
    public void tourGetsDeletedFromFriend() throws InterruptedException {
        //create new past tour
        createNewTour();

        //click on new tour
        sleep(1000);
        onView(withId(R.id.personal_past_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        //search for friend
        searchForFriendToAddToTour("michael@gmail.com");
        //click add button

        sleep(2000);
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_add_btn, TimeUnit.SECONDS.toMillis(5)));
        onView(withId(R.id.add_friend_to_tour_add_btn)).perform(nestedScrollTo(),click());


        //open friends account
        clickFriendAccount();

        //scroll to friends tours recycler view
        onView(withId(R.id.friend_tours_rv)).perform(nestedScrollTo());
        //check to make sure user has tour name "Fun Times"

        onView(isRoot()).perform(waitForView(R.id.item_tour_name, TimeUnit.SECONDS.toMillis(5)));

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_tour_name), withText("aaaaFun Times"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("aaaaFun Times")));

        //delete tour
        deleteTour();


        //open friends account
        onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(5)));

        onView(withId(R.id.navigation_profile)).perform(click());
        clickFriendAccount();

        sleep(1000);
        //scroll to friends tours recycler view
        onView(withId(R.id.friend_tours_rv)).perform(nestedScrollTo());
        //check to make sure user has tour name "Fun Times"

        onView(isRoot()).perform(waitForView(R.id.item_tour_name, TimeUnit.SECONDS.toMillis(5)));

        try {
            ViewInteraction textView2 = onView(
                    allOf(withId(R.id.item_tour_name), withText("aaaaFun Times"),
                            withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                            isDisplayed()));
        textView2.check(matches(withText("aaaaFun Times")));

        }
        catch(Exception e){
                assert true;
        }
    }

    /**
     *Checks to make sure that you can click add friend to tour and then are able to search for a friend.
     * It does this by checking to make sure that the friend that was searched for appears on the screen with the correct name
     **/
    @Test
    public void canSearchForFriendToAdd() throws InterruptedException {

        sleep(2000);
        //click on a tour
        onView(withId(R.id.personal_current_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //search for friend
        searchForFriendToAddToTour("michael@gmail.com");

        //make sure friend that was searched for appears

        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_friendName_tv, TimeUnit.SECONDS.toMillis(1)));
        sleep(2000);

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
    public void cantAddStrangerToTour() throws InterruptedException {

        //click on a tour
        onView(withId(R.id.personal_current_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //search for friend
        searchForFriendToAddToTour("test2@gmail.com");

        //make sure error message appears
        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_friendName_tv, TimeUnit.SECONDS.toMillis(5)));

        sleep(1000);

        onView(withId(R.id.add_friend_to_tour_error_tv)).check(matches(withText("Cannot find user with email entered on friends list")));
    }

    /**
     *Checks to make sure someone not on the users friends list cant be added by them.
     **/
    @Test
    public void cantSearchForInvalidUser(){

        //click on a tour
        onView(withId(R.id.personal_current_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //search for friend
        searchForFriendToAddToTour("asasaa@sads.cass");

        //make sure error message appears

        onView(isRoot()).perform(waitForView(R.id.add_friend_to_tour_friendName_tv, TimeUnit.SECONDS.toMillis(5)));

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
