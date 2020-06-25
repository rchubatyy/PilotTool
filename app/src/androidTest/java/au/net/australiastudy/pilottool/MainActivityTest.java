

package au.net.australiastudy.pilottool;

import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.Suppress;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.Random;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test1() throws InterruptedException {
        int[] ratings = {1,2,3,4,5,4,
        3,1,4,2,1,5,
        4,2,1,4,3,1,
        1,3,4,5,2,1,
        2,1,3,2,1,4,
        1,4,2,2,1,3,
        3,5,5,2,1,1,
        5,4,3,2,1,2};
        for (int i = 0; i < 48; i++) {

            //onView(withId(R.id.question)).check((matches(withText(containsString(i + ".")))));
            onView(withId(R.id.proRating)).perform(setRating(ratings[i]));
            Thread.sleep(1000);
        }
        onView(withId(R.id.button0)).check((matches(withText(containsString("5")))));
    }


    private ViewAction setRating(int r) {
        final int rating;
        if (r == 0)
            rating = 1;
        else rating = Math.min(r, 5);
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(SimpleRatingBar.class);
            }

            @Override
            public String getDescription() {
                return "Set rating on RatingBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                GeneralClickAction viewAction = new GeneralClickAction(
                        Tap.SINGLE,
                        new CoordinatesProvider() {
                            @Override
                            public float[] calculateCoordinates(View view) {
                                int[] locationOnScreen = new int[2];
                                view.getLocationOnScreen(locationOnScreen);
                                int screenX = locationOnScreen[0];
                                int screenY = locationOnScreen[1];
                                float x = (screenX + view.getWidth() - 1 ) / 5 * rating ;
                                float y = screenY + view.getHeight() / 2;
                                return new float[]{x, y};
                            }
                        },
                        Press.FINGER,
                        InputDevice.SOURCE_UNKNOWN,
                        MotionEvent.BUTTON_PRIMARY
                );
                viewAction.perform(uiController, view);
            }
        };
    }

}