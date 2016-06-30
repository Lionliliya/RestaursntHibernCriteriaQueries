package com.gmail.liliyayalovchenko.dao.hibernate;

import com.gmail.liliyayalovchenko.dao.IngredientDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IngredientDAOImpl implements IngredientDAO {

    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Ingredient getIngredientById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Ingredient ingredient = (Ingredient) session.load(Ingredient.class, id);
        if (ingredient != null) {
            return ingredient;
        } else {
            throw new RuntimeException("Cant get ingredient by this id.");
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Ingredient> getAllIngredients() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Ingredient.class);
        return criteria.list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Ingredient getIngredientByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Ingredient.class);
        criteria.add(Restrictions.eq("name", name));
        Ingredient ingredient = (Ingredient) criteria.list().get(0);
        if (ingredient == null) {
            throw new RuntimeException("Cant get ingredient by this name. Wrong name!");
        } else {
            return ingredient;
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean exist(Ingredient ingredient) {
        Session session = sessionFactory.getCurrentSession();
        Set<Ingredient> allIngredient = new HashSet<>(session.createCriteria(Ingredient.class).list());
        return allIngredient.contains(ingredient);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addIngredient(Ingredient ingredient) {
        sessionFactory.getCurrentSession().save(ingredient);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
