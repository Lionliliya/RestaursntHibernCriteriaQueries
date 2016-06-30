package com.gmail.liliyayalovchenko.dao;

import com.gmail.liliyayalovchenko.domain.Ingredient;
import com.gmail.liliyayalovchenko.domain.ReadyMeal;

import java.util.List;

public interface ReadyMealDAO {

    List<ReadyMeal> getAllReadyMeals();

    void addReadyMeal(ReadyMeal meal);

    void removeReadyMeal(int orderNumber);

    void changeAmountOnWarehouse(Ingredient ingredient);
}
