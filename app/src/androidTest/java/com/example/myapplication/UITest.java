package com.example.myapplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testKeywordBased() {
        onView(withId(R.id.buttonSnap)).perform(click());
        onView(withId(R.id.buttonSearch)).perform(click());

        onView(withId(R.id.keywordsEditText)).perform(typeText("roof"), closeSoftKeyboard());
        onView(withId(R.id.searchViewBtn)).perform(click());
        onView(withId(R.id.edit_Add_Captions)).check(matches(withText("my roof on the top"))); //also will fail

        //onView(withId(R.id.keywordsEditText)).perform(typeText("Blue"), closeSoftKeyboard());
       // onView(withId(R.id.searchViewBtn)).perform(click());
        //onView(withId(R.id.edit_Add_Captions)).check(matches(withText("Blue"))); //also will fail

    }

   @Test
    public void testTimeBased() {
        onView(withId(R.id.buttonSearch)).perform(click());
        onView(withId(R.id.keywordsEditText)).perform(typeText("roof"), closeSoftKeyboard());
        onView(withId(R.id.editTextFromDate)).perform(typeText("20200918"), closeSoftKeyboard());
        onView(withId(R.id.editTextToDate)).perform(typeText("20200922"), closeSoftKeyboard());
        onView(withId(R.id.searchViewBtn)).perform(click());
        onView(withId(R.id.textViewTimeStamp)).check(matches(withText("20200921_172532")));
        //this will fail, but it should grab the photo, then compare the timestamp on the photo with the range that was given
        //check database or imageview for any images that within time index
        //check that the to date is bigger than the from date.
    }
}
