package com.urizev.bakingapp.view.list;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.detail.RecipeDetailActivity;
import com.urizev.bakingapp.view.detail.RecipeStepDetailActivity;

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
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityTest {
    private static final int POSITION_STEP_WITH_VIDEO = 9;
    private static final int POSITION_STEP_NO_VIDEO = 10;
    private static final int POSITION_RECIPE_NUTELLA = 0;
    private static final int POSITION_LAST_RECIPE = 3;
    private static final int EXPECTED_RECIPE_COUNT = 4;
    private static final String RECIPE_NUTELLA_PIE_NAME = "Nutella Pie";
    private static final String RECIPE_CHEESECAKE_NAME = "Cheesecake";

    @Rule
    public IntentsTestRule<RecipeListActivity> testRule = new IntentsTestRule<>(RecipeListActivity.class);



    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void checkNotEmptyRecipeList() {
        onView(withId(R.id.recipe_list_content)).check(RecyclerViewAssertions.containsItemCount(EXPECTED_RECIPE_COUNT));
        onView(withText(RECIPE_NUTELLA_PIE_NAME)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_list_content)).perform(RecyclerViewActions.scrollToPosition(POSITION_LAST_RECIPE));
        onView(withText(RECIPE_CHEESECAKE_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void checkClickRecipeItemOpenRecipeDetails() {
        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_list_content))
                .check(matches(isDisplayed()));
    }

    @Test // Just for tablets
    public void checkClickRecipeItemOpenStepDetails_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartphones
    public void checkClickRecipeItemDoesNotOpenStepDetails() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(doesNotExist());
    }

    @Test // Just for smartphones
    public void checkClickStepItemOpenStepDetails_smartphone() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(doesNotExist());
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_WITH_VIDEO, click()));
        intended(hasComponent(RecipeStepDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartpones
    public void checkClickStepItemDisplayNavigation_smartphone() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(doesNotExist());
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_WITH_VIDEO, click()));
        intended(hasComponent(RecipeStepDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_navigation))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartpones in landscape
    public void checkClickStepItemDoesNotDisplayDescription_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(doesNotExist());
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_WITH_VIDEO, click()));
        intended(hasComponent(RecipeStepDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_description))
                .check(doesNotExist());
    }

    @Test // Just for smartpones in landscape
    public void checkClickStepItemDoesNotDisplayNavigation_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(doesNotExist());
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_WITH_VIDEO, click()));
        intended(hasComponent(RecipeStepDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_navigation))
                .check(doesNotExist());
    }

    @Test // Just for smartpones in landscape
    public void checkClickStepItemDisplayPlayer_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(doesNotExist());
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_WITH_VIDEO, click()));
        intended(hasComponent(RecipeStepDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(isDisplayed()));
    }

    @Test // Just for smartpones in landscape
    public void checkClickStepItemDoesNotDisplayPlayer_smartphoneLandscape() {
        assumeFalse(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.landscape));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(doesNotExist());
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_NO_VIDEO, click()));
        intended(hasComponent(RecipeStepDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(not(isDisplayed())));
    }

    @Test // Just for tablets
    public void checkClickStepItemDisplayPlayer_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(isDisplayed()));
    }

    @Test // Just for tablets
    public void checkClickStepItemDisplayDescription_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_description))
                .check(matches(isDisplayed()));
    }

    @Test // Just for tablets
    public void checkClickStepItemDoesNotDisplayNavigation_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_navigation))
                .check(doesNotExist());
    }

    @Test // Just for tablets
    public void checkClickStepItemDoesNotDisplayPlayer_tablet() {
        assumeTrue(testRule.getActivity().getResources().getBoolean(R.bool.twoPane));

        onView(ViewMatchers.withId(R.id.recipe_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_RECIPE_NUTELLA, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(ViewMatchers.withId(R.id.step_detail_include_content))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.step_list_content))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION_STEP_NO_VIDEO, click()));
        onView(ViewMatchers.withId(R.id.step_detail_player))
                .check(matches(not(isDisplayed())));
    }
}
