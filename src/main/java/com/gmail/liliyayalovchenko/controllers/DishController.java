package com.gmail.liliyayalovchenko.controllers;

import com.gmail.liliyayalovchenko.dao.DishDAO;
import com.gmail.liliyayalovchenko.domain.Dish;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DishController {

    private DishDAO dishDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(DishController.class);


    @Transactional
    public void createDish(Dish dish) {
       LOGGER.info("Start persisting dish.");
       try {
           dishDAO.save(dish);
           LOGGER.info("Dish is persisted.");
       } catch (HibernateException ex) {
           LOGGER.error("Cant save dish to database! " + ex);
       }
    }

    @Transactional
    public List<Dish> getAllDishes() {
        List<Dish> allDish = null;
        LOGGER.info("Start getting all dishes.");
        try {
            allDish = dishDAO.findAll();
            LOGGER.info("All dishes are got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cant get all dishes from database " + ex);
        }
        return allDish;
    }

    @Transactional
    public void printAllDishes() {
        LOGGER.info("Start to print all dishes");
        List<Dish> allDishes = getAllDishes();
        if (null != allDishes) {
            allDishes.forEach(System.out::println);
            LOGGER.info("All dishes are printed");
        } else {
            LOGGER.info("Could not get dish list from database.");
        }
    }

    @Transactional
    public Dish getDishByName(String dishName) {
       LOGGER.info("Try to get dish by name.");
       try {
           Dish dish = dishDAO.getDishByName(dishName);
           LOGGER.info("Dish is got by name.");
           return dish;
       } catch (HibernateException ex) {
           LOGGER.error("Hibernate exception! " + ex);
       } catch (RuntimeException ex) {
           LOGGER.error("Error! " + ex);
       }
       return null;
    }

    @Transactional
    public void removeDish(Dish dish) {
        LOGGER.info("Start to removing dish.");
        try {
            dishDAO.removeDish(dish);
            LOGGER.info("Dish is removed");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot remove dish! " + ex);
        }
    }

    @Transactional
    public Dish getDishById(int dishId) {
       LOGGER.info("Start getting dish by id.");
       try {
           Dish dishById = dishDAO.getDishById(dishId);
           LOGGER.info("Dish by id is got.");
           return dishById;
       } catch (HibernateException ex) {
           LOGGER.error("Cannot get dish from database " + ex);
       } catch (RuntimeException ex) {
           LOGGER.error("Error! " + ex);
       }
       return null;
    }


    public void setDishDAO(DishDAO dishDAO) {
        this.dishDAO = dishDAO;
    }

}
