package com.urizev.bakingapp.model;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Creado por jcvallejo en 28/11/17.
 */

@AutoValue
public abstract class Recipe {
    public abstract int id();
    public abstract String name();
    public abstract ImmutableList<Ingredient> ingredients();
    public abstract ImmutableList<Step> steps();
    public abstract int servings();
    public abstract String image();

    public static JsonAdapter<Recipe> jsonAdapter(Moshi moshi) {
        return new AutoValue_Recipe.MoshiJsonAdapter(moshi);
    }
}