package com.gmail.liliyayalovchenko.dao.hibernate;

import com.gmail.liliyayalovchenko.dao.ReadyMealDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import com.gmail.liliyayalovchenko.domain.Order;
import com.gmail.liliyayalovchenko.domain.ReadyMeal;
import com.gmail.liliyayalovchenko.domain.Warehouse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ReadyMealDAOImpl implements ReadyMealDAO {

    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<ReadyMeal> getAllReadyMeals() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(ReadyMeal.class).list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeReadyMeal(int orderNumber) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(ReadyMeal.class);
        criteria.add(Restrictions.eq("orderNumber", orderNumber));
        Order order = (Order) criteria.list().get(0);
        criteria.add(Restrictions.eq("orderId", order.getId()));
        List<ReadyMeal> readyMealList = criteria.list();
        readyMealList.forEach(session::delete);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addReadyMeal(ReadyMeal meal) {
        sessionFactory.getCurrentSession().save(meal);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void changeAmountOnWarehouse(Ingredient ingredient) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Ingredient.class);
        criteria.add(Restrictions.eq("name", ingredient.getName()));
        Ingredient ingredientResult = (Ingredient) criteria.list().get(0);
        criteria = session.createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq("ingredId", ingredientResult.getId()));
        Warehouse warehouse = (Warehouse) criteria.list().get(0);
        int newAmount = warehouse.getAmount() - 1;
        warehouse.setAmount(newAmount < 0 ? 0 : newAmount);
        session.update(warehouse);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
