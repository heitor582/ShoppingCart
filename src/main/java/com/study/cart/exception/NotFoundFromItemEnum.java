package com.study.cart.exception;

public class NotFoundFromItemEnum extends NotFoundException{
    protected NotFoundFromItemEnum(final String message) {
        super(message);
    }

    public static NotFoundFromItemEnum with(){
        return new NotFoundFromItemEnum("The item passed cannot be processable");
    }
}
