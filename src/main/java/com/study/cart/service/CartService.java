package com.study.cart.service;

import com.study.cart.pagination.Pagination;
import com.study.cart.service.dtos.CartOutput;
import com.study.cart.service.dtos.CloseCartOutput;
import com.study.cart.service.dtos.CreateCartOutput;
import com.study.cart.service.dtos.ItemOperationInput;
import com.study.cart.service.dtos.ListCartOutput;
import org.springframework.data.domain.Pageable;

public interface CartService {
    CreateCartOutput create();
    Pagination<ListCartOutput> list(final Pageable page);
    CartOutput getById(final Long id);
    CloseCartOutput close(final Long id);
    CartOutput emptyById(final Long id);
    CartOutput removeItem(final ItemOperationInput input);
    CartOutput addItem(final ItemOperationInput input);
}
