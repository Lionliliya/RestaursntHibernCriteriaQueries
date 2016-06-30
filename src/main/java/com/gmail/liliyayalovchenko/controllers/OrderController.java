package com.gmail.liliyayalovchenko.controllers;

import com.gmail.liliyayalovchenko.dao.OrderDAO;
import com.gmail.liliyayalovchenko.domain.Dish;
import com.gmail.liliyayalovchenko.domain.Order;
import com.gmail.liliyayalovchenko.domain.OrderStatus;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class OrderController {

    private OrderDAO orderDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Transactional
    public void saveOrder(Order order) {
        LOGGER.info("Trying to persist order.");
        try {
            orderDAO.save(order);
            LOGGER.info("Order is persisted.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot persist order to database. " + ex);
            System.out.println("Order was not saved!");
        }
    }

    @Transactional
    public List<Order> getAllorders() {
        List<Order> all = null;
        LOGGER.info("Trying to get all orders");
        try {
            all = orderDAO.findAll();
            LOGGER.info("All orders were got");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get list of orders from database. " + ex);
        }
        return all;
    }



    @Transactional
    public void addDishToOpenOrder(Dish dish, int orderNumber) {
        LOGGER.info("Trying to add dish to order.");
        try{
            orderDAO.addDishToOpenOrder(dish, orderNumber);
            LOGGER.info("Dish " + dish.getName() + " was added to order " + orderNumber);
        } catch (HibernateException ex) {
            LOGGER.error("Cannot persist dish to order. " + ex);
            System.out.println("Dish " + dish.getName() + " was not added to order number " + orderNumber);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
            System.out.println("Dish " + dish.getName() + " was not added to order number " + orderNumber);
        }
    }

    @Transactional
    public void deleteOrder(int orderNumber) {
        LOGGER.info("Trying to delete order " + orderNumber);
       try {
           orderDAO.deleteOrder(orderNumber);
           LOGGER.info("Order " + orderNumber + " was deleted.");
       } catch (HibernateException ex) {
            LOGGER.error("Cannot delete order " + orderNumber + " from database. " + ex);
           System.out.println("Order " + orderNumber + " was not deleted.");
       } catch (RuntimeException ex) {
           LOGGER.error("Wrong input! " + ex);
           System.out.println("Order " + orderNumber + " was not deleted.");
       }
    }

    @Transactional
    public void changeOrderStatus(int orderNumber) {
        LOGGER.info("Trying to change order " + orderNumber + " status.");
        try {
            orderDAO.changeOrderStatus(orderNumber);
            LOGGER.info("Order " + orderNumber + " status was changed.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot change order " + orderNumber + " in database." + ex);
            System.out.println("Order " + orderNumber + " status was not changed.");
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
            System.out.println("Order " + orderNumber + " status was not changed.");
        }
    }

    @Transactional
    public List<Order> getOpenOrClosedOrder(OrderStatus orderStatus) {
        List<Order> openOrClosedOrder = null;
        LOGGER.info("Trying to get " + orderStatus +" orders.");
        try {
            openOrClosedOrder = orderDAO.getOpenOrClosedOrder(orderStatus);
            LOGGER.info("Orders were got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cant get orders from database " + ex);
        }
        return openOrClosedOrder;
    }

    @Transactional
    public void printOpenOrClosedOrders(OrderStatus orderStatus) {
        LOGGER.info("Trying to print all " + orderStatus + " orders.");
        List<Order> openOrClosedOrder = getOpenOrClosedOrder(orderStatus);
        if (openOrClosedOrder != null) {
            openOrClosedOrder.forEach(System.out::println);
            LOGGER.info("Orders were printed.");
        } else {
            System.out.println("No order was found.");
        }
    }

    @Transactional
    public Order getOrderById(int i) {
        Order orderById = null;
        LOGGER.info("Trying to get order by id " + i);
        try {
            orderById = orderDAO.getOrderById(i);
            LOGGER.info("Order by id " + i + " is got.");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get order by id " + i + " from database " + ex);
        } catch (RuntimeException ex) {
           LOGGER.error("Wrong input " + i + ex);
        }
        return orderById;
    }

    @Transactional
    public int getLastOrder() {
        int lastOrder;
        LOGGER.info("Trying to get last order id.");
        try {
            lastOrder = orderDAO.getLastOrder();
            LOGGER.info("Last order id was got.");
            return lastOrder;
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get last order id from database " + ex);
            throw new RuntimeException("Cant get last order id. Could not connected to database in right way.");
        }
    }

    public void setOrderDAO(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }


}
