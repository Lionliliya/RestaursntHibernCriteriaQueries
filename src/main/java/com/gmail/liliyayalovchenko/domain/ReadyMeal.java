package com.gmail.liliyayalovchenko.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "READY_MEALS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReadyMeal {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    private int id;

    @Column(name = "dish_numb")
    private int dishNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id")
    private Dish dishId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employeeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order orderId;

    @Column(name = "meal_date")
    private Date mealDate;

    public ReadyMeal(int dishNumber, Dish dishId, Employee employeeId, Order orderId, Date mealDate) {
        this.dishNumber = dishNumber;
        this.dishId = dishId;
        this.employeeId = employeeId;
        this.orderId = orderId;
        this.mealDate = mealDate;
    }

    public ReadyMeal() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setDishNumber(int dishNumber) {
        this.dishNumber = dishNumber;
    }

    public void setDishId(Dish dishId) {
        this.dishId = dishId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
    }

    public void setMealDate(Date mealDate) {
        this.mealDate = mealDate;
    }

    public int getId() {
        return id;
    }

    public int getDishNumber() {
        return dishNumber;
    }

    public Dish getDishId() {
        return dishId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public Order getOrderId() {
        return orderId;
    }

    public Date getMealDate() {
        return mealDate;
    }

    @Override
    public String toString() {
        return "ReadyMeal{" +
                "id=" + id +
                ", dishNumber=" + dishNumber +
                ", dishId=" + dishId.getName() +
                ", employeeId=" + employeeId.getFirstName() + " " + employeeId.getSecondName() +
                ", orderId=" + orderId.getOrderNumber() +
                ", mealDate=" + mealDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReadyMeal)) return false;

        ReadyMeal readyMeal = (ReadyMeal) o;

        if (dishNumber != readyMeal.dishNumber) return false;
        if (dishId != null ? !dishId.equals(readyMeal.dishId) : readyMeal.dishId != null) return false;
        if (employeeId != null ? !employeeId.equals(readyMeal.employeeId) : readyMeal.employeeId != null) return false;
        if (mealDate != null ? !mealDate.equals(readyMeal.mealDate) : readyMeal.mealDate != null) return false;
        if (orderId != null ? !orderId.equals(readyMeal.orderId) : readyMeal.orderId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dishNumber;
        result = 31 * result + (dishId != null ? dishId.hashCode() : 0);
        result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (mealDate != null ? mealDate.hashCode() : 0);
        return result;
    }
}
