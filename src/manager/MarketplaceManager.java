package manager;

import factory.ProductFactory;
import model.Order;
import model.Product;
import model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketplaceManager {

    // Singleton instance
    private static MarketplaceManager instance;

    private final List<User> users;
    private final List<Product> products;
    private final List<Order> orders;

    // Private constructor
    private MarketplaceManager() {
        users = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
    }

    // Get Singleton instance
    public static MarketplaceManager getInstance() {
        if (instance == null) {
            instance = new MarketplaceManager();
        }
        return instance;
    }

    // =========================
    // USER MANAGEMENT
    // =========================

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        if (findUserById(user.getId()) != null) {
            throw new IllegalArgumentException("User ID already exists.");
        }

        users.add(user);
    }

    public boolean removeUser(String userId) {
        User user = findUserById(userId);

        if (user == null) {
            return false;
        }

        return users.remove(user);
    }

    public User findUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        for (User user : users) {
            if (user.getId().equalsIgnoreCase(userId.trim())) {
                return user;
            }
        }

        return null;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    // =========================
    // PRODUCT MANAGEMENT
    // =========================

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (findProductById(product.getId()) != null) {
            throw new IllegalArgumentException("Product ID already exists.");
        }

        products.add(product);
    }

    public Product createProduct(
            String type,
            String id,
            String name,
            double price,
            int stock) {

        Product product = ProductFactory.createProduct(
                type,
                id,
                name,
                price,
                stock
        );

        addProduct(product);

        return product;
    }

    public Product createProduct(
            String type,
            String id,
            String name,
            double price) {

        return createProduct(
                type,
                id,
                name,
                price,
                0
        );
    }

    public boolean removeProduct(String productId) {
        Product product = findProductById(productId);

        if (product == null) {
            return false;
        }

        return products.remove(product);
    }

    public Product findProductById(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return null;
        }

        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId.trim())) {
                return product;
            }
        }

        return null;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    // =========================
    // ORDER MANAGEMENT
    // =========================

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        if (findOrderById(order.getId()) != null) {
            throw new IllegalArgumentException("Order ID already exists.");
        }

        orders.add(order);
    }

    public boolean removeOrder(String orderId) {
        Order order = findOrderById(orderId);

        if (order == null) {
            return false;
        }

        return orders.remove(order);
    }

    public Order findOrderById(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return null;
        }

        for (Order order : orders) {
            if (order.getId().equalsIgnoreCase(orderId.trim())) {
                return order;
            }
        }

        return null;
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    // =========================
    // STATISTICS
    // =========================

    public int getUserCount() {
        return users.size();
    }

    public int getProductCount() {
        return products.size();
    }

    public int getOrderCount() {
        return orders.size();
    }

    @Override
    public String toString() {
        return "MarketplaceManager{" +
                "users=" + users.size() +
                ", products=" + products.size() +
                ", orders=" + orders.size() +
                '}';
    }
};