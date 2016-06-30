package com.gmail.liliyayalovchenko.dao;


import com.gmail.liliyayalovchenko.domain.Ingredient;
import com.gmail.liliyayalovchenko.domain.Warehouse;

import java.util.List;

public interface WarehouseDAO {

    void addIngredient(Ingredient ingredient, int amount);

    void removeIngredient(String ingredientName);

    void changeAmount(Ingredient ingredient, int delta, boolean increase);

    Warehouse findByName(String ingredientName);

    List<Warehouse> getAllIngredients();

    List<Warehouse> getEndingIngredients();

    boolean alreadyExist(Ingredient ingredient);

    boolean checkAvailiability(Ingredient ingredient);
}
