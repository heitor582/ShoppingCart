package com.study.cart.service;

import com.study.cart.UnitTest;
import com.study.cart.constants.Items;
import com.study.cart.entities.Cart;
import com.study.cart.entities.Item;
import com.study.cart.exception.NotFoundException;
import com.study.cart.exception.NotFoundFromItemEnum;
import com.study.cart.jpa.CartRepository;
import com.study.cart.service.dtos.ItemOperationInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceTest extends UnitTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Override
    protected List<Object> getMocks() {
        return List.of(cartRepository);
    }

    @Test
    public void givenAValidPage_whenCallsListCart_shouldReturnPageableCarts() {
        //given
        final var page = PageRequest.of(0, 10);
        when(cartRepository.findAll(page)).thenReturn(new PageImpl<>(List.of()));

        //when
        final var output = cartService.list(page);

        //then
        assertEquals(output.currentPage(), page.getPageNumber());
        assertEquals(output.content(), List.of());
    }

    @Test
    public void givenAValidPage_whenCallsListCartWithPrePersistedCarts_shouldReturnPageableCarts() {
        //given
        final var page = PageRequest.of(0, 10);
        final List<Cart> carts = List.of(Cart.newCart(), Cart.newCart());

        when(cartRepository.findAll(page)).thenReturn(new PageImpl<>(carts));

        //when
        final var output = cartService.list(page);

        //then
        assertEquals(output.currentPage(), page.getPageNumber());
        assertEquals(output.content().size(), carts.size());
    }

    @Test
    public void givenAValidInput_whenCallsCreateCart_shouldReturnIt(){
        //given
        when(cartRepository.save(any())).thenAnswer(returnsFirstArg());

        //when
        final var output = cartService.create();

        //then
        assertNotNull(output);

        verify(cartRepository).save(argThat(cart ->
                Objects.nonNull(cart.getCreatedAt())
                && Objects.nonNull(cart.getUpdatedAt())
                && Objects.equals(0, cart.getTotalItems())
                && Objects.equals(0L, cart.getId())
                && Objects.equals(new HashSet<>(), cart.getItems())
        ));
    }

    @Test
    public void givenAValidId_whenCallsFindById_shouldReturnIt(){
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(cart));

        //when
        final var output = cartService.getById(expectedId);

        //then
        assertNotNull(output);
        assertNotNull(output.id());
        assertEquals(cart.getCreatedAt(), output.createdAt());
        assertEquals(cart.getUpdatedAt(), output.updatedAt());
        assertEquals(cart.getTotalItems(), output.totalItems());
        assertEquals(cart.getItems().stream().toList(), output.items());

        verify(cartRepository).findById(expectedId);
    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnNotFoundException(){
        //given
        final var expectedId = 1L;
        final var expectedErrorMessage = "Cart with ID %s was not found".formatted(expectedId);

        when(cartRepository.findById(expectedId)).thenReturn(Optional.empty());

        //when
        final var exception = assertThrows(NotFoundException.class, () -> cartService.getById(expectedId));

        //then
        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(cartRepository).findById(expectedId);
    }

    @Test
    public void givenAValidInput_whenCallsEmptyCart_shouldReturnEmptyCart() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        final Item item1 = Item.newItem(Items.DRESS, 10);
        final Item item2 = Item.newItem(Items.T_SHIRT, 9);

        cart.addItem(item1);
        cart.addItem(item2);

        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(Cart.from(cart).emptyItems());

        //when
        final var output = cartService.emptyById(expectedId);

        //then
        assertNotNull(output);
        assertEquals(0, output.items().size());
        assertEquals(0, output.totalItems());


        verify(cartRepository).findById(expectedId);
        verify(cartRepository).save(any());
    }

    @Test
    public void givenACartWithEmptyItemList_whenCallsEmptyCart_shouldBeOk() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();

        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        //when
        final var output = cartService.emptyById(expectedId);

        //then
        assertNotNull(output);
        assertEquals(0, output.items().size());
        assertEquals(0, output.totalItems());


        verify(cartRepository).findById(expectedId);
        verify(cartRepository).save(any());
    }

    @Test
    public void givenAValidInput_whenCallsAddItem_shouldBeOk() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(Cart.from(cart)));
        when(cartRepository.save(any())).thenAnswer(returnsFirstArg());

        //when
        final var output = cartService.addItem(ItemOperationInput.with(expectedId, 1, 10));

        //then
        assertNotNull(output);
        assertEquals(10, output.totalItems());
        assertTrue(cart.getUpdatedAt().isBefore(output.updatedAt()));
        assertEquals(cart.getCreatedAt(), output.createdAt());


        verify(cartRepository).findById(expectedId);
        verify(cartRepository).save(any());
    }

    @Test
    public void givenAInvalidInput_whenCallsAddItem_shouldThrowsException() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        final String expectedErrorMessage = "The item passed cannot be processable";

        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(Cart.from(cart)));

        //when
        final var exception = assertThrows(NotFoundFromItemEnum.class, () -> cartService.addItem(ItemOperationInput.with(expectedId, 5, 10)));

        //then
        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(cartRepository).findById(expectedId);
    }

    @Test
    public void givenAValidInput_whenCallsAddItem2Times_shouldBeOk() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenAnswer(returnsFirstArg());

        //when
        cartService.addItem(ItemOperationInput.with(expectedId, 1, 10));
        final var output = cartService.addItem(ItemOperationInput.with(expectedId, 1, 10));

        //then
        assertNotNull(output);
        assertEquals(20, output.totalItems());
        assertEquals(1, output.items().size());
        assertEquals(cart.getCreatedAt(), output.createdAt());
    }

    @Test
    public void givenAValidInput_whenCallsRemoveItem_shouldBeOk() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        cart.addItem(Item.newItem(Items.T_SHIRT, 10));
        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(Cart.from(cart)));
        when(cartRepository.save(any())).thenAnswer(returnsFirstArg());

        //when
        final var output = cartService.removeItem(ItemOperationInput.with(expectedId, 1, 10));

        //then
        assertNotNull(output);
        assertEquals(0, output.totalItems());
        assertTrue(cart.getUpdatedAt().isBefore(output.updatedAt()));
        assertEquals(cart.getCreatedAt(), output.createdAt());


        verify(cartRepository).findById(expectedId);
        verify(cartRepository).save(any());
    }

    @Test
    public void givenAValidInput_whenCallsRemoveItemWithCartEmpty_shouldBeOk() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();

        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(Cart.from(cart)));
        when(cartRepository.save(any())).thenAnswer(returnsFirstArg());

        //when
        final var output = cartService.removeItem(ItemOperationInput.with(expectedId, 1, 10));

        //then
        assertNotNull(output);
        assertEquals(0, output.totalItems());
        assertEquals(cart.getUpdatedAt(), output.updatedAt());
        assertEquals(cart.getCreatedAt(), output.createdAt());


        verify(cartRepository).findById(expectedId);
        verify(cartRepository).save(any());
    }

    @Test
    public void givenAnInvalidInput_whenCallsRemoveItem_shouldThrowsException() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        final String expectedErrorMessage = "The item passed cannot be processable";

        cart.addItem(Item.newItem(Items.T_SHIRT, 10));
        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(Cart.from(cart)));

        //when
        final var exception = assertThrows(NotFoundFromItemEnum.class, () -> cartService.removeItem(ItemOperationInput.with(expectedId, 5, 10)));

        //then
        assertNotNull(exception);
        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(cartRepository).findById(expectedId);
    }

    @Test
    public void givenAValidInput_whenCallsRemoveItemWithMoreItemsThanCartHave_shouldBeOk() {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        cart.addItem(Item.newItem(Items.T_SHIRT, 9));
        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(Cart.from(cart)));
        when(cartRepository.save(any())).thenAnswer(returnsFirstArg());

        //when
        final var output = cartService.removeItem(ItemOperationInput.with(expectedId, 1, 10));

        //then
        assertNotNull(output);
        assertEquals(0, output.totalItems());
        assertTrue(cart.getUpdatedAt().isBefore(output.updatedAt()));
        assertEquals(cart.getCreatedAt(), output.createdAt());


        verify(cartRepository).findById(expectedId);
        verify(cartRepository).save(any());
    }

    @ParameterizedTest
    @MethodSource("provideCloseCartTestItems")
    public void givenAValidInput_whenCallsCloseCart_shouldReturnDiscountedValue(
            final List<Item> items,
            final double totalPrice,
            final double discountPrice,
            final int totalPaidItems,
            final int totalItems
    ) {
        //given
        final Long expectedId = 1L;
        final Cart cart = Cart.newCart();
        cart.addItems(items);
        when(cartRepository.findById(expectedId)).thenReturn(Optional.of(cart));

        //when
        final var output = cartService.close(expectedId);

        //then
        assertEquals(totalPaidItems, output.totalPaidItems());
        assertEquals(totalPrice, output.totalPrice());
        assertEquals(totalItems, output.totalItems());
        assertEquals(discountPrice, output.discountPrice());
        assertEquals(0, cart.getTotalItems());
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
                        List.of(Item.newItem(Items.T_SHIRT, 4),
                                Item.newItem(Items.JEANS, 20),
                                Item.newItem(Items.DRESS, 59)),
                        1770.31,
                        1243.4,
                        56,
                        83
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