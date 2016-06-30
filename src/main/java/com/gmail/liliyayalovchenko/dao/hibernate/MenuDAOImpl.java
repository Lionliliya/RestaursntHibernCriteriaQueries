package com.gmail.liliyayalovchenko.dao.hibernate;

import com.gmail.liliyayalovchenko.dao.MenuDAO;
import com.gmail.liliyayalovchenko.domain.Dish;
import com.gmail.liliyayalovchenko.domain.Menu;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MenuDAOImpl implements MenuDAO {

    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addNewMenu(String menuName, List<Dish> dishList) {
        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setDishList(dishList);
        sessionFactory.getCurrentSession().save(menu);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void createMenu(Menu menu) {
        sessionFactory.getCurrentSession().save(menu);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeMenu(Menu menu) {
        sessionFactory.getCurrentSession().delete(menu);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Menu getMenuByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Menu.class);
        criteria.add(Restrictions.eq("name", name));
        Menu menu = (Menu) criteria.list().get(0);
        if (menu == null) {
            throw new RuntimeException("Cant get Menu by this name! Wrong name!");
        } else {
            return menu;
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void showAllMenus() {
        Session session = sessionFactory.getCurrentSession();
        session.createCriteria(Menu.class).list().forEach(System.out::println);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void showAllMenuNames() {
        List<Menu> menus = sessionFactory.getCurrentSession().createCriteria(Menu.class).list();
        for (Menu menu : menus) {
            System.out.println(menu.getName());
        }

    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addDishToMenu(int menuId, Dish dish) {
        Session session = sessionFactory.getCurrentSession();
        Menu menu = (Menu) session.load(Menu.class, menuId);
        if (menu == null) {
            throw new RuntimeException("Cant get menu by this id");
        } else {
            menu.addDishToMenu(dish);
            session.update(menu);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeDishFromMenu(int menuId, Dish dish) {
        Session session = sessionFactory.getCurrentSession();
        Menu menu = (Menu) session.load(Menu.class, menuId);
        if (menu == null) {
            throw new RuntimeException("Cant get menu by this id");
        } else {
            menu.removeDishFromMenu(dish);
            session.update(menu);
        }
    }

    @Override
    @Transactional
    public List<Menu> getAllMenu() {
        return sessionFactory.getCurrentSession().createCriteria(Menu.class).list();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
