package com.urizev.bakingapp.model;

import com.google.common.collect.ImmutableList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.subjects.BehaviorSubject;

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
        ObservableSource<ImmutableList<Recipe>> apiObservable;
        apiObservable = recipeService.getRecipes()
                .doOnNext(recipes::onNext);
        Observable<ImmutableList<Recipe>> cacheObservable = recipes.hasValue()
                ? Observable.just(recipes.getValue())
                : Observable.empty();

        return cacheObservable.switchIfEmpty(apiObservable);
    }

    public Observable<Recipe> getRecipe(final int id) {
        return this.getRecipes()
                .flatMap(Observable::fromIterable)
                .filter(recipe -> id == recipe.id());
    }
}
