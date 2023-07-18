package com.study.cart.exception;


public class NotFoundException extends NoStackTraceException{
    protected NotFoundException(final String message) {
        super(message);
    }

    public static NotFoundException with(
            final Class aggregate,
            final Long id
    ) {
        final var error = "%s with ID %s was not found".formatted(
                aggregate.getSimpleName(), id
        );

        return new NotFoundException(error);
    }
}
