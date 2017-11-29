package com.urizev.bakingapp.model;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Creado por jcvallejo en 28/11/17.
 */

@AutoValue
public abstract class Ingredient {
    public abstract float quantity();
    public abstract String measure();
    public abstract String ingredient();

    public static Ingredient create(float quantity, String measure, String ingredient) {
        return new AutoValue_Ingredient(quantity, measure, ingredient);
    }

    public static JsonAdapter<Ingredient> jsonAdapter(Moshi moshi) {
        return new AutoValue_Ingredient.MoshiJsonAdapter(moshi);
    }
}
