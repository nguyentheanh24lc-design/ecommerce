package model;

public class Seller extends User {

    private String shopName;

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
                '}';
    }
}