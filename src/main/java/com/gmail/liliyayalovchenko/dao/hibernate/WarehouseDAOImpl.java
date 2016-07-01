package com.gmail.liliyayalovchenko.dao.hibernate;

import com.gmail.liliyayalovchenko.dao.IngredientDAO;
import com.gmail.liliyayalovchenko.dao.WarehouseDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import com.gmail.liliyayalovchenko.domain.Warehouse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class WarehouseDAOImpl implements WarehouseDAO {

    private SessionFactory sessionFactory;
    private IngredientDAO ingredientDAO;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addIngredient(Ingredient ingredient, int amount) {
        Warehouse warehouse = new Warehouse();
        warehouse.setIngredId(ingredient);
        warehouse.setAmount(amount);
        sessionFactory.getCurrentSession().save(warehouse);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void removeIngredient(String ingredientName) {
        Session session = sessionFactory.getCurrentSession();
        Ingredient ingredientByName = ingredientDAO.getIngredientByName(ingredientName);
        Criteria criteria = session.createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq("ingredId", ingredientByName));
        Warehouse warehouse = (Warehouse) criteria.list().get(0);
        if (warehouse != null) {
            session.delete(warehouse);
        } else {
            throw new RuntimeException("Cant find ingredient on warehouse by this ingredient name");
        }

    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void changeAmount(Ingredient ingredient, int delta, boolean increase) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq("ingredId", ingredient));
        Warehouse warehouse = (Warehouse) criteria.list().get(0);
        if (warehouse != null) {
            warehouse.changeAmount(delta, increase);
            session.update(warehouse);
        } else {
            throw new RuntimeException("Cant find this ingredient on warehouse! Error!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Warehouse findByName(String ingredientName) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Ingredient.class);
        criteria.add(Restrictions.eq("name", ingredientName));
        Ingredient ingredientResult = (Ingredient) criteria.list().get(0);
        criteria = session.createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq("ingredId", ingredientResult));
        Warehouse warehouse = (Warehouse) criteria.list().get(0);
        if (warehouse != null) {
            return warehouse;
        } else {
            throw new RuntimeException("Cant find ingredient on warehouse by this name");
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Warehouse> getAllIngredients() {
        return sessionFactory.getCurrentSession().createCriteria(Warehouse.class).list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Warehouse> getEndingIngredients() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Warehouse.class);
        criteria.add(Restrictions.lt("amount", 10));
        return criteria.list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean alreadyExist(Ingredient ingredient) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Ingredient.class);
        criteria.add(Restrictions.eq("name", ingredient.getName()));
        Ingredient ingredientResult = (Ingredient) criteria.list().get(0);
        criteria = session.createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq("ingredId", ingredientResult.getId()));
        Warehouse warehouse = (Warehouse) criteria.list().get(0);
        return null != warehouse;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean checkAvailiability(Ingredient ingredient) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Ingredient.class);
        criteria.add(Restrictions.eq("name", ingredient.getName()));
        Ingredient ingredientResult = (Ingredient) criteria.list().get(0);
        criteria = session.createCriteria(Warehouse.class);
        criteria.add(Restrictions.eq("ingredId", ingredientResult.getId()));
        Warehouse warehouse = (Warehouse) criteria.list().get(0);
        int amount = warehouse.getAmount();
        return amount > 0;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setIngredientDAO(IngredientDAO ingredientDAO) {
        this.ingredientDAO = ingredientDAO;
    }
}
