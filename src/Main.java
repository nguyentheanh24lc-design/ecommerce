import manager.MarketplaceManager;
import model.Admin;
import model.Buyer;
import model.OrderItem;
import model.Product;
import model.Seller;
import strategy.CashPayment;
import strategy.PaymentStrategy;

public class Main {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" E-COMMERCE MANAGEMENT SYSTEM ");
        System.out.println("=================================");

        // =========================
        // SINGLETON
        // =========================

        MarketplaceManager manager1 = MarketplaceManager.getInstance();
        MarketplaceManager manager2 = MarketplaceManager.getInstance();

        System.out.println("\n--- SINGLETON TEST ---");
        System.out.println(
                "manager1 == manager2: "
                        + (manager1 == manager2)
        );

        // =========================
        // CREATE USERS
        // =========================

        Buyer buyer = new Buyer(
                "B001",
                "Nguyen Van A",
                "buyer@gmail.com",
                "Ha Noi"
        );

        Seller seller = new Seller(
                "S001",
                "Tran Van B",
                "seller@gmail.com",
                "B Shop"
        );

        Admin admin = new Admin(
                "A001",
                "Le Van C",
                "admin@gmail.com",
                "HIGH"
        );

        manager1.addUser(buyer);
        manager1.addUser(seller);
        manager1.addUser(admin);

        // =========================
        // CREATE PRODUCTS
        // =========================

        Product product1 = createProduct(
                "P001",
                "Laptop",
                15000000,
                10
        );

        Product product2 = createProduct(
                "P002",
                "Mouse",
                500000,
                20
        );

        if (product1 != null) {
            manager1.addProduct(product1);
        }

        if (product2 != null) {
            manager1.addProduct(product2);
        }

        // =========================
        // TEST INVALID PRICE
        // =========================

        System.out.println("\n--- INVALID PRICE TEST ---");

        Product invalidPriceProduct = createProduct(
                "P999",
                "Invalid Product",
                -100,
                10
        );

        if (invalidPriceProduct == null) {
            System.out.println(
                    "Invalid product was rejected successfully."
            );
        }

        // =========================
        // TEST INVALID QUANTITY
        // =========================

        System.out.println("\n--- INVALID QUANTITY TEST ---");

        if (product1 != null) {

            try {

                OrderItem invalidItem =
                        new OrderItem(product1, 0);

                System.out.println(invalidItem);

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Quantity error handled: "
                                + e.getMessage()
                );
            }
        }

        // =========================
        // DISPLAY DATA
        // =========================

        System.out.println("\n--- DATA FROM manager2 ---");

        System.out.println(
                "Users: " + manager2.getUserCount()
        );

        System.out.println(
                "Products: " + manager2.getProductCount()
        );

        System.out.println(
                "Orders: " + manager2.getOrderCount()
        );

        System.out.println("\n--- USERS ---");

        for (var user : manager2.getUsers()) {

            user.displayRole();

            System.out.println(user);

            System.out.println("------------------");
        }

        System.out.println("\n--- PRODUCTS ---");

        for (Product product : manager2.getProducts()) {

            System.out.println(product);
        }

        System.out.println("\n=================================");
        System.out.println(" Exception handling completed!");
        System.out.println("=================================");
    }

    // =========================
    // PRODUCT CREATION
    // =========================

    private static Product createProduct(
            String id,
            String name,
            double price,
            int stock) {

        try {

            return new Product(
                    id,
                    name,
                    price,
                    stock
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Product creation error: "
                            + e.getMessage()
            );

            return null;
        }
    }
}