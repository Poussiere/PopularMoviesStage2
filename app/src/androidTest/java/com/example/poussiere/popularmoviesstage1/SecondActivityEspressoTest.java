package com.example.poussiere.popularmoviesstage1;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;



@RunWith(AndroidJUnit4.class)
public class SecondActivityEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void testReviewDisplay()
    {
        onView(withId(R.id.posters_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.note_contenair)).perform(click());
        onView(withId(R.id.reviews_recycler_view)).check(matches(isDisplayed()));
    }
}
