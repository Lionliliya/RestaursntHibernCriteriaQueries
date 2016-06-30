package com.gmail.liliyayalovchenko.dao.hibernate;

import com.gmail.liliyayalovchenko.dao.DishDAO;
import com.gmail.liliyayalovchenko.domain.Dish;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DishDAOImpl implements DishDAO {

    private SessionFactory sessionFactory;


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(Dish dish) {
        sessionFactory.getCurrentSession().save(dish);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Dish> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        Criteria criteria = currentSession.createCriteria(Dish.class);
        return criteria.list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Dish getDishByName(String dishName) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Dish.class);
        criteria.add(Restrictions.eq("name", dishName));
        Dish dish = (Dish) criteria.list().get(0);
        if (dish != null) {
            return dish;
        } else {
            throw new RuntimeException("Cant get dish by this dish name! Error!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeDish(Dish dish) {
        sessionFactory.getCurrentSession().delete(dish);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Dish getDishById(int dishId) {
        Session session = sessionFactory.getCurrentSession();
        Dish dish = (Dish) session.load(Dish.class, dishId);
        if (dish != null) {
            return dish;
        } else {
            throw new RuntimeException("Cant get dish by this id! Error");
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
