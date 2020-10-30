package com.tourtrek;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.tourtrek.activities.MainActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.tourtrek.EspressoExtensions.waitId;
import static org.hamcrest.Matchers.allOf;

public class ProfileFragmentTest {

    public static final String TAG = "ProfileFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        // If any user is logged in, make sure to log them out
        try {
            onView(isRoot()).perform(waitId(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(15)));
            onView(withId(R.id.navigation_profile)).perform(click());
            onView(withId(R.id.profile_logout_btn)).perform(click());
        } catch (Exception NoMatchingViewException) {
            Log.w(TAG, "No user is not logged in, continuing test execution");
        } finally {
            //then log into test profile
            onView(withId(R.id.navigation_tours)).perform(click());
            onView(withId(R.id.login_email_et)).perform(typeText("robert@gmail.com"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.login_password_et)).perform(typeText("password"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.login_login_btn)).perform(click());
            onView(isRoot()).perform(waitId(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(15)));
            onView(withId(R.id.navigation_profile)).perform(click());
        }
    }
    @Test
    public void TestLogOut() {
        onView(withId(R.id.profile_logout_btn)).perform(click());
       // onData(allOf(withId(R.id.login_container), withText("Register")));
        onView(withId(R.id.login_register_btn)).check(matches(withText("Register")));
    }

//    @Test
//    public void ChangePicture() {
//        onView(withId(R.id.profile_user_iv)).perform(click());
//        onView(withId(R.id.login_register_btn)).check(matches(withText("Register")));
//    }

}
