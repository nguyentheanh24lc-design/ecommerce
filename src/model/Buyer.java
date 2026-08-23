package model;

public class Buyer extends User {

    private String shippingAddress;

    public Buyer() {
        this(
                "B000",
                "Buyer1",
                "buyer1@gmail.com",
                "123 Main St"
        );
    }

    public Buyer(
            String id,
            String name,
            String email,
            String shippingAddress) {

        super(id, name, email);
        setShippingAddress(shippingAddress);
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {

        if (shippingAddress == null ||
                shippingAddress.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Shipping address cannot be empty."
            );
        }

        this.shippingAddress = shippingAddress.trim();
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Buyer");
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}