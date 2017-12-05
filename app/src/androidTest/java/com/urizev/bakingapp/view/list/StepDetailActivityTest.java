package com.urizev.bakingapp.view.list;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.detail.RecipeStepDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepDetailActivityTest {
    private static final int ID_RECIPE_NUTELLA = 1;
    private static final int ID_STEP_WITH_VIDEO = 0;

    @Rule
    public IntentsTestRule<RecipeStepDetailActivity> testRule = new IntentsTestRule<RecipeStepDetailActivity>(RecipeStepDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepDetailActivity.EXTRA_RECIPE_ID, ID_RECIPE_NUTELLA);
            intent.putExtra(RecipeStepDetailActivity.EXTRA_STEP_ID, ID_STEP_WITH_VIDEO);
            return intent;
        }
    };

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = testRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test // Just for tablets
    public void checkStepDetailsDisplayed_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartphones
    public void checkClickStepItemOpenStepDetails_smartphone() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartphones
    public void checkClickStepItemDisplayNavigation_smartphone() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.step_detail_navigation))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartphones in landscape
    public void checkClickStepItemDoesNotDisplayDescription_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.step_detail_description))
                .check(doesNotExist());
    }

    @Test // Just for smartphones in landscape
    public void checkClickStepItemDoesNotDisplayNavigation_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.step_detail_navigation))
                .check(doesNotExist());
    }

    @Test // Just for smartphones in landscape
    public void checkClickStepItemDisplayPlayer_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
