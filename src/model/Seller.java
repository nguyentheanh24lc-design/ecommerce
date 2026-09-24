package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Seller extends User {

private String shopName;

private List<Product> products;

public Seller() {
  this(
      "S000",
      "Seller1",
      "seller1@gmail.com",
      "Shop1"
  );
}

public Seller(
    String id,
    String name,
    String email,
    String shopName) {

  super(id, name, email);

  setShopName(shopName);

  this.products = new ArrayList<>();
}

public String getShopName() {
  return shopName;
}

public void setShopName(String shopName) {

  if (shopName == null ||
      shopName.trim().length() < 2) {

    throw new IllegalArgumentException(
        "Shop name must contain at least 2 characters."
    );
  }

  this.shopName = shopName.trim();
}

public List<Product> getProducts() {
  return Collections.unmodifiableList(products);
}

public boolean addProduct(Product product) {

  if (product == null) {
    throw new IllegalArgumentException(
        "Product cannot be null."
    );
  }

  if (products.contains(product)) {
    return false;
  }

  products.add(product);

  return true;
}

public boolean removeProduct(Product product) {

  if (product == null) {
    return false;
  }

  return products.remove(product);
}

public Product findProductById(String productId) {

  if (productId == null ||
      productId.trim().isEmpty()) {

    return null;
  }

  for (Product product : products) {

    if (product.getId().equalsIgnoreCase(
        productId.trim())) {

      return product;
    }
  }

  return null;
}

public int getProductCount() {
  return products.size();
}

@Override
public void displayRole() {
  System.out.println("Role: Seller");
}

@Override
public String toString() {

  return "Seller{" +
      "id='" + getId() + '\'' +
      ", name='" + getName() + '\'' +
      ", email='" + getEmail() + '\'' +
      ", shopName='" + shopName + '\'' +
      ", productCount=" + products.size() +
      '}';
}
}