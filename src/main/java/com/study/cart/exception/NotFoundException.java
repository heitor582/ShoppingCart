package com.study.cart.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotFoundException extends NoStackTraceException{
    public static final Logger LOGGER = LoggerFactory.getLogger(NotFoundException.class);
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

        LOGGER.error("[NOT-FOUND] {}", error);

        return new NotFoundException(error);
    }
}
