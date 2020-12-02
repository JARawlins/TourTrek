package com.tourtrek;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ScrollToAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;

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

    /**
     * This method enables scrolling through a nested scroll view
     * @return
     */
    public static ViewAction nestedScrollTo(){
        return new ViewAction() {

            @SuppressWarnings("unchecked")
            @Override
            public Matcher<View> getConstraints() {
                return allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), isDescendantOfA(anyOf(
                        isAssignableFrom(ScrollView.class), isAssignableFrom(HorizontalScrollView.class), isAssignableFrom(NestedScrollView.class))));
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (isDisplayingAtLeast(90).matches(view)) {
                    Log.i(TAG, "View is already displayed. Returning.");
                    return;
                }
                Rect rect = new Rect();
                view.getDrawingRect(rect);
                if (!view.requestRectangleOnScreen(rect, true /* immediate */)) {
                    Log.w(TAG, "Scrolling to view was requested, but none of the parents scrolled.");
                }
                uiController.loopMainThreadUntilIdle();
                if (!isDisplayingAtLeast(90).matches(view)) {
                    throw new PerformException.Builder()
                            .withActionDescription(this.getDescription())
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(
                                    new RuntimeException(
                                            "Scrolling to view was attempted, but the view is not displayed"))
                            .build();
                }
            }

            @Override
            public String getDescription() {
                return "scroll to";
            }

        };

    }
}
