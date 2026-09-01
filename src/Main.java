import model.Admin;
import model.Buyer;
import model.Product;
import model.Seller;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class Main {

public static void main(String[] args) {

    System.out.println("=================================");
    System.out.println(" E-COMMERCE MANAGEMENT SYSTEM ");
    System.out.println("=================================");

    List<User> users = new ArrayList<>();

    users.add(new Buyer(
            "B001",
            "Nguyen Van A",
            "vana@gmail.com",
            "Ha Noi"
    ));

    users.add(new Seller(
            "S001",
            "Tran Van B",
            "seller@gmail.com",
            "Tech Store"
    ));

    users.add(new Admin(
            "A001",
            "Le Van C",
            "admin@gmail.com",
            "HIGH"
    ));

    System.out.println("\n--- USER LIST ---");

    for (User user : users) {
        user.displayRole();
        System.out.println(user);
    }

    Product product = new Product(
            "P001",
            "Laptop Dell",
            25000000,
            10
    );

    System.out.println("\n--- PRODUCT TEST ---");

    System.out.println(product);

    product.increaseStock(5);

    System.out.println(
            "Stock after increase: " + product.getStock()
    );

    product.decreaseStock(3);

    System.out.println(
            "Stock after decrease: " + product.getStock()
    );

    System.out.println(
            "Total products created: "
                    + Product.getTotalProducts()
    );
}
}