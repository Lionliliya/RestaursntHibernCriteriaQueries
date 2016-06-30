package com.gmail.liliyayalovchenko.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "WAREHOUSE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Warehouse {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingred_id")
    private Ingredient ingredId;

    @Column(name = "amount")
    private int amount;

    public Warehouse(int amount, Ingredient ingredId) {
        this.amount = amount;
        this.ingredId = ingredId;
    }

    public Warehouse(int id, int amount, Ingredient ingredId) {
        this.id = id;
        this.amount = amount;
        this.ingredId = ingredId;
    }


    public Warehouse() {}

    public void changeAmount(int amount1, boolean increase) {
        if (increase) {
           amount = amount + amount1;
        } else {
            amount = amount - amount1;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ingredient getIngredId() {
        return ingredId;
    }

    public void setIngredId(Ingredient ingredId) {
        this.ingredId = ingredId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", ingredId=" + ingredId +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Warehouse)) return false;

        Warehouse warehouse = (Warehouse) o;

        if (amount != warehouse.amount) return false;
        if (ingredId != null ? !ingredId.equals(warehouse.ingredId) : warehouse.ingredId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ingredId != null ? ingredId.hashCode() : 0;
        result = 31 * result + amount;
        return result;
    }
}
