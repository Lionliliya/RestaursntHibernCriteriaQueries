package com.gmail.liliyayalovchenko.dao.hibernate;

import com.gmail.liliyayalovchenko.dao.OrderDAO;
import com.gmail.liliyayalovchenko.domain.Dish;
import com.gmail.liliyayalovchenko.domain.Order;
import com.gmail.liliyayalovchenko.domain.OrderStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(Order order) {
        sessionFactory.getCurrentSession().save(order);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Order> findAll() {
        return sessionFactory.getCurrentSession().createCriteria(Order.class).list();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void addDishToOpenOrder(Dish dish, int orderNumber) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("orderNumber", orderNumber));
        Order order = (Order) criteria.list().get(0);
        if (order == null) {
            throw new RuntimeException("Cant get order by this order number! Wrong order number");
        } else {
            order.addDishToOrder(dish);
            session.update(order);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteOrder(int orderNumber) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("orderNumber", orderNumber));
        Order order = (Order) criteria.list().get(0);
        if (order == null) {
            throw new RuntimeException("Cant get order by this order number! Wrong order number");
        } else {
            session.delete(order);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void changeOrderStatus(int orderNumber) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("orderNumber", orderNumber));
        Order order = (Order) criteria.list().get(0);
        if (order != null) {
            order.setStatus(OrderStatus.closed);
            session.update(order);

        } else {
            throw new RuntimeException("Cant get order by this order number! Wrong order number");
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Order> getOpenOrClosedOrder(OrderStatus orderStatus) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("status", orderStatus));
        return  (List<Order>) criteria.list();
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Order getOrderById(int i) {
        Session session = sessionFactory.getCurrentSession();
        Order order = (Order) session.load(Order.class, i);
        if (order != null) {
            return order;
        } else {
            throw new RuntimeException("Cant get order by this id! Error!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public int getLastOrder() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Order.class);
        criteria.setProjection(Projections.max("orderNumber"));
        return (int) criteria.list().get(0);
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
