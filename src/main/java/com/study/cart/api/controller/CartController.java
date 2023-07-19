package com.study.cart.api.controller;

import com.study.cart.api.CartAPI;
import com.study.cart.api.dto.CartItemRequest;
import com.study.cart.api.dto.CartResponse;
import com.study.cart.api.dto.CartsListResponse;
import com.study.cart.api.dto.CloseResponse;
import com.study.cart.pagination.Pagination;
import com.study.cart.service.CartService;
import com.study.cart.service.dtos.ItemOperationInput;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CartController implements CartAPI {
    private final CartService service;

    public CartController(final CartService service) {
        this.service = Objects.requireNonNull(service);
    }


    @Override
    public ResponseEntity<?> create() {
        final var output = service.create();
        return ResponseEntity.created(URI.create("/carts/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<CloseResponse> close(final Long id) {
        return ResponseEntity.ok(CloseResponse.from(service.close(id)));
    }

    @Override
    public ResponseEntity<Pagination<CartsListResponse>> list(final Pageable page) {
        return ResponseEntity.ok(service.list(page).map(CartsListResponse::from));
    }

    @Override
    public ResponseEntity<CartResponse> getById(final Long id) {
        return ResponseEntity.ok(CartResponse.from(service.getById(id)));
    }

    @Override
    public ResponseEntity<CartResponse> emptyById(final Long id) {
        return ResponseEntity.ok(CartResponse.from(service.emptyById(id)));
    }

    @Override
    public ResponseEntity<CartResponse> addItem(final Long id, final CartItemRequest request) {
        final var input = ItemOperationInput.with(id, request.itemId(), request.quantity());
        return ResponseEntity.ok(CartResponse.from(service.addItem(input)));
    }

    @Override
    public ResponseEntity<CartResponse> removeItem(final Long id, final CartItemRequest request) {
        final var input = ItemOperationInput.with(id, request.itemId(), request.quantity());
        return ResponseEntity.ok(CartResponse.from(service.removeItem(input)));
    }
}
