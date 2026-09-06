package model;

import strategy.PaymentStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order implements Identifiable {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    private String id;
    private Buyer buyer;
    private LocalDateTime orderDate;
    private String status;
    private PaymentStrategy paymentStrategy;

    private final List<OrderItem> items;

    // =========================
    // CONSTRUCTORS
    // =========================

    public Order() {
        this(
                "O000",
                new Buyer(),
                LocalDateTime.now(),
                STATUS_PENDING
        );
    }

    public Order(String id, Buyer buyer) {
        this(
                id,
                buyer,
                LocalDateTime.now(),
                STATUS_PENDING
        );
    }

    public Order(
            String id,
            Buyer buyer,
            LocalDateTime orderDate,
            String status) {

        setId(id);
        setBuyer(buyer);
        setOrderDate(orderDate);
        setStatus(status);

        this.items = new ArrayList<>();
    }

    // =========================
    // GETTER / SETTER
    // =========================

    public String getId() {
        return id;
    }

    public void setId(String id) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Order ID cannot be empty."
            );
        }

        this.id = id.trim();
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {

        if (buyer == null) {
            throw new IllegalArgumentException(
                    "Buyer cannot be null."
            );
        }

        this.buyer = buyer;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {

        if (orderDate == null) {
            throw new IllegalArgumentException(
                    "Order date cannot be null."
            );
        }

        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Order status cannot be empty."
            );
        }

        String normalizedStatus =
                status.trim().toUpperCase();

        if (!normalizedStatus.equals(STATUS_PENDING)
                && !normalizedStatus.equals(STATUS_CONFIRMED)
                && !normalizedStatus.equals(STATUS_COMPLETED)
                && !normalizedStatus.equals(STATUS_CANCELLED)) {

            throw new IllegalArgumentException(
                    "Invalid order status."
            );
        }

        this.status = normalizedStatus;
    }

    // =========================
    // PAYMENT STRATEGY
    // =========================

    public PaymentStrategy getPaymentStrategy() {
        return paymentStrategy;
    }

    public void setPaymentStrategy(
            PaymentStrategy paymentStrategy) {

        if (paymentStrategy == null) {
            throw new IllegalArgumentException(
                    "Payment strategy cannot be null."
            );
        }

        this.paymentStrategy = paymentStrategy;
    }

    // =========================
    // ORDER ITEMS
    // =========================

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(Product product, int quantity) {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Product cannot be null."
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0."
            );
        }

        for (OrderItem item : items) {

            if (item.getProduct().equals(product)) {

                item.increaseQuantity(quantity);
                return;
            }
        }

        OrderItem newItem =
                new OrderItem(product, quantity);

        items.add(newItem);
    }

    public boolean removeItem(String productId) {

        if (productId == null
                || productId.trim().isEmpty()) {

            return false;
        }

        return items.removeIf(
                item -> item.getProduct()
                        .getId()
                        .equalsIgnoreCase(productId.trim())
        );
    }

    // =========================
    // CALCULATION
    // =========================

    public double calculateTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.calculateSubtotal();
        }

        return total;
    }

    public int getTotalQuantity() {

        int totalQuantity = 0;

        for (OrderItem item : items) {
            totalQuantity += item.getQuantity();
        }

        return totalQuantity;
    }

    // =========================
    // PAYMENT
    // =========================

    public void pay() {

        if (paymentStrategy == null) {
            throw new IllegalStateException(
                    "Payment strategy has not been selected."
            );
        }

        if (items.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot pay for an empty order."
            );
        }

        paymentStrategy.pay(calculateTotal());
    }

    public void showPaymentInfo() {

        if (paymentStrategy == null) {
            System.out.println(
                    "Payment method: Not selected"
            );
            return;
        }

        paymentStrategy.showPaymentInfo();
    }

    // =========================
    // ORDER STATUS
    // =========================

    public void confirmOrder() {

        if (!status.equals(STATUS_PENDING)) {

            throw new IllegalStateException(
                    "Only pending orders can be confirmed."
            );
        }

        status = STATUS_CONFIRMED;
    }

    public void completeOrder() {

        if (!status.equals(STATUS_CONFIRMED)) {

            throw new IllegalStateException(
                    "Only confirmed orders can be completed."
            );
        }

        status = STATUS_COMPLETED;
    }

    public void cancelOrder() {

        if (status.equals(STATUS_COMPLETED)) {

            throw new IllegalStateException(
                    "Completed orders cannot be cancelled."
            );
        }

        status = STATUS_CANCELLED;
    }

    // =========================
    // OBJECT METHODS
    // =========================

    @Override
    public String toString() {

        return "Order{" +
                "id='" + id + '\'' +
                ", buyer='" + buyer.getName() + '\'' +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", totalQuantity=" + getTotalQuantity() +
                ", total=" + calculateTotal() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Order)) {
            return false;
        }

        Order order = (Order) obj;

        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}