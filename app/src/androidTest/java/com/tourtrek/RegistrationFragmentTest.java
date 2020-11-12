//package com.tourtrek;
//
//import com.tourtrek.activities.MainActivity;
//
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import androidx.test.core.app.ActivityScenario;
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.filters.LargeTest;
//
//@RunWith(AndroidJUnit4.class)
//@LargeTest
//public class RegistrationFragmentTest {
//
//    public static final String TAG = "RegistrationFragmentTest";
//    private ActivityScenario mainActivityScenario;
//
//    @Rule
//    public final ActivityScenarioRule<MainActivity> mainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
//
//    @Before
//    public void setup() {
//
////        // If any user is logged in, make sure to log them out
////        try {
////            onView(isRoot()).perform(waitId(R.id.navigation_profile, TimeUnit.SECONDS.toMillis(15)));
////            onView(withId(R.id.navigation_profile)).perform(click());
////            onView(withId(R.id.profile_logout_btn)).perform(click());
////        } catch (Exception NoMatchingViewException) {
////            Log.w(TAG, "No user is not logged in, continuing test execution");
////        } finally {
////            onView(withId(R.id.navigation_tours)).perform(click());
////            onView(withId(R.id.login_register_btn)).perform(click());
////        }
//
//    }
//
//    @Test
//    public void RegisterWithNoUsername() throws InterruptedException {
////        onView(withId(R.id.register_username_et)).perform(typeText(""));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText("fakeEmail@fake.com"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("Not all fields entered")));
//    }
//    @Test
//    public void RegisterWithNoEmail() {
////        onView(withId(R.id.register_username_et)).perform(typeText("FakeUsername"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText(""));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("Not all fields entered")));
//    }
//
//    @Test
//    public void RegisterWithWrongEmail1() {
////        onView(withId(R.id.register_username_et)).perform(typeText("FakeUsername"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText("test123"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("The email address is badly formatted.")));
//    }
//
//    @Test
//    public void RegisterWithWrongEmail2() {
////        onView(withId(R.id.register_username_et)).perform(typeText("FakeUsername"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText("@hello.com"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("The email address is badly formatted.")));
//    }
//    @Test
//    public void RegisterWithNoPassword() {
////        onView(withId(R.id.register_username_et)).perform(typeText("FakeUsername"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText("fakeEmail@fake.com"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText(""));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("Not all fields entered")));
//    }
//
//    @Test
//    public void RegisterWithNoPassword2() {
////        onView(withId(R.id.register_username_et)).perform(typeText("FakeUsername"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText("fakeEmail@fake.com"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText(""));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("Not all fields entered")));
//    }
//
//    @Test
//    public void RegisterWithShortPasswords() {
////        onView(withId(R.id.register_username_et)).perform(typeText("FakeUsername"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText("fakeEmail@fake.com"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText("hey"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText("hey"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("The given password is invalid. [ Password should be at least 6 characters ]")));
//    }
//
//    @Test
//    public void RegisterWithMismatchedPasswords() {
////        onView(withId(R.id.register_username_et)).perform(typeText("FakeUsername"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_email_et)).perform(typeText("fakeEmail@fake.com"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password1_et)).perform(typeText("password"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_password2_et)).perform(typeText("pasdword"));
////        Espresso.closeSoftKeyboard();
////        onView(withId(R.id.register_register_btn)).perform(click());
////        onView(isRoot()).perform(waitForView(R.id.register_error_tv, TimeUnit.SECONDS.toMillis(1000)));
////        onView(withId(R.id.register_error_tv)).check(matches(withText("Passwords do not match")));
//    }
//
//
//}
//
//
