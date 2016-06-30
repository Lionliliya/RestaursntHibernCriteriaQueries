package com.gmail.liliyayalovchenko.dao;

import com.gmail.liliyayalovchenko.domain.Dish;
import com.gmail.liliyayalovchenko.domain.Order;
import com.gmail.liliyayalovchenko.domain.OrderStatus;

import java.util.List;

public interface OrderDAO {

    void save(Order order);

    List<Order> findAll();

    void addDishToOpenOrder(Dish dish, int orderNumber);

    void deleteOrder(int orderNumber);

    void changeOrderStatus(int orderNumber);

    List<Order> getOpenOrClosedOrder(OrderStatus orderStatus);

    Order getOrderById(int i);

    int getLastOrder();
}
