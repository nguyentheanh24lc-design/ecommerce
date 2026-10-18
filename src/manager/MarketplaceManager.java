package manager;

import factory.ProductFactory;
import model.Order;
import model.Product;
import model.Searchable;
import model.User;
import model.Buyer;
import model.CartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketplaceManager implements Searchable<Product> {

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

    for (Order order : orders) {
      if (order.getBuyer() == user) {
        throw new IllegalArgumentException("Cannot remove a buyer referenced by an order.");
      }
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

  public List<User> findUsersByKeyword(String keyword) {
    List<User> results = new ArrayList<>();
    if (keyword == null || keyword.trim().isEmpty()) {
      return results;
    }
    String normalized = keyword.trim().toLowerCase(java.util.Locale.ROOT);
    for (User user : users) {
      if (user.getId().toLowerCase(java.util.Locale.ROOT).contains(normalized)
          || user.getName().toLowerCase(java.util.Locale.ROOT).contains(normalized)
          || user.getEmail().toLowerCase(java.util.Locale.ROOT).contains(normalized)) {
        results.add(user);
      }
    }
    return results;
  }

  public int getUserCountByType(Class<? extends User> type) {
    if (type == null) {
      throw new IllegalArgumentException("User type cannot be null.");
    }
    int count = 0;
    for (User user : users) {
      if (type.isInstance(user)) {
        count++;
      }
    }
    return count;
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

    for (Order order : orders) {
      for (var item : order.getItems()) {
        if (item.getProduct() == product) {
          throw new IllegalArgumentException("Cannot remove a product referenced by an order.");
        }
      }
    }
    for (User user : users) {
      if (user instanceof Buyer
          && ((Buyer) user).getCart().findByProductId(productId) != null) {
        throw new IllegalArgumentException("Cannot remove a product referenced by a cart.");
      }
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

  @Override
  public Product findById(String id) {
    return findProductById(id);
  }

  @Override
  public List<Product> findByKeyword(String keyword) {

    List<Product> results = new ArrayList<>();

    if (keyword == null || keyword.trim().isEmpty()) {
      return results;
    }

    String normalizedKeyword = keyword.trim().toLowerCase();

    for (Product product : products) {

      if (product.getName()
          .toLowerCase()
          .contains(normalizedKeyword)) {

        results.add(product);
      }
    }

    return results;
  }

  public List<Product> getProducts() {
    return Collections.unmodifiableList(products);
  }

  // =========================
  // ORDER MANAGEMENT
  // =========================

  public Order createOrderFromCart(String orderId, String buyerId) {
    User user = findUserById(buyerId);
    if (!(user instanceof Buyer)) {
      throw new IllegalArgumentException("Buyer does not exist.");
    }
    Buyer buyer = (Buyer) user;
    Order order = new Order(orderId, buyer);
    if (findOrderById(order.getId()) != null) {
      throw new IllegalArgumentException("Order ID already exists.");
    }
    if (buyer.getCart().getItems().isEmpty()) {
      throw new IllegalArgumentException("Cannot create an order from an empty cart.");
    }

    // Build and validate the entire order before changing stock or the cart.
    for (CartItem item : buyer.getCart().getItems()) {
      Product product = findProductById(item.getProduct().getId());
      if (product == null || product != item.getProduct()) {
        throw new IllegalArgumentException("Product is no longer available: "
            + item.getProduct().getId() + ". Remove it from the cart and add it again.");
      }
      if (item.getQuantity() > product.getStock()) {
        throw new IllegalArgumentException("Not enough stock for product: " + product.getId());
      }
      order.addItem(product, item.getQuantity());
    }

    for (var item : order.getItems()) {
      item.getProduct().decreaseStock(item.getQuantity());
    }
    orders.add(order);
    buyer.getCart().clear();
    return order;
  }

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

  private Order requireOrder(String orderId) {
    Order order = findOrderById(orderId);
    if (order == null) {
      throw new IllegalArgumentException("Order does not exist.");
    }
    return order;
  }

  public void confirmOrder(String orderId) {
    requireOrder(orderId).confirmOrder();
  }

  public void completeOrder(String orderId) {
    requireOrder(orderId).completeOrder();
  }

  public void cancelOrder(String orderId) {
    Order order = requireOrder(orderId);
    if (!Order.STATUS_PENDING.equals(order.getStatus())
        && !Order.STATUS_CONFIRMED.equals(order.getStatus())) {
      throw new IllegalStateException("Only pending or confirmed orders can be cancelled.");
    }
    // Validate all stock restorations before changing any product or status.
    for (var item : order.getItems()) {
      if (findProductById(item.getProduct().getId()) != item.getProduct()) {
        throw new IllegalStateException("Cannot restore stock: product is no longer available.");
      }
      if ((long) item.getProduct().getStock() + item.getQuantity() > Integer.MAX_VALUE) {
        throw new IllegalStateException("Cannot restore stock: quantity exceeds the supported limit.");
      }
    }
    order.cancelOrder();
    for (var item : order.getItems()) {
      item.getProduct().increaseStock(item.getQuantity());
    }
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

  public double calculateTotalRevenue() {

  double totalRevenue = 0;

  for (Order order : orders) {

    if (!Order.STATUS_CANCELLED.equals(
        order.getStatus())) {

      totalRevenue += order.calculateTotal();
    }
  }

  return totalRevenue;
  }

  public int calculateTotalItemsSold() {

  int totalItemsSold = 0;

  for (Order order : orders) {

    if (Order.STATUS_CANCELLED.equals(
        order.getStatus())) {
      continue;
    }

    totalItemsSold += order.getTotalQuantity();
  }

  return totalItemsSold;
  }

  public Product findBestSellingProduct() {

  Product bestProduct = null;
  int highestQuantity = 0;

  for (Product product : products) {

    int soldQuantity = 0;

    for (Order order : orders) {

      if (Order.STATUS_CANCELLED.equals(
          order.getStatus())) {
        continue;
      }

      for (var item : order.getItems()) {

        if (item.getProduct()
            .equals(product)) {

          soldQuantity +=
              item.getQuantity();
        }
      }
    }

    if (soldQuantity > highestQuantity) {

      highestQuantity = soldQuantity;
      bestProduct = product;
    }
  }

  return bestProduct;
  }

  public int getSoldQuantity(
    String productId) {

  if (productId == null
      || productId.trim().isEmpty()) {

    return 0;
  }

  int total = 0;

  for (Order order : orders) {

    if (Order.STATUS_CANCELLED.equals(
        order.getStatus())) {
      continue;
    }

    for (var item : order.getItems()) {

      if (item.getProduct()
          .getId()
          .equalsIgnoreCase(
              productId.trim())) {

        total += item.getQuantity();
      }
    }
  }

  return total;
  }

  public Product findLowestStockProduct() {

  if (products.isEmpty()) {
    return null;
  }

  Product lowestStockProduct =
      products.get(0);

  for (Product product : products) {

    if (product.getStock()
        < lowestStockProduct.getStock()) {

      lowestStockProduct = product;
    }
  }

  return lowestStockProduct;
  }

  @Override
  public String toString() {
    return "MarketplaceManager{" +
        "users=" + users.size() +
        ", products=" + products.size() +
        ", orders=" + orders.size() +
        '}';
  }
}
