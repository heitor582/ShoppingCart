package com.study.cart.api;

import com.study.cart.api.dto.CartItemRequest;
import com.study.cart.api.dto.CartResponse;
import com.study.cart.api.dto.CartsListResponse;
import com.study.cart.api.dto.CloseResponse;
import com.study.cart.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "/carts")
@Tag(name = "Carts")
public interface CartAPI {
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create();

    @DeleteMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Close a cart member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CloseResponse> close(@PathVariable(name = "id") final Long id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "List all carts paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<Pagination<CartsListResponse>> list(
            @PageableDefault(
                    sort = {"id"}) Pageable page
    );

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CartResponse> getById(@PathVariable(name = "id") final Long id);

    @DeleteMapping(value = "/{id}/empty")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Empty a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart empty successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CartResponse> emptyById(@PathVariable(name = "id") final Long id);

    @PatchMapping(value = "/{id}/add/item",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Add a item in a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart update successfully"),
            @ApiResponse(responseCode = "404", description = "Cart was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CartResponse> addItem(@PathVariable(name = "id") final Long id, @RequestBody final CartItemRequest input);

    @PatchMapping(value = "/{id}/remove/item",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Remove a item in a cart by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart update successfully"),
            @ApiResponse(responseCode = "404", description = "Cart was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<CartResponse> removeItem(@PathVariable(name = "id") final Long id, @RequestBody final CartItemRequest input);
}
