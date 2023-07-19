package com.study.cart.service;

import com.study.cart.constants.Items;
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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService{
    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartRepository repository;

    public CartServiceImpl(final CartRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public CreateCartOutput create() {
        LOGGER.info("[CART-CREATION] [PROCESSING] Trying to create a new cart");
        return CreateCartOutput.from(repository.save(Cart.newCart()));
    }

    @Override
    public Pagination<ListCartOutput> list(final Pageable page) {
        LOGGER.info("[CART-LIST] [PROCESSING] Listing all carts on database");
        return Pagination.from(repository.findAll(page)).map(ListCartOutput::from);
    }

    @Override
    public CartOutput getById(final Long id) {
        LOGGER.info("[CART-GET] [PROCESSING] Get cart by his identifier: {}", id);
        return repository.findById(id).map(CartOutput::from)
                .orElseThrow(() -> NotFoundException.with(Cart.class, id));
    }

    @Override
    @Transactional
    public CloseCartOutput close(final Long id) {
        LOGGER.info("[CART-CLOSING] [PROCESSING] Trying to close a cart by his identifier: {}", id);
        final Cart cart = repository.findById(id).orElseThrow(() -> NotFoundException.with(Cart.class, id));

        final int totalItems = cart.getTotalItems();
        final double totalPrice = cart.getItems().stream().map(Item::getTotalOf).reduce(0.0, Double::sum);
        final int totalDiscountItems = totalItems / 3;

        List<Item> items = cart.getItems().stream().sorted().toList();

        int remainingDiscountItems = totalDiscountItems;
        int index = 0;

        while(remainingDiscountItems > 0) {
            final var item = items.get(index);
            final var itemQuantity = item.getQuantity();
            cart.removeItem(Item.from(item, remainingDiscountItems));
            if(item.getQuantity() == 0) {
                remainingDiscountItems = totalDiscountItems - itemQuantity;
                index++;
            } else {
                remainingDiscountItems = 0;
            }
        }

        final double totalDiscountPrice = items.stream().map(Item::getTotalOf).reduce(0.0, Double::sum);

        repository.save(cart.emptyItems());

        LOGGER.info("[CART-CLOSED] [SUCCESSFULLY] Cart with identifier: {} was closed and clean", id);

        return new CloseCartOutput(
                totalPrice,
                totalDiscountPrice,
                totalItems,
                totalItems - totalDiscountItems
        );
    }

    @Override
    @Transactional
    public CartOutput emptyById(final Long id) {
        LOGGER.info("[CART-EMPTY] [PROCESSING] Trying to clean a cart by his identifier: {}", id);
        final Cart cart = repository.findById(id).orElseThrow(() -> NotFoundException.with(Cart.class, id));
        return CartOutput.from(repository.save(cart.emptyItems()));
    }

    @Override
    @Transactional
    public CartOutput removeItem(final ItemOperationInput input) {
        LOGGER.info("[CART-REMOVE-ITEM] [PROCESSING] Trying to remove item of a cart by his identifier: {}", input.cartId());
        final Cart cart = repository.findById(input.cartId()).orElseThrow(() -> NotFoundException.with(Cart.class, input.cartId()));
        final Items itemData = Items.valuesOf(input.itemId());
        if (itemData == null) {
            throw NotFoundFromItemEnum.with();
        }

        cart.removeItem(Item.newItem(itemData, input.quantity()));

        LOGGER.info("[CART-REMOVE-ITEM] [SUCCESSFULLY] Item removed of a cart by his identifier: {}",input.cartId());

        return CartOutput.from(repository.save(cart));
    }

    @Override
    @Transactional
    public CartOutput addItem(final ItemOperationInput input) {
        LOGGER.info("[CART-ADD-ITEM] [PROCESSING] Trying to add item of a cart by his identifier: {}", input.cartId());
        final Cart cart = repository.findById(input.cartId()).orElseThrow(() -> NotFoundException.with(Cart.class, input.cartId()));
        final Items itemData = Items.valuesOf(input.itemId());
        if (itemData == null) {
            throw NotFoundFromItemEnum.with();
        }

        cart.addItem(Item.newItem(itemData, input.quantity()));

        LOGGER.info("[CART-ADD-ITEM] [SUCCESSFULLY] Item {} added of a cart by his identifier: {}",itemData.getName(), input.cartId());

        return CartOutput.from(repository.save(cart));
    }
}
