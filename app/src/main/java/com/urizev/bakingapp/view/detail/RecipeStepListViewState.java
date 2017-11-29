package com.urizev.bakingapp.view.detail;

import com.google.auto.value.AutoValue;
import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.view.common.ViewState;

import javax.annotation.Nullable;

@AutoValue
abstract class RecipeStepListViewState implements ViewState {
    @Nullable
    public abstract Recipe recipe();
    @Nullable
    public abstract Throwable error();
    public abstract int selectedStepId();
    public abstract boolean loading();

    public abstract RecipeStepListViewState.Builder toBuilder();

    static RecipeStepListViewState.Builder builder() {
        return new AutoValue_RecipeStepListViewState.Builder().loading(false).selectedStepId(0);
    }

    RecipeStepListViewState withSelectedStepId(int stepId) {
        return toBuilder().loading(false).error(null).selectedStepId(stepId).build();
    }

    RecipeStepListViewState withRecipe(Recipe recipe) {
        return toBuilder().loading(false).error(null).recipe(recipe).build();
    }

    RecipeStepListViewState withError(Throwable error) {
        return toBuilder().loading(false).error(error).build();
    }

    RecipeStepListViewState withLoading() {
        return toBuilder().error(null).loading(true).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract RecipeStepListViewState.Builder recipe(Recipe recipe);
        public abstract RecipeStepListViewState.Builder error(Throwable throwable);
        public abstract RecipeStepListViewState.Builder loading(boolean loading);
        public abstract RecipeStepListViewState.Builder selectedStepId(int stepId);

        public abstract RecipeStepListViewState build();
    }}
