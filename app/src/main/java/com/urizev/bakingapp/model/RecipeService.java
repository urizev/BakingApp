package com.urizev.bakingapp.model;

import com.google.common.collect.ImmutableList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public interface RecipeService {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Observable<ImmutableList<Recipe>> getRecipes();
}
