package com.study.cart.entities;

import com.study.cart.utils.InstantUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_items", nullable = false)
    private int totalItems;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant updatedAt;

    @OneToMany(mappedBy = "cart")
    private Set<Item> items;

    @Deprecated
    public Cart() {}

    private Cart(final Long id, final int totalItems, final Set<Item> items, final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.totalItems = totalItems;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Cart newCart(){
        final Instant now = InstantUtils.now();
        return new Cart(0L, 0, new HashSet<>(), now, now);
    }

    public static Cart from(final Cart cart){
        return new Cart(cart.getId(), cart.getTotalItems(), cart.getItems(), cart.getCreatedAt(), cart.getUpdatedAt());
    }

    public Cart emptyItems() {
        this.items = new HashSet<>();
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Cart addItem(final Item item){
        final Optional<Item> itemFound = this.items.stream().filter(it -> it.equals(item)).findFirst();
        if (itemFound.isEmpty()){
            this.items.add(item);
        } else {
            itemFound.get().add(item.getQuantity());
        }

        this.totalItems += item.getQuantity();
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Cart addItems(final List<Item> items){
        items.forEach(this::addItem);
        return this;
    }

    public Cart removeItem(final Item item){
        final Optional<Item> itemFound = this.items.stream().filter(it -> it.equals(item)).findFirst();
        if (itemFound.isEmpty()){
            return this;
        }

        final Item getItem = itemFound.get();

        final int itemTotal = getItem.getQuantity();

        getItem.remove(item.getQuantity());
        if(getItem.getQuantity() <= 0){
            this.items.remove(item);
        }

        this.totalItems -= Math.min(item.getQuantity(), itemTotal);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Long getId() {
        return id;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<Item> getItems() {
        return items;
    }
}
