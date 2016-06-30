package com.gmail.liliyayalovchenko.dao;


import com.gmail.liliyayalovchenko.domain.Dish;
import com.gmail.liliyayalovchenko.domain.Menu;

import java.util.List;

public interface MenuDAO {

    void addNewMenu(String menuName, List<Dish> dishList);

    void createMenu(Menu menu);

    void removeMenu(Menu menu);

    Menu getMenuByName(String name);

    void showAllMenus();

    void addDishToMenu(int menuId, Dish dish);

    void removeDishFromMenu(int menuId, Dish dish);

    List<Menu> getAllMenu();

    void showAllMenuNames();
}
