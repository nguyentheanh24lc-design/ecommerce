package model;

import exception.InvalidPriceException;

import java.util.Objects;

public class Product implements Identifiable {

    public static final double MIN_PRICE = 0.0;

    private static int totalProducts = 0;

    private String id;
    private String name;
    private double price;
    private int stock;

    public Product() {
        this("P000", "Unknown Product", 1.0, 0);
    }

    public Product(String id, String name, double price) {
        this(id, name, price, 0);
    }

    public Product(String id, String name, double price, int stock) {
        setId(id);
        setName(name);

        try {
            setPrice(price);
        } catch (InvalidPriceException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        setStock(stock);
        totalProducts++;
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty.");
        }

        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException(
                    "Product name must contain at least 2 characters."
            );
        }

        this.name = name.trim();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidPriceException {

        if (price <= MIN_PRICE) {
            throw new InvalidPriceException(
                    "Product price must be greater than 0."
            );
        }

        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException(
                    "Product stock cannot be negative."
            );
        }

        this.stock = stock;
    }

    public void increaseStock(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Increase quantity must be greater than 0."
            );
        }

        stock += quantity;
    }

    public void decreaseStock(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Decrease quantity must be greater than 0."
            );
        }

        if (quantity > stock) {
            throw new IllegalArgumentException(
                    "Not enough product in stock."
            );
        }

        stock -= quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Product)) {
            return false;
        }

        Product product = (Product) obj;

        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}