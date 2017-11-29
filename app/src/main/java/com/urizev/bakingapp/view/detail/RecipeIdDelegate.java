package com.urizev.bakingapp.view.detail;

/**
 * Creado por jcvallejo en 29/11/17.
 */

public interface RecipeIdDelegate {
    int getRecipeId();
    int getRecipeStepId();

    void setTitle(String title);
    void showStepDetails(int stepId);
}
