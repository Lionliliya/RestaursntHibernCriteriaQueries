package com.gmail.liliyayalovchenko.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MENU")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch=FetchType.EAGER, orphanRemoval=true, mappedBy="menu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Dish> dishList;

    public Menu(List<Dish> dishList, String name) {
        this.dishList = dishList;
        this.name = name;
    }

    public Menu() {}

    public void addDishToMenu(Dish dish) {
        if (!dishList.contains(dish)) {
            dishList.add(dish);
        }
        dish.setMenu(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(List<Dish> dishList) {
        this.dishList = dishList;
    }



    @Override
    public String toString() {
       return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishList=" + printDishList() +
                '}';
    }

    private String printDishList() {
        StringBuilder dishPrint = new StringBuilder();
        dishPrint.append("[ ");
        for (Dish dish : dishList) {
            dishPrint.append(dish.getName()).append(", ");
        }
        dishPrint.append(" ]");
        return dishPrint.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;

        Menu menu = (Menu) o;

        if (name != null ? !name.equals(menu.name) : menu.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public void removeDishFromMenu(Dish dish) {
        dishList.remove(dish);

    }
}
