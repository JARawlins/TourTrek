package com.tourtrek;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.tourtrek.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tourtrek.EspressoExtensions.nestedScrollTo;
import static com.tourtrek.EspressoExtensions.waitForView;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;

public class FriendProfileFragmentTest {

    public static final String TAG = "FriendProfileFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    @Before
    public void setup() throws InterruptedException {

        mainActivityScenario = mainActivityScenarioRule.getScenario();

        // If any user is logged in, make sure to log them out
        sleep(1000);
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
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(isRoot()).perform(waitForView(R.id.profile_friend_btn, TimeUnit.SECONDS.toMillis(1000)));
            onView(withId(R.id.profile_friend_btn)).perform(click());

    }
    /**
    *This test checks to make sure you can view a friend's friends by checking a specific profile for a specific friend
     *
     * This test depends on the testing accounts second friend in the recycler view being an account named "Michael" that contains
     *  a friend named "Testing Account"
     **/
    @Test
    public void viewFriendsFriends(){
        //click on friend at position 1 inside recyclerview
        onView(isRoot()).perform(waitForView(R.id.add_friend_my_friends_rv, TimeUnit.SECONDS.toMillis(1000)));
        onView(withId(R.id.add_friend_my_friends_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        //make sure that this friends list contains the friend "Testing Account"
        onView(isRoot()).perform(waitForView(R.id.item_friend_friendName_tv, TimeUnit.SECONDS.toMillis(1000)));
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_friend_friendName_tv), withText("Testing Account"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Testing Account")));
    }

    /**
     *This test checks to make sure you can view a friend's tours by checking a specific profile for a specific tour
     *
     * This test depends on the testing accounts second friend in the recycler view being an account named "Michael" that contains
     * the tour named "Triple D"
     **/
    @Test
    public void viewFriendsTours(){
        //click on friend at position 1 in friends list
        onView(isRoot()).perform(waitForView(R.id.add_friend_my_friends_rv, TimeUnit.SECONDS.toMillis(1000)));
        onView(withId(R.id.add_friend_my_friends_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        //scroll to friends tours recycler view
        onView(withId(R.id.friend_tours_rv)).perform(nestedScrollTo());

        //check to make sure user has tour name "Triple D"
        onView(isRoot()).perform(waitForView(R.id.item_tour_name, TimeUnit.SECONDS.toMillis(1000)));
        ViewInteraction textView = onView(
                allOf(withId(R.id.item_tour_name), withText("Triple D"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Triple D")));
    }
}
