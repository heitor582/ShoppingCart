package com.study.cart.entities;

import com.study.cart.constants.Items;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "items")
public class Item implements Comparable<Item>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Deprecated
    public Item() {}

    private Item(final Long id, final String name, final double price, final int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static Item newItem(final Items itemData, final int quantity) {
        return new Item(0L, itemData.getName(), itemData.getPrice(), quantity);
    }

    public static Item from(final Item item, final int quantity){
        return new Item(item.getId(), item.getName(), item.getPrice(), quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalOf() {
        return this.getPrice() * this.getQuantity();
    }

    public void add(final int quantity){
        this.quantity += quantity;
    }

    public void remove(final int quantity){
        this.quantity -= quantity;
        if (this.quantity < 0 ){
            this.quantity = 0;
        }
    }

    @Override
    public int compareTo(final Item item) {
        return Double.compare(getPrice(), item.getPrice());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Item item = (Item) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
