package com.study.cart.api;

import com.study.cart.IntegrationTest;
import com.study.cart.api.dto.CartItemRequest;
import com.study.cart.constants.Items;
import com.study.cart.constants.Json;
import com.study.cart.entities.Cart;
import com.study.cart.entities.Item;
import com.study.cart.jpa.CartRepository;
import com.study.cart.service.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class CartIT extends IntegrationTest {
    @Autowired
    private CartRepository repository;
    @SpyBean
    private CartService service;

    private final static String URL = "/carts";

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToCreateAShoppingCart() throws Exception{
        Assertions.assertEquals(0, repository.count());

        final var request = MockMvcRequestBuilders.post(URL);

        final var response = this.http.perform(request);

        Assertions.assertEquals(1, repository.count());

        final Long id = repository.findAll().get(0).getId();

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(header().string("Location", "%s/%s".formatted(URL, id)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(id.intValue())));

        verify(service).create();
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToGetAShoppingCartByHistIdentifier() throws Exception{
        final Cart cart = repository.save(Cart.newCart());
        final Long id = cart.getId();

        final var request = MockMvcRequestBuilders.get("%s/%s".formatted(URL, id));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.total_items", equalTo(cart.getTotalItems())))
                .andExpect(jsonPath("$.items", hasSize(cart.getItems().size())))
                .andExpect(jsonPath("$.created_at", equalTo(cart.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(cart.getUpdatedAt().toString())));

        verify(service).getById(id);
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToGetAShoppingCartWithItemsByHistIdentifier() throws Exception{
        final Items items = Items.T_SHIRT;
        final int quantity = 5;
        final double total = items.getPrice() * quantity;

        final Cart cart = Cart.newCart();
        cart.addItem(Item.newItem(items, quantity));
        final Long id = repository.save(cart).getId();

        final var request = MockMvcRequestBuilders.get("%s/%s".formatted(URL, id));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.total_items", equalTo(cart.getTotalItems())))
                .andExpect(jsonPath("$.items", hasSize(cart.getItems().size())))
                .andExpect(jsonPath("$.items[0].name", equalTo(items.getName())))
                .andExpect(jsonPath("$.items[0].price", equalTo(items.getPrice())))
                .andExpect(jsonPath("$.items[0].quantity", equalTo(quantity)))
                .andExpect(jsonPath("$.items[0].total", equalTo(total)))
                .andExpect(jsonPath("$.created_at", equalTo(cart.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(cart.getUpdatedAt().toString())));

        verify(service).getById(id);
    }


    @Test
    public void asAShoppingCartAdminIShouldBeAbleToGetAErrorWhenITryToGetANonExistenceShoppingCart() throws Exception{
        final Long id = 1L;
        final String errorMessage = "Cart with ID %s was not found".formatted(id);

        final var request = MockMvcRequestBuilders.get("%s/%s".formatted(URL, id));
        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(errorMessage)));

        verify(service).getById(id);
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToGetAllShoppingCartsPageable() throws Exception{
        repository.saveAll(List.of(Cart.newCart(), Cart.newCart()));

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 2;

        final var request = MockMvcRequestBuilders.get("%s".formatted(URL))
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("size", String.valueOf(expectedPerPage));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(service).list(expectedPage, expectedPerPage);
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToAddAItemInACartByHistIdentifier() throws Exception{
        final Cart cart = repository.save(Cart.newCart());
        final Long id = cart.getId();

        final CartItemRequest itemRequest = new CartItemRequest(1, 5);

        final var request = MockMvcRequestBuilders.patch("%s/%s/add/item".formatted(URL, id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(itemRequest));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.total_items", equalTo(5)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.created_at", equalTo(cart.getCreatedAt().toString())));

        verify(service).addItem(any());
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToReceiveAErrorWhenTryToAddANonExistenceItem() throws Exception{
        final Cart cart = repository.save(Cart.newCart());
        final Long id = cart.getId();
        final String errorMessage = "The item passed cannot be processable";

        final CartItemRequest itemRequest = new CartItemRequest(5, 5);

        final var request = MockMvcRequestBuilders.patch("%s/%s/add/item".formatted(URL, id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(itemRequest));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(errorMessage)));

        verify(service).addItem(any());
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToRemoveAItemInACartByHistIdentifier() throws Exception{
        final Cart cart = Cart.newCart();
        cart.addItem(Item.newItem(Items.T_SHIRT, 6));
        final Long id = repository.save(cart).getId();

        final CartItemRequest itemRequest = new CartItemRequest(1, 5);

        final var request = MockMvcRequestBuilders.patch("%s/%s/remove/item".formatted(URL, id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(itemRequest));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.total_items", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.created_at", equalTo(cart.getCreatedAt().toString())));

        verify(service).removeItem(any());
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToReceiveAErrorWhenTryToRemoveANonExistenceItem() throws Exception{
        final Cart cart = repository.save(Cart.newCart());
        final Long id = cart.getId();
        final String errorMessage = "The item passed cannot be processable";

        final CartItemRequest itemRequest = new CartItemRequest(5, 5);

        final var request = MockMvcRequestBuilders.patch("%s/%s/remove/item".formatted(URL, id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(itemRequest));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(errorMessage)));

        verify(service).removeItem(any());
    }

    @Test
    public void asAShoppingCartAdminIShouldBeAbleToEmptyACartByHistIdentifier() throws Exception{
        final Cart cart = Cart.newCart();
        cart.addItem(Item.newItem(Items.T_SHIRT, 6));
        final Long id = repository.save(cart).getId();

        final var request = MockMvcRequestBuilders.delete("%s/%s/empty".formatted(URL, id));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.total_items", equalTo(0)))
                .andExpect(jsonPath("$.items", hasSize(0)))
                .andExpect(jsonPath("$.created_at", equalTo(cart.getCreatedAt().toString())));

        verify(service).emptyById(any());
    }

    @ParameterizedTest
    @MethodSource("provideCloseCartTestItems")
    public void asAShoppingCartAdminIShouldBeAbleToCloseACartByHistIdentifier(
            final List<Item> items,
            final double totalPrice,
            final double discountPrice,
            final int totalPaidItems,
            final int totalItems
    ) throws Exception{
        final Cart cart = Cart.newCart();
        cart.addItems(items);
        final Long id = repository.save(cart).getId();

        final var request = MockMvcRequestBuilders.delete("%s/%s".formatted(URL, id));

        final var response = this.http.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.total_price", equalTo(totalPrice)))
                .andExpect(jsonPath("$.discount_price", equalTo(discountPrice)))
                .andExpect(jsonPath("$.total_items", equalTo(totalItems)))
                .andExpect(jsonPath("$.total_paid_items", equalTo(totalPaidItems)));

        verify(service).close(id);
    }

    private static Stream<Arguments> provideCloseCartTestItems() {
        return Stream.of(
                Arguments.of(
                        List.of(Item.newItem(Items.T_SHIRT, 1),
                                Item.newItem(Items.JEANS, 2),
                                Item.newItem(Items.DRESS, 3)),
                        124.94,
                        91.3,
                        4,
                        6
                ),
                Arguments.of(
                        List.of(Item.newItem(Items.T_SHIRT, 3)),
                        38.97,
                        25.98,
                        2,
                        3
                ),
                Arguments.of(
                        List.of(Item.newItem(Items.T_SHIRT, 2),
                                Item.newItem(Items.JEANS, 2)),
                        75.98,
                        62.99,
                        3,
                        4
                )
        );
    }
}