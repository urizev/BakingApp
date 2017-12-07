package com.urizev.bakingapp.view.detail;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.urizev.bakingapp.R;
import com.urizev.bakingapp.model.Ingredient;
import com.urizev.bakingapp.model.Recipe;
import com.urizev.bakingapp.model.Step;

import java.lang.ref.WeakReference;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Creado por jcvallejo en 29/11/17.
 */

class RecipeStepListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CELL_STEP = 0;
    private static final int CELL_INGREDIENT = 1;
    private static final int CELL_INGREDIENT_TOP = 2;
    private static final int CELL_INGREDIENT_BOTTOM = 3;

    private Recipe mRecipe;

    private final boolean mHighlightSelectedStep;
    private final WeakReference<RecipeStepListAdapterDelegate> mDelegate;

    RecipeStepListAdapter(RecipeStepListAdapterDelegate delegate, boolean highlightSelectedStep) {
        this.mDelegate = new WeakReference<>(delegate);
        this.mHighlightSelectedStep = highlightSelectedStep;
    }

    void update(Recipe recipe) {
        this.mRecipe = recipe;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CELL_INGREDIENT_TOP;
        }
        else if (position < mRecipe.ingredients().size() - 1) {
            return CELL_INGREDIENT;
        }
        else if (position == mRecipe.ingredients().size() - 1) {
            return CELL_INGREDIENT_BOTTOM;
        }
        else {
            return CELL_STEP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CELL_INGREDIENT:
            case CELL_INGREDIENT_TOP:
            case CELL_INGREDIENT_BOTTOM:
                return new RecipeStepListAdapter.IngredientViewHolder(inflater.inflate(R.layout.cell_ingredient, parent, false), viewType);
            case CELL_STEP:
                return new RecipeStepListAdapter.StepViewHolder(inflater.inflate(R.layout.cell_step, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mRecipe.ingredients().size()) {
            ((IngredientViewHolder) holder).bind(mRecipe.ingredients().get(position));
            return;
        }

        position -= mRecipe.ingredients().size();
        ((StepViewHolder) holder).bind(mRecipe.steps().get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipe != null ? mRecipe.ingredients().size() + mRecipe.steps().size() : 0;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_description) TextView description;
        @BindView(R.id.ingredient_card) View card;
        IngredientViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
            if (viewType != CELL_INGREDIENT) {
                setMargins(viewType);
            }
        }

        private void setMargins(int viewType) {
            int top = viewType == CELL_INGREDIENT_TOP ? (int) itemView.getResources().getDimension(R.dimen.cell_ingrediente_margin_vertical) : 0;
            int bottom = viewType == CELL_INGREDIENT_BOTTOM ? (int) itemView.getResources().getDimension(R.dimen.cell_ingrediente_margin_vertical) : 0;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) card.getLayoutParams();
            int left = params.leftMargin;
            int right = params.rightMargin;
            params.setMargins(left, top, right, bottom);
            card.setLayoutParams(params);
            description.setPadding(left, top, right, bottom);
        }

        void bind(Ingredient ingredient) {
            this.description.setText(String.format(Locale.getDefault(), "%.2f %s %s", ingredient.quantity(), ingredient.measure(), ingredient.ingredient()));
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_description) TextView description;
        private int stepId;
        private final int selectionColor;

        StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            this.selectionColor = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryLight);
        }

        void bind(Step step) {
            this.stepId = step.id();
            this.description.setText(step.shortDescription());

            if (mHighlightSelectedStep) {
                RecipeStepListAdapterDelegate d = mDelegate.get();
                if (d != null) {
                    int stepId = d.getSelectedStepId();
                    itemView.setBackgroundColor(stepId == this.stepId ? selectionColor : Color.TRANSPARENT);
                }
            }
        }

        @Override
        public void onClick(View view) {
            RecipeStepListAdapterDelegate d = mDelegate.get();
            if (d != null) {
                d.onStepClicked(stepId);
            }
        }
    }

    interface RecipeStepListAdapterDelegate {
        void onStepClicked(int stepId);
        int getSelectedStepId();
    }
}
