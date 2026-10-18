import manager.MarketplaceManager;
import model.*;
import util.DataStore;

// Run each phase in a separate JVM, from an empty temporary directory.
public class PersistenceSmokeTest {
  private static void check(boolean value) {
    if (!value) throw new AssertionError("Persistence check failed");
  }

  public static void main(String[] args) throws Exception {
    MarketplaceManager m = MarketplaceManager.getInstance();
    if (args[0].equals("write")) {
      Buyer buyer = new Buyer("B1", "Người, mua \"A\"", "buyer@test.com", "Đường 1\nQuận 2");
      Buyer other = new Buyer("B2", "Buyer Two", "two@test.com", "Address");
      m.addUser(buyer); m.addUser(other);
      m.addUser(new Seller("S1", "Seller One", "seller@test.com", "Shop, One"));
      m.addUser(new Admin("A1", "Admin One", "admin@test.com", "HIGH"));
      Product p = new Product("P1", "Máy, tính \"A\"", 100, 5);
      m.addProduct(p);
      other.getCart().addProduct(p, 5);
      buyer.getCart().addProduct(p, 3);
      m.createOrderFromCart("O1", "B1");
      p.setPrice(200);
      DataStore.save(m);
    } else {
      DataStore.load(m);
      Buyer buyer = (Buyer) m.findUserById("B1");
      Buyer other = (Buyer) m.findUserById("B2");
      Order order = m.findOrderById("O1");
      check(m.getUserCount() == 4 && m.getOrderCount() == 1);
      check(buyer.getName().equals("Người, mua \"A\""));
      check(buyer.getShippingAddress().equals("Đường 1\nQuận 2"));
      check(m.findProductById("P1").getName().equals("Máy, tính \"A\""));
      check(order.calculateTotal() == 300 && order.getItems().get(0).getUnitPrice() == 100);
      check(order.getItems().get(0).getProduct() == m.findProductById("P1"));
      check(order.getBuyer() == buyer && buyer.getCart().getItems().isEmpty());
      check(other.getCart().getTotalQuantity() == 5);
      if (args[0].equals("cancel")) {
        check(m.findProductById("P1").getStock() == 2);
        m.cancelOrder("O1");
        DataStore.save(m);
      } else {
        check(order.getStatus().equals(Order.STATUS_CANCELLED));
        check(m.findProductById("P1").getStock() == 5);
        try {
          m.cancelOrder("O1");
          throw new AssertionError("Repeated cancellation accepted");
        } catch (IllegalStateException expected) { }
      }
    }
    System.out.println("PASS: " + args[0]);
  }
}
