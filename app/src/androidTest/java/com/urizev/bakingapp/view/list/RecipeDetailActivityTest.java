package com.urizev.bakingapp.view.list;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.detail.RecipeDetailActivity;
import com.urizev.bakingapp.view.detail.RecipeStepDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeDetailActivityTest {
    private static final int POSITION_STEP_NO_VIDEO = 10;
    private static final int ID_STEP_NO_VIDEO = 1;
    private static final int ID_RECIPE_NUTELLA = 1;

    @Rule
    public IntentsTestRule<RecipeDetailActivity> testRule = new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(targetContext, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, ID_RECIPE_NUTELLA);
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

    @Test
    public void checkStepListDisplayed() {
        onView(ViewMatchers.withId(R.id.step_list_content))
                .check(matches(isDisplayed()));
    }

    @Test // Just for tablets
    public void checkStepDetailsDisplayed_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartphones
    public void checkStepDetailsNotDisplayed_smartphone() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_include_content)).check(doesNotExist());
    }

    @Test
    public void checkStepListNotEmpty() {
        onView(ViewMatchers.withId(R.id.step_list_loading)).check(matches(not(isDisplayed())));
        onView(ViewMatchers.withId(R.id.step_list_error)).check(matches(not(isDisplayed())));
        onView(ViewMatchers.withId(R.id.step_list_content))
                .check(RecyclerViewAssertions.containsItemCount(16));
    }

    @Test // Just for smartphones
    public void checkStepClickLaunchActivity_smartphone() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.scrollToPosition(POSITION_STEP_NO_VIDEO))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_NO_VIDEO, click()));
        intended(allOf(hasComponent(RecipeStepDetailActivity.class.getName()),
                hasExtra(RecipeStepDetailActivity.EXTRA_RECIPE_ID, ID_RECIPE_NUTELLA),
                hasExtra(RecipeStepDetailActivity.EXTRA_STEP_ID, ID_STEP_NO_VIDEO)));
    }

    @Test // Just for tablets
    public void checkClickStepItemDisplayPlayer_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(isDisplayed()));
    }

    @Test // Just for tablets
    public void checkClickStepItemDisplayDescription_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_description))
                .check(matches(isDisplayed()));
    }

    @Test // Just for tablets
    public void checkNavigationNotDisplayed_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_navigation))
                .check(doesNotExist());
    }

    @Test // Just for tablets
    public void checkPlayerNotDisplayed_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_NO_VIDEO, click()));
        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(not(isDisplayed())));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
