package com.study.cart.service;

import com.study.cart.contants.Items;
import com.study.cart.entities.Cart;
import com.study.cart.entities.Item;
import com.study.cart.exception.NotFoundException;
import com.study.cart.exception.NotFoundFromItemEnum;
import com.study.cart.jpa.CartRepository;
import com.study.cart.pagination.Pagination;
import com.study.cart.service.dtos.CartOutput;
import com.study.cart.service.dtos.CloseCartOutput;
import com.study.cart.service.dtos.CreateCartOutput;
import com.study.cart.service.dtos.ItemOperationInput;
import com.study.cart.service.dtos.ListCartOutput;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService{
    private final CartRepository repository;

    public CartServiceImpl(final CartRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public CreateCartOutput create() {
        return CreateCartOutput.from(repository.save(Cart.newCart()));
    }

    @Override
    public Pagination<ListCartOutput> list(final Pageable page) {
        return Pagination.from(repository.findAll(page)).map(ListCartOutput::from);
    }

    @Override
    public CartOutput getById(final Long id) {
        return repository.findById(id).map(CartOutput::from)
                .orElseThrow(() -> NotFoundException.with(Cart.class, id));
    }

    @Override
    public CloseCartOutput close(final Long id) {
        final Cart cart = repository.findById(id).orElseThrow(() -> NotFoundException.with(Cart.class, id));

        final int totalItems = cart.getTotalItems();
        final double totalPrice = cart.getItems().stream().map(Item::getTotalOf).reduce(0.0, Double::sum);
        final int totalDiscountItems = totalItems / 3;

        List<Item> items = cart.getItems().stream().sorted().toList();

        int remaingDiscountItems = totalDiscountItems;
        int index = 0;

        while(remaingDiscountItems > 0) {
            final var item = items.get(index);
            final var itemQuantity = item.getQuantity();
            cart.removeItem(Item.from(item, remaingDiscountItems));
            if(item.getQuantity() == 0) {
                remaingDiscountItems = totalDiscountItems - itemQuantity;
                index++;
            } else {
                remaingDiscountItems = 0;
            }
        }

        return new CloseCartOutput(
                totalPrice,
                items.stream().map(Item::getTotalOf).reduce(0.0, Double::sum),
                totalItems,
                totalItems - totalDiscountItems
        );
    }

    @Override
    public CartOutput emptyById(final Long id) {
        final Cart cart = repository.findById(id).orElseThrow(() -> NotFoundException.with(Cart.class, id));
        return CartOutput.from(repository.save(cart.emptyItems()));
    }

    @Override
    public CartOutput removeItem(final ItemOperationInput input) {
        final Cart cart = repository.findById(input.cartId()).orElseThrow(() -> NotFoundException.with(Cart.class, input.cartId()));
        final Items itemData = Items.valuesOf(input.itemId());
        if (itemData == null) {
            throw NotFoundFromItemEnum.with();
        }

        cart.removeItem(Item.newItem(itemData, input.quantity(), cart));
        return CartOutput.from(repository.save(cart));
    }

    @Override
    public CartOutput addItem(final ItemOperationInput input) {
        final Cart cart = repository.findById(input.cartId()).orElseThrow(() -> NotFoundException.with(Cart.class, input.cartId()));
        final Items itemData = Items.valuesOf(input.itemId());
        if (itemData == null) {
            throw NotFoundFromItemEnum.with();
        }

        cart.addItem(Item.newItem(itemData, input.quantity(), cart));
        return CartOutput.from(repository.save(cart));
    }
}
