package com.urizev.bakingapp.model;

import com.google.common.collect.ImmutableList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Creado por jcvallejo en 28/11/17.
 */

public class RecipeRepository {
    private BehaviorSubject<ImmutableList<Recipe>> recipes;
    private RecipeService recipeService;

    public RecipeRepository(RecipeService recipeService) {
        this.recipeService = recipeService;
        this.recipes = BehaviorSubject.create();
    }

    public Observable<ImmutableList<Recipe>> getRecipes() {
        Timber.d("Getting recipes");
        ObservableSource<ImmutableList<Recipe>> apiObservable;
        apiObservable = recipeService.getRecipes()
                .doOnNext(recipes -> Timber.d("Gotten recipes from network"))
                .doOnNext(recipes::onNext);
        return recipes.doOnNext(recipes -> Timber.d("Gotten recipes from cache"))
                .switchIfEmpty(apiObservable);
    }

    public Observable<Recipe> getRecipe(final int id) {
        return this.getRecipes()
                .flatMap(Observable::fromIterable)
                .filter(recipe -> id == recipe.id());
    }

    public Observable<Step> getRecipeStep(final int recipeId, final int stepId) {
        return this.getRecipe(recipeId)
                .flatMap(recipe -> Observable.fromIterable(recipe.steps()))
                .filter(step -> stepId == step.id());
    }
}
