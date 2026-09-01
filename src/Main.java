import model.Admin;
import model.Buyer;
import model.Identifiable;
import model.Order;
import model.OrderItem;
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

    Seller seller = new Seller(
            "S001",
            "Tran Van B",
            "seller@gmail.com",
            "Tech Store"
    );

    users.add(seller);

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

    Product product1 = new Product(
            "P001",
            "Laptop Dell",
            25000000,
            10
    );

    Product product2 = new Product(
            "P002",
            "Mechanical Keyboard",
            1500000,
            20
    );

    Product product3 = new Product(
            "P003",
            "Gaming Mouse",
            800000,
            30
    );

    seller.addProduct(product1);
    seller.addProduct(product2);
    seller.addProduct(product3);

    System.out.println("\n--- SELLER PRODUCT LIST ---");

    System.out.println(
            "Shop: " + seller.getShopName()
    );

    System.out.println(
            "Total products: "
                    + seller.getProductCount()
    );

    for (Product product : seller.getProducts()) {
        System.out.println(product);
    }

    System.out.println("\n--- FIND PRODUCT ---");

    Product foundProduct =
            seller.findProductById("P002");

    if (foundProduct != null) {

        System.out.println(
                "Product found: "
                        + foundProduct
        );

    } else {

        System.out.println(
                "Product not found."
        );
    }

    System.out.println("\n--- ORDER ITEM TEST ---");

    OrderItem orderItem = new OrderItem(
            product1,
            2
    );

    System.out.println(orderItem);

    System.out.println(
            "Subtotal: "
                    + orderItem.calculateSubtotal()
    );

    orderItem.increaseQuantity(1);

    System.out.println(
            "After increasing quantity: "
                    + orderItem
    );

    orderItem.decreaseQuantity(1);

    System.out.println(
            "After decreasing quantity: "
                    + orderItem
    );

    System.out.println("\n--- ORDER TEST ---");

    Buyer orderBuyer = new Buyer(
            "B002",
            "Pham Van D",
            "phamvand@gmail.com",
            "Ho Chi Minh City"
    );

    Order order = new Order(
            "O001",
            orderBuyer
    );

    order.addItem(product1, 1);
    order.addItem(product2, 2);
    order.addItem(product3, 3);

    System.out.println(order);

    System.out.println("\n--- ORDER ITEMS ---");

    for (OrderItem item : order.getItems()) {
        System.out.println(item);
    }

    System.out.println(
            "\nTotal quantity: "
                    + order.getTotalQuantity()
    );

    System.out.println(
            "Order total: "
                    + order.calculateTotal()
    );

    System.out.println(
            "\nCurrent status: "
                    + order.getStatus()
    );

    order.confirmOrder();

    System.out.println(
            "After confirm: "
                    + order.getStatus()
    );

    order.completeOrder();

    System.out.println(
            "After complete: "
                    + order.getStatus()
    );

    System.out.println("\n--- INTERFACE TEST ---");

    Identifiable identifiableProduct = product1;

    System.out.println(
        "Product has ID P001: "
                + identifiableProduct.hasId("P001")
    );

    Identifiable identifiableOrder = order;

    System.out.println(
        "Order has ID O001: "
                + identifiableOrder.hasId("O001")
    );
}
}
