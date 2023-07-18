package com.study.cart.contants;

public enum Items {
    T_SHIRT(1, "T-shirt", 12.99),
    JEANS(2,  "Jeans", 25.00),
    DRESS(3,  "Dress", 20.65);

    private final int id;
    private final String name;
    private final double price;

    Items(final int id, final String name, final double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public static Items valuesOf(final int id){
        for (final Items i : values()) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }
}
