package com.study.cart.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotFoundFromItemEnum extends NotFoundException{
    public static final Logger LOGGER = LoggerFactory.getLogger(NotFoundFromItemEnum.class);
    protected NotFoundFromItemEnum(final String message) {
        super(message);
    }

    public static NotFoundFromItemEnum with(){
        final String error = "The item passed cannot be processable";
        LOGGER.error("[NOT-FOUND] {}", error);
        return new NotFoundFromItemEnum(error);
    }
}
