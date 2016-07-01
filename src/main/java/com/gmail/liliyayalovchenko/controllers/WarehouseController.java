package com.gmail.liliyayalovchenko.controllers;

import com.gmail.liliyayalovchenko.dao.WarehouseDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import com.gmail.liliyayalovchenko.domain.Warehouse;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class WarehouseController {

    private WarehouseDAO warehouseDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseController.class);

    @Transactional
    public void addIngredient(Ingredient ingredient, int amount) {
        LOGGER.info("Trying to persist ingredient " + ingredient.getName() + " to warehouse.");
        try {
            warehouseDAO.addIngredient(ingredient, amount);
            LOGGER.info("Ingredient + " + ingredient.getName() + " were persisted to warehouse.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot persist ingredient to warehouse " + ex);
            System.out.println("Ingredient was not added to warehouse.");
        }
    }

    @Transactional
    public void removeIngredient(String ingredientName) {
        LOGGER.info("Trying to remove ingredient " + ingredientName);
        try {
            warehouseDAO.removeIngredient(ingredientName);
            LOGGER.info("Ingredient " + ingredientName + " was removed.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot remove ingredient from database " + ex);
            System.out.println("Cannot save ingredient " + ingredientName);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ingredientName + ex);
            System.out.println("Cannot save ingredient with name " + ingredientName);
        }
    }

    @Transactional
    public void changeAmount(Ingredient ingredient, int delta, boolean increase) {
        LOGGER.info("Trying to change amount of ingredient " + ingredient.getName());
        try {
            warehouseDAO.changeAmount(ingredient, delta, increase);
            LOGGER.info("Amount of ingredient " + ingredient.getName() + " was changed.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot change amount of ingredient " + ingredient.getName() + " in database. " + ex);
            System.out.println("Amount was not changed for ingredient " + ingredient.getName());
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
            System.out.println("Amount was not changed for ingredient " + ingredient.getName());
        }
    }

    @Transactional
    public Warehouse findByName(String ingredientName) {
        Warehouse warehouse = null;
        LOGGER.info("Trying to find ingredient " + ingredientName + " on warehouse by name.");
        try {
            warehouse = warehouseDAO.findByName(ingredientName);
            LOGGER.info("Ingredient " + ingredientName + " was found.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot find ingredient " + ingredientName + " in database " + Arrays.toString(ex.getStackTrace()));
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
        }
        return warehouse;
    }

    @Transactional
    public List<Warehouse> getAllIngredients() {
        List<Warehouse> warehouseList = null;
        LOGGER.info("Trying to get all ingredients on warehouse.");
        try {
            warehouseList = warehouseDAO.getAllIngredients();
            LOGGER.info("All ingredients on warehouse were got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get all ingredient from database " + ex);
        }
        return warehouseList;
    }

    @Transactional
    public List<Warehouse> getEndingIngredients() {
        List<Warehouse> endingIngredients = null;
        LOGGER.info("Trying to get ending ingredients.");
        try {
            endingIngredients = warehouseDAO.getEndingIngredients();
            LOGGER.info("Ending ingredients were got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get ending ingredients from database " + ex);
        }
        return endingIngredients;
    }

    @Transactional
    public boolean validateAmount(List<Ingredient> ingredients) {
        LOGGER.info("Trying to check availability of ingredients to create ready meal");
        boolean enoughIngredient = true;
        for (Ingredient ingredient : ingredients) {
            if (!warehouseDAO.checkAvailiability(ingredient)) {
                enoughIngredient = false;
                System.out.println(ingredient.getName() + " is not enough amount on warehouse");
                LOGGER.info(ingredient.getName() + " is not enough on warehouse!");
            }
        }
        return enoughIngredient;
    }

    public void setWarehouseDAO(WarehouseDAO warehouseDAO) {
        this.warehouseDAO = warehouseDAO;
    }

}
