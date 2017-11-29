package com.urizev.bakingapp.view.list;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.view.common.ViewState;

import javax.annotation.Nullable;

/**
 * Creado por jcvallejo en 28/11/17.
 */

@AutoValue
public abstract class RecipeListViewState implements ViewState {
    @Nullable
    public abstract ImmutableList<Recipe> recipes();
    @Nullable
    public abstract Throwable error();
    public abstract boolean loading();

    public abstract Builder toBuilder();

    static Builder builder() {
        return new AutoValue_RecipeListViewState.Builder().loading(false);
    }

    RecipeListViewState withRecipes(ImmutableList<Recipe> recipes) {
        return toBuilder().loading(false).error(null).recipes(recipes).build();
    }

    RecipeListViewState withError(Throwable error) {
        return toBuilder().loading(false).error(error).build();
    }

    RecipeListViewState withLoading() {
        return toBuilder().error(null).loading(true).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder recipes(ImmutableList<Recipe> recipes);
        public abstract Builder error(Throwable throwable);
        public abstract Builder loading(boolean loading);

        public abstract RecipeListViewState build();
    }
}
