package factory;

import model.Product;

public class ProductFactory {

    public static final String TYPE_BASIC = "BASIC";
    public static final String TYPE_PREMIUM = "PREMIUM";

    private ProductFactory() {
    }

    public static Product createProduct(
            String type,
            String id,
            String name,
            double price,
            int stock) {

        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Product type cannot be empty."
            );
        }

        String normalizedType = type.trim().toUpperCase();

        switch (normalizedType) {

            case TYPE_BASIC:
                return createBasicProduct(
                        id,
                        name,
                        price,
                        stock
                );

            case TYPE_PREMIUM:
                return createPremiumProduct(
                        id,
                        name,
                        price,
                        stock
                );

            default:
                throw new IllegalArgumentException(
                        "Unknown product type: " + type
                );
        }
    }

    public static Product createBasicProduct(
            String id,
            String name,
            double price,
            int stock) {

        return new Product(
                id,
                name,
                price,
                stock
        );
    }

    public static Product createPremiumProduct(
            String id,
            String name,
            double price,
            int stock) {

        double premiumPrice = price * 1.2;

        return new Product(
                id,
                name + " Premium",
                premiumPrice,
                stock
        );
    }
}