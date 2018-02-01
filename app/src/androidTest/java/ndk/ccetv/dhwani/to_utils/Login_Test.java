package ndk.ccetv.dhwani.to_utils;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import ndk.ccetv.dhwani.R;
import ndk.ccetv.dhwani.activities.Registers;
import ndk.ccetv.dhwani.constants.Application_Specification;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class Login_Test {
    @Rule
    public ActivityTestRule<Login> mActivityRule = new ActivityTestRule<>(
            Login.class);

    @Test
    public void check_login() {
        // Type text and then press the button.
        onView(withId(R.id.username)).perform(typeText("v"));
        onView(withId(R.id.passcode)).perform(typeText("v"));
        onView(withId(R.id.sign_in_button)).perform(click());

        Context appContext = InstrumentationRegistry.getTargetContext();
        if (getActivityInstance().getClass().equals(Registers.class)) {
            assertEquals("1", appContext.getSharedPreferences(Application_Specification.APPLICATION_NAME, Context.MODE_PRIVATE).getString("id", "0"));
            assertEquals("Core Coordinator", appContext.getSharedPreferences(Application_Specification.APPLICATION_NAME, Context.MODE_PRIVATE).getString("role", "Unknown"));
        }

    }

    @After
    public void observe() {
    }

    public Activity getActivityInstance() {
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                for (Activity act : resumedActivities) {
                    Log.d("Your current activity: ", act.getClass().getName());
                    currentActivity[0] = act;
                    break;
                }
            }
        });

        return currentActivity[0];
    }


}