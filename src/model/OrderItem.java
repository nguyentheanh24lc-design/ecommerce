package model;

import exception.InvalidQuantityException;

import java.util.Objects;

public class OrderItem {

  private Product product;
  private int quantity;

  public OrderItem() {
    this(new Product(), 1);
  }

  public OrderItem(Product product, int quantity) {
    setProduct(product);

    try {
      setQuantity(quantity);
    } catch (InvalidQuantityException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  // =========================
  // PRODUCT
  // =========================

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException(
          "Product cannot be null."
      );
    }

    this.product = product;
  }

  // =========================
  // QUANTITY
  // =========================

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity)
      throws InvalidQuantityException {

    if (quantity <= 0) {
      throw new InvalidQuantityException(
          "Product quantity must be greater than 0."
      );
    }

    this.quantity = quantity;
  }

  // =========================
  // BUSINESS METHODS
  // =========================

  public double calculateSubtotal() {
    return product.getPrice() * quantity;
  }

  public void increaseQuantity(int amount) {

    if (amount <= 0) {
      throw new IllegalArgumentException(
          "Increase amount must be greater than 0."
      );
    }

    quantity += amount;
  }

  public void decreaseQuantity(int amount) {

    if (amount <= 0) {
      throw new IllegalArgumentException(
          "Decrease amount must be greater than 0."
      );
    }

    if (quantity - amount <= 0) {
      throw new IllegalArgumentException(
          "Quantity must be greater than 0."
      );
    }

    quantity -= amount;
  }

  // =========================
  // OBJECT METHODS
  // =========================

  @Override
  public String toString() {
    return "OrderItem{" +
        "productId='" + product.getId() + '\'' +
        ", productName='" + product.getName() + '\'' +
        ", quantity=" + quantity +
        ", subtotal=" + calculateSubtotal() +
        '}';
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof OrderItem)) {
      return false;
    }

    OrderItem orderItem = (OrderItem) obj;

    return Objects.equals(
        product.getId(),
        orderItem.product.getId()
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(product.getId());
  }
}