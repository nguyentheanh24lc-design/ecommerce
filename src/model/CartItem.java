package model;

public class CartItem {
  private final Product product;
  private int quantity;

  public CartItem(Product product, int quantity) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null.");
    }
    this.product = product;
    setQuantity(quantity);
  }

  public Product getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    if (quantity <= 0 || quantity > product.getStock()) {
      throw new IllegalArgumentException("Quantity must be positive and within available stock.");
    }
    this.quantity = quantity;
  }

  public double calculateSubtotal() {
    return product.getPrice() * quantity;
  }

  @Override
  public String toString() {
    return "CartItem{productId='" + product.getId()
        + "', productName='" + product.getName()
        + "', quantity=" + quantity
        + ", subtotal=" + calculateSubtotal() + '}';
  }
}
