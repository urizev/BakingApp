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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class NoVideoStepDetailActivityTest {
    private static final int ID_RECIPE_NUTELLA = 1;
    private static final int ID_STEP_NO_VIDEO = 1;

    @Rule
    public IntentsTestRule<RecipeStepDetailActivity> testRule = new IntentsTestRule<RecipeStepDetailActivity>(RecipeStepDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepDetailActivity.EXTRA_RECIPE_ID, ID_RECIPE_NUTELLA);
            intent.putExtra(RecipeStepDetailActivity.EXTRA_STEP_ID, ID_STEP_NO_VIDEO);
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

    @Test // Just for smartphones in landscape
    public void checkClickStepItemDoesNotDisplayPlayer_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(not(isDisplayed())));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
