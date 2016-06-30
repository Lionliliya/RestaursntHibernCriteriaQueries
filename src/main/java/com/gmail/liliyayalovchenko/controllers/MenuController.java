package com.gmail.liliyayalovchenko.controllers;

import com.gmail.liliyayalovchenko.dao.MenuDAO;
import com.gmail.liliyayalovchenko.domain.Dish;
import com.gmail.liliyayalovchenko.domain.Menu;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MenuController {

    private MenuDAO menuDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);


    @Transactional
    public void addNewMenu(String menuName, List<Dish> dishList) {
        LOGGER.info("Trying to persist menu");
        try {
            menuDAO.addNewMenu(menuName, dishList);
            LOGGER.info("Menu is persisted.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot persist menu to database! " + ex);
        }
    }

    @Transactional
    public void createMenu(Menu menu) {
        LOGGER.info("Trying to persist menu");
        try {
            menuDAO.createMenu(menu);
            LOGGER.info("Menu is persisted.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot persist menu to database! " + ex);
        }
    }

    @Transactional
    public void removeMenu(Menu menu) {
        LOGGER.info("Trying to remove menu.");
        try {
            menuDAO.removeMenu(menu);
            LOGGER.info("Menu is removed");
        } catch (HibernateException ex) {
            LOGGER.error("Cant remove menu from database " + ex);
        }
    }

    @Transactional
    public Menu getMenuByName(String name) {
        Menu menuByName = null;
        LOGGER.info("Trying to get menu by name");
        try {
            menuByName = menuDAO.getMenuByName(name);
            LOGGER.info("Menu is got");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get menu by name from database " + ex);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
        }
        return menuByName;
    }

    @Transactional
    public void addDishToMenu(int menuId, Dish dish) {
        LOGGER.info("Trying to add dish to menu");
        try {
            menuDAO.addDishToMenu(menuId, dish);
            LOGGER.info("Dish to menu is added.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get menu by id from database " + ex);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
        }
    }

    @Transactional
    public void removeDishFromMenu(int menuId, Dish dish) {
        LOGGER.info("Trying to remove dish from menu.");
        try {
            menuDAO.removeDishFromMenu(menuId, dish);
            LOGGER.info("Dish was removed from menu.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot remove dish from menu " + ex);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input " + ex);
        }
    }

    @Transactional
    public void showAllMenus() {
        LOGGER.info("Trying to show all menus");
        try {
            menuDAO.showAllMenus();
            LOGGER.info("List of menu was shown.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get all menus from database " + ex);
        }
    }

    @Transactional
    public void printMenuNames() {
        LOGGER.info("Trying to show all menus name.");
        try {
            menuDAO.showAllMenuNames();
            LOGGER.error("All menus name were printed.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot show all menus " + ex);
            System.out.println("There is no menu.");
        }
    }


    @Transactional
    public List<Menu> getAllMenus() {
        List<Menu> allMenu = null;
        LOGGER.info("Trying to get all menus.");
        try {
            allMenu = menuDAO.getAllMenu();
            LOGGER.info("All menus were got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get all menus from database " + ex);
        }
        return allMenu;
    }

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }
}
