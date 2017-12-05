package com.urizev.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.urizev.bakingapp.App;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;

/**
 * Creado por jcvallejo en 5/12/17.
 */

public class RecipeWidgetService extends IntentService {
    public static final String EXTRA_RECIPE_ID = "recipeId";

    public RecipeWidgetService() {
        super("Recipe Widget Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId < 0) {
            return;
        }

        List<String> items = ((App) getApplication())
                .getRecipeRepository()
                .getRecipe(recipeId)
                .flatMap(recipe -> Observable.fromIterable(recipe.ingredients()))
                .map(i -> String.format(Locale.getDefault(), "%.2f %s %s", i.quantity(), i.measure(), i.ingredient()))
                .toList()
                .blockingGet();
        handleUpdateWidget(items);
    }

    private void handleUpdateWidget(List<String> ingredients) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(RecipeWidgetProvider.EXTRA_INGREDIENTS_LIST, ingredients.toArray());
        sendBroadcast(intent);
    }
}
