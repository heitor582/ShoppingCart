package com.study.cart.api;

import com.study.cart.IntegrationTest;
import com.study.cart.jpa.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;

class CartIT extends IntegrationTest {
    @Autowired
    private CartRepository repository;
}