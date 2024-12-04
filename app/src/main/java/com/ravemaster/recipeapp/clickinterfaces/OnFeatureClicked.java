package com.ravemaster.recipeapp.clickinterfaces;

import com.ravemaster.recipeapp.api.getfeed.models.Item;
import com.ravemaster.recipeapp.api.getfeed.models.Item2;
import com.ravemaster.recipeapp.api.getfeed.models.Recipe;

public interface OnFeatureClicked {
    void moveToRecipeDetails(Recipe item);
}
