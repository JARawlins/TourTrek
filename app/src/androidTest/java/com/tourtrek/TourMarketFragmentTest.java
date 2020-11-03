package com.tourtrek;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.tourtrek.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class TourMarketFragmentTest {
    public static final String TAG = "RegistrationFragmentTest";
    private ActivityScenario mainActivityScenario;

    @Rule
    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {

//        // If any user is logged in, make sure to log them out
//        try {
//            onView(isRoot()).perform(waitForView(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(15)));
//            onView(withId(R.id.navigation_profile)).perform(click());
//            onView(withId(R.id.profile_logout_btn)).perform(click());
//        } catch (Exception NoMatchingViewException) {
//            Log.w(TAG, "No user is not logged in, continuing test execution");
//        } finally {
//            //then log into test profile
//            onView(withId(R.id.navigation_tours)).perform(click());
//            onView(withId(R.id.login_email_et)).perform(typeText("robert@gmail.com"));
//            onView(withId(R.id.login_password_et)).perform(typeText("password"));
//            onView(withId(R.id.login_register_btn)).perform(click());
//            onView(withId(R.id.navigation_tour_market)).perform(click());
//        }

    }

//    @Test
//    public void TourInfoDisplay() throws InterruptedException {
//        onView(withId(R.id.item_tour_cover_iv)).perform(click());
//       onView(withId(R.id.personal_future_tours_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//    }
}
