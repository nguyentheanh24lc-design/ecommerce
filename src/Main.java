import factory.ProductFactory;
import manager.MarketplaceManager;
import model.Admin;
import model.Buyer;
import model.OrderItem;
import model.Product;
import model.Seller;
import model.Order;
import strategy.BankTransferPayment;

public class Main {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" E-COMMERCE MANAGEMENT SYSTEM ");
        System.out.println("=================================");

        // =========================
        // SINGLETON
        // =========================

        MarketplaceManager manager1 =
                MarketplaceManager.getInstance();

        MarketplaceManager manager2 =
                MarketplaceManager.getInstance();

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

        System.out.println("\n--- FACTORY TEST ---");

        Product product1 = manager1.createProduct(
                ProductFactory.TYPE_BASIC,
                "P001",
                "Laptop",
                15000000,
                10
        );

        Product product2 = manager1.createProduct(
                ProductFactory.TYPE_PREMIUM,
                "P002",
                "Mouse",
                500000,
                20
        );

        System.out.println(product1);
        System.out.println(product2);

        // =========================
        // ORDER + STRATEGY TEST
        // =========================

        System.out.println("\n--- ORDER PAYMENT TEST ---");

        Order order = new Order(
                "O001",
                buyer
        );

        order.addItem(product1, 2);
        order.addItem(product2, 1);

        // Select payment strategy
        order.setPaymentStrategy(
                new BankTransferPayment("123456789")
        );

        System.out.println(
                "Order total: "
                        + order.calculateTotal()
        );

        order.showPaymentInfo();

        order.pay();

        manager1.addOrder(order);

        // =========================
        // TEST INVALID PRICE
        // =========================

        System.out.println("\n--- INVALID PRICE TEST ---");

        try {

            Product invalidPriceProduct =
                    manager1.createProduct(
                            ProductFactory.TYPE_BASIC,
                            "P999",
                            "Invalid Product",
                            -100,
                            10
                    );

            System.out.println(invalidPriceProduct);

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Product creation error: "
                            + e.getMessage()
            );

            System.out.println(
                    "Invalid product was rejected successfully."
            );
        }

        // =========================
        // TEST INVALID QUANTITY
        // =========================

        System.out.println("\n--- INVALID QUANTITY TEST ---");

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

        // =========================
        // DISPLAY USERS
        // =========================

        System.out.println("\n--- USERS ---");

        for (var user : manager2.getUsers()) {

            user.displayRole();

            System.out.println(user);

            System.out.println("------------------");
        }

        // =========================
        // DISPLAY PRODUCTS
        // =========================

        System.out.println("\n--- PRODUCTS ---");

        for (Product product : manager2.getProducts()) {

            System.out.println(product);
        }

        // =========================
        // END
        // =========================

        System.out.println("\n=================================");
        System.out.println(" Factory integration completed!");
        System.out.println("=================================");
    }
}