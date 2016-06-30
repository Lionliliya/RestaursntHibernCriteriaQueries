package com.gmail.liliyayalovchenko.dao;

import com.gmail.liliyayalovchenko.domain.Dish;

import java.util.List;

public interface DishDAO {

    void save(Dish dish);

    void removeDish(Dish dish);

    List<Dish> findAll();

    Dish getDishByName(String dishName);

    Dish getDishById(int dishId);
}
