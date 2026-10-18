package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Cart {
  private final List<CartItem> items = new ArrayList<>();

  public List<CartItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public CartItem findByProductId(String productId) {
    if (productId == null || productId.trim().isEmpty()) {
      return null;
    }
    for (CartItem item : items) {
      if (item.getProduct().getId().equalsIgnoreCase(productId.trim())) {
        return item;
      }
    }
    return null;
  }

  public List<CartItem> findByKeyword(String keyword) {
    List<CartItem> results = new ArrayList<>();
    if (keyword == null || keyword.trim().isEmpty()) {
      return results;
    }
    String normalized = keyword.trim().toLowerCase(Locale.ROOT);
    for (CartItem item : items) {
      Product product = item.getProduct();
      if (product.getId().toLowerCase(Locale.ROOT).contains(normalized)
          || product.getName().toLowerCase(Locale.ROOT).contains(normalized)) {
        results.add(item);
      }
    }
    return results;
  }

  public void addProduct(Product product, int quantity) {
    if (product == null) {
      throw new IllegalArgumentException("Product cannot be null.");
    }
    CartItem existing = findByProductId(product.getId());
    long total = (long) quantity + (existing == null ? 0 : existing.getQuantity());
    if (quantity <= 0 || total > product.getStock()) {
      throw new IllegalArgumentException("Quantity must be positive and within available stock.");
    }
    if (existing == null) {
      items.add(new CartItem(product, quantity));
    } else {
      existing.setQuantity((int) total);
    }
  }

  public void updateQuantity(String productId, int quantity) {
    CartItem item = findByProductId(productId);
    if (item == null) {
      throw new IllegalArgumentException("Product is not in the cart.");
    }
    item.setQuantity(quantity);
  }

  public boolean removeProduct(String productId) {
    CartItem item = findByProductId(productId);
    return item != null && items.remove(item);
  }

  public int getTotalQuantity() {
    int total = 0;
    for (CartItem item : items) {
      total = Math.addExact(total, item.getQuantity());
    }
    return total;
  }

  public double calculateTotal() {
    double total = 0;
    for (CartItem item : items) {
      total += item.calculateSubtotal();
    }
    return total;
  }

  public void clear() {
    items.clear();
  }
}
