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

    /**
     * Action that waits for a view to become visible before continuing with execution with a delay
     * after finding the view
     *
     * @param viewId view to wait for
     * @param timeout amount of time to wait before failing test
     * @param postDelay amount of time to wait after view becomes visible
     * @return view after it is displayed
     */
    public static ViewAction waitForView(final int viewId, final long timeout, final long postDelay) {
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

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(rootView)) {

                        // found view with required ID
                        if (viewMatcher.matches(child)) {

                            final long delayStartTime = System.currentTimeMillis();
                            final long delayEndTime = delayStartTime + postDelay;

                            while (System.currentTimeMillis() < delayEndTime) {
                                uiController.loopMainThreadForAtLeast(1);
                            }

                            return;
                        }

                        uiController.loopMainThreadForAtLeast(100);
                    }
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

    /**
     * Action that waits for a view to become visible before continuing with execution
     *
     * @param viewId view to wait for
     * @param timeout amount of time to wait before failing test
     * @return view after it is displayed
     */
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

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(rootView)) {

                        // found view with required ID
                        if (viewMatcher.matches(child)) {
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
