package com.tourtrek;

import android.util.Log;
import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class EspressoExtensions {

    private final static String TAG = "EspressoExtensions";

    /** Perform action of waiting for a specific view id. */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static ViewAction waitForView(final int viewId, final long timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for a specific view with id " + viewId + " for " + timeout + "millis";
            }

            @Override
            public void perform(UiController uiController, View rootView) {

                uiController.loopMainThreadUntilIdle();

                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + timeout;
                final Matcher<View> viewMatcher = withId(viewId);

                Log.e(TAG, "startTime: " + startTime);
                Log.e(TAG, "endTime: " + endTime);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(rootView)) {

                        Log.e(TAG, "CurrentTime" + System.currentTimeMillis());

                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            Log.e(TAG, "RETURNED");
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(100);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(rootView))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
