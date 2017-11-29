package com.urizev.bakingapp.view.detail;

import com.google.auto.value.AutoValue;
import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.model.Step;
import com.urizev.bakingapp.view.common.ViewState;

import javax.annotation.Nullable;

@AutoValue
public abstract class RecipeStepDetailViewState implements ViewState {
    @Nullable
    public abstract Step step();
    @Nullable
    public abstract Throwable error();
    public abstract boolean loading();

    public abstract RecipeStepDetailViewState.Builder toBuilder();

    static RecipeStepDetailViewState.Builder builder() {
        return new AutoValue_RecipeStepDetailViewState.Builder().loading(false);
    }

    RecipeStepDetailViewState withStep(Step step) {
        return toBuilder().loading(false).error(null).step(step).build();
    }

    RecipeStepDetailViewState withError(Throwable error) {
        return toBuilder().loading(false).error(error).build();
    }

    RecipeStepDetailViewState withLoading() {
        return toBuilder().error(null).loading(true).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract RecipeStepDetailViewState.Builder step(Step step);
        public abstract RecipeStepDetailViewState.Builder error(Throwable throwable);
        public abstract RecipeStepDetailViewState.Builder loading(boolean loading);

        public abstract RecipeStepDetailViewState build();
    }
}
