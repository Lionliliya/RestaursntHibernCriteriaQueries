package com.gmail.liliyayalovchenko.controllers;

import com.gmail.liliyayalovchenko.dao.IngredientDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class IngredientController {

    private IngredientDAO ingredientDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientController.class);

    @Transactional
    public Ingredient getIngredientById(int id) {
        Ingredient ingredientById = null;
        LOGGER.info("Trying to get ingredient by id.");
        try {
            ingredientById = ingredientDAO.getIngredientById(id);
            LOGGER.info("Ingredient by id is got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cant get ingredient by id from database " + ex);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
        }
        return ingredientById;
    }

    @Transactional
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> allIngredients = null;
        LOGGER.info("Trying to get all ingredients.");
        try {
            allIngredients = ingredientDAO.getAllIngredients();
            LOGGER.info("Ingredient list is got.");
        } catch (HibernateException ex) {
            LOGGER.error("CAnt get all ingredient from database " + ex);
        }
        return allIngredients;
    }

    @Transactional
    public Ingredient getIngredientByName(String name) {
        Ingredient ingredientByName = null;
        LOGGER.info("Truing to get ingredient by name.");
        try {
            ingredientByName = ingredientDAO.getIngredientByName(name);
            LOGGER.info("Ingredient by name is got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cant get ingredient by name from database " + ex);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
        }
        return ingredientByName;
    }

    @Transactional
    public void createIngredient(Ingredient ingredient) {
        LOGGER.info("Trying to save ingredient.");
        try {
            ingredientDAO.addIngredient(ingredient);
            LOGGER.info("Ingredient is persisted.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot persist ingredient! " + ex);
        }
    }

    public void setIngredientDAO(IngredientDAO ingredientDAO) {
        this.ingredientDAO = ingredientDAO;
    }
}
