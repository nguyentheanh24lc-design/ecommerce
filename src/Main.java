import manager.MarketplaceManager;
import model.Admin;
import model.Buyer;
import model.Product;
import model.Seller;

public class Main {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println(" E-COMMERCE MANAGEMENT SYSTEM ");
        System.out.println("=================================");

        // Get Singleton instance
        MarketplaceManager manager1 = MarketplaceManager.getInstance();
        MarketplaceManager manager2 = MarketplaceManager.getInstance();

        // Check Singleton
        System.out.println("\n--- SINGLETON TEST ---");
        System.out.println("manager1 == manager2: " + (manager1 == manager2));

        // Create users
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

        // Add users
        manager1.addUser(buyer);
        manager1.addUser(seller);
        manager1.addUser(admin);

        // Create products
        Product product1 = new Product(
                "P001",
                "Laptop",
                15000000,
                10
        );

        Product product2 = new Product(
                "P002",
                "Mouse",
                500000,
                20
        );

        // Add products
        manager1.addProduct(product1);
        manager1.addProduct(product2);

        // Display data through manager2
        System.out.println("\n--- DATA FROM manager2 ---");
        System.out.println("Users: " + manager2.getUserCount());
        System.out.println("Products: " + manager2.getProductCount());
        System.out.println("Orders: " + manager2.getOrderCount());

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
        System.out.println(" Singleton test completed!");
        System.out.println("=================================");
    }
}