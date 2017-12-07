package com.urizev.bakingapp.view.list;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.view.detail.RecipeDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeListActivityTest {
    private static final int ID_RECIPE_NUTELLA = 1;
    private static final int POSITION_RECIPE_NUTELLA = 0;
    private static final int POSITION_LAST_RECIPE = 3;
    private static final int EXPECTED_RECIPE_COUNT = 4;
    private static final String RECIPE_NUTELLA_PIE_NAME = "Nutella Pie";
    private static final String RECIPE_CHEESECAKE_NAME = "Cheesecake";

    @Rule
    public IntentsTestRule<RecipeListActivity> testRule = new IntentsTestRule<>(RecipeListActivity.class);
    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = testRule.getActivity().getmIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

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
        intended(allOf(hasComponent(RecipeDetailActivity.class.getName()),
                hasExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, ID_RECIPE_NUTELLA)));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
