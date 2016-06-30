package com.gmail.liliyayalovchenko.controllers;

import com.gmail.liliyayalovchenko.dao.ReadyMealDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import com.gmail.liliyayalovchenko.domain.ReadyMeal;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ReadyMealController {

    private ReadyMealDAO readyMealDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadyMealController.class);

    @Transactional
    public List<ReadyMeal> getAllReadyMeals() {
        List<ReadyMeal> allReadyMeals = null;
        LOGGER.info("Trying to get all all ready meals from database.");
        try {
            allReadyMeals = readyMealDAO.getAllReadyMeals();
            LOGGER.info("List of ready meals were got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cant get all ready meals from database. " + ex);
        }
        return allReadyMeals;
    }

    @Transactional
    public void addReadyMeal(ReadyMeal meal) {
        LOGGER.info("Trying to persist meal.");
        try {
            readyMealDAO.addReadyMeal(meal);
            for (Ingredient ingredient : meal.getDishId().getIngredients()) {
                String ingredientName = ingredient.getName();
                LOGGER.info(ingredientName + " try to change amount");
                readyMealDAO.changeAmountOnWarehouse(ingredient);
                LOGGER.info(ingredientName + " amount was successfully changed");
            }

            LOGGER.info("Ready meal was persisted to database.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot persist ready meal " + ex);
        }
    }

    @Transactional
    public void removeReadyMeal(int orderNumber1) {
        LOGGER.info("Trying  to remove ready meal from order " + orderNumber1);
        try {
            readyMealDAO.removeReadyMeal(orderNumber1);
            LOGGER.info("Ready meal was removed from order " + orderNumber1);
        } catch (HibernateException ex) {
            LOGGER.error("Cannot remove ready meal from order " + orderNumber1 + ex);
        }
    }



    public void setReadyMealDAO(ReadyMealDAO readyMealDAO) {
        this.readyMealDAO = readyMealDAO;
    }



}
