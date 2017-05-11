package com.example.piyush.collection_vitaran;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by Harshit on 12-05-2017.
 */

@RunWith(AndroidJUnit4.class)
public class UnitTester {

    @Rule
    public ActivityTestRule<PickUp> pledgeDonationActivityTestRule = new ActivityTestRule<>(PickUp.class);

    @Before
    public void initVariables() {

    }

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {

        //Performs the click event on the spinner ,showing all the donors ,whom the collection unit is yet to arrive and collect items;
        onView(withId(R.id.spinner)).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Selects position 0 element in the spinner which implies change in the text of editText
        onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Clicks and updates the information about the donor shown in the editText ,that donation by the donor is made and successfully updated by the collection unit
        //THis results in change in the colour of the pin from red to green,confirming donation made
        onView(withId(R.id.button5)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       //Clicks when colection unit finishes all the collection work.
        //On this click event the database will be updated accordingly by the collection unit ,in shorts all the successful donations would be removed from database
        onView(withId(R.id.button4)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Clicks to look through the google map for all the pins mapped on googl map as donor location by the collection unit
        onView(withId(R.id.button3)).perform(click());

    }


}


