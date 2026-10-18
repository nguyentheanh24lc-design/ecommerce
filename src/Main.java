import exception.InvalidPriceException;
import factory.ProductFactory;
import manager.MarketplaceManager;
import model.Product;
import model.Order;
import model.User;
import model.Buyer;
import model.Seller;
import model.Admin;
import model.Cart;
import model.CartItem;
import util.DataStore;

import java.util.List;
import java.util.Scanner;

public class Main {

  private static final Scanner scanner =
      new Scanner(System.in);

  private static final MarketplaceManager manager =
      MarketplaceManager.getInstance();

  public static void main(String[] args) {

    try {
      DataStore.load(manager);
    } catch (RuntimeException e) {
      System.out.println("Không thể tải dữ liệu: " + e.getMessage());
      System.out.println("Chương trình dừng để tránh ghi đè dữ liệu lỗi.");
      return;
    }

    boolean running = true;

    while (running) {

      showMainMenu();

      int choice = readInt(
          "Chọn chức năng: "
      );

      try {
        switch (choice) {
          case 1: productMenu(); break;
          case 2: showAllProducts(); break;
          case 3: searchProduct(); break;
          case 4: showBasicStatistics(); break;
          case 5: showBusinessReports(); break;
          case 6: userMenu(); break;
          case 7: cartMenu(); break;
          case 8: orderMenu(); break;
          case 0:
            saveData();
            running = false;
            System.out.println("Đã thoát chương trình.");
            break;
          default: System.out.println("Lựa chọn không hợp lệ.");
        }
      } catch (IllegalStateException e) {
        System.out.println(e.getMessage());
        System.out.println("Dữ liệu trong phiên vẫn được giữ. Hãy khắc phục lỗi và thử lưu lại.");
      }
    }

    scanner.close();
  }

  // ==============================
  // MAIN MENU
  // ==============================

  private static void showMainMenu() {

    System.out.println();
    System.out.println(
        "========================================"
    );
    System.out.println(
        "       E-COMMERCE MANAGEMENT SYSTEM"
    );
    System.out.println(
        "========================================"
    );
    System.out.println(
    "1. Quản lý sản phẩm"
    );
    System.out.println(
        "2. Xem tất cả sản phẩm"
    );
    System.out.println(
        "3. Tìm kiếm sản phẩm"
    );
    System.out.println(
        "4. Thống kê sản phẩm"
    );

    System.out.println(
        "5. Báo cáo kinh doanh"
    );

    System.out.println(
        "6. Quản lý người dùng"
    );
    System.out.println(
        "7. Quản lý giỏ hàng"
    );
    System.out.println(
        "8. Quản lý đơn hàng"
    );
    System.out.println(
        "0. Thoát"
    );
    System.out.println(
        "========================================"
    );
  }

  // ==============================
  // PRODUCT MENU
  // ==============================

  private static void orderMenu() {
    boolean running = true;
    while (running) {
      System.out.println("\n--------- QUẢN LÝ ĐƠN HÀNG ---------");
      System.out.println("1. Xem tất cả đơn hàng");
      System.out.println("2. Tìm và xem chi tiết theo mã đơn");
      System.out.println("3. Xác nhận đơn (PENDING -> CONFIRMED)");
      System.out.println("4. Hoàn tất đơn (CONFIRMED -> COMPLETED)");
      System.out.println("5. Hủy đơn và hoàn tồn kho");
      System.out.println("0. Quay lại");
      int choice = readInt("Chọn chức năng: ");
      try {
        switch (choice) {
          case 1:
            if (manager.getOrders().isEmpty()) {
              System.out.println("Chưa có đơn hàng.");
            }
            for (Order order : manager.getOrders()) {
              System.out.println(order);
            }
            break;
          case 2:
            Order order = manager.findOrderById(readText("Nhập mã đơn: "));
            if (order == null) {
              System.out.println("Không tìm thấy đơn hàng.");
              break;
            }
            System.out.println(order);
            for (var item : order.getItems()) {
              System.out.println(item);
            }
            break;
          case 3:
            manager.confirmOrder(readText("Nhập mã đơn cần xác nhận: "));
            System.out.println("Đã xác nhận đơn hàng.");
            break;
          case 4:
            manager.completeOrder(readText("Nhập mã đơn cần hoàn tất: "));
            System.out.println("Đã hoàn tất đơn hàng.");
            break;
          case 5:
            String id = readText("Nhập mã đơn cần hủy: ");
            Order cancelled = manager.findOrderById(id);
            if (cancelled == null) {
              System.out.println("Không tìm thấy đơn hàng.");
              break;
            }
            System.out.println(cancelled);
            if (!readText("Xác nhận hủy đơn và hoàn tồn kho? (Y/N): ").equalsIgnoreCase("Y")) {
              System.out.println("Đã bỏ qua thao tác hủy đơn.");
              break;
            }
            manager.cancelOrder(id);
            saveData();
            System.out.println("Đã hủy đơn và hoàn tồn kho.");
            break;
          case 0: running = false; break;
          default: System.out.println("Lựa chọn không hợp lệ.");
        }
        DataStore.save(manager);
      } catch (IllegalArgumentException | IllegalStateException e) {
        System.out.println("Không thể thực hiện: " + e.getMessage());
      }
    }
  }

  private static void cartMenu() {
    User user = manager.findUserById(readText("Nhập ID Buyer: "));
    if (!(user instanceof Buyer)) {
      System.out.println("Không tìm thấy Buyer. Hãy thêm Buyer trong quản lý người dùng.");
      return;
    }
    Cart cart = ((Buyer) user).getCart();
    boolean running = true;
    while (running) {
      System.out.println("\n--------- GIỎ HÀNG CỦA " + user.getName() + " ---------");
      System.out.println("1. Thêm sản phẩm");
      System.out.println("2. Sửa số lượng");
      System.out.println("3. Xóa sản phẩm khỏi giỏ");
      System.out.println("4. Tìm theo ID hoặc tên sản phẩm");
      System.out.println("5. Xem giỏ hàng và tổng tiền");
      System.out.println("6. Tạo đơn hàng từ giỏ");
      System.out.println("0. Quay lại");
      try {
        switch (readInt("Chọn chức năng: ")) {
          case 1:
            Product product = manager.findProductById(readText("Nhập ID sản phẩm: "));
            if (product == null) {
              System.out.println("Không tìm thấy sản phẩm.");
              break;
            }
            cart.addProduct(product, readInt("Nhập số lượng thêm: "));
            System.out.println("Đã thêm sản phẩm vào giỏ hàng.");
            break;
          case 2:
            cart.updateQuantity(readText("Nhập ID sản phẩm trong giỏ: "),
                readInt("Nhập số lượng mới (lớn hơn 0): "));
            System.out.println("Đã cập nhật số lượng.");
            break;
          case 3:
            boolean removed = cart.removeProduct(readText("Nhập ID sản phẩm cần xóa khỏi giỏ: "));
            System.out.println(removed ? "Đã xóa sản phẩm khỏi giỏ." : "Sản phẩm không có trong giỏ.");
            break;
          case 4:
            showCartItems(cart.findByKeyword(readText("Nhập ID hoặc tên sản phẩm: ")));
            break;
          case 5:
            showCartItems(cart.getItems());
            System.out.println("Số loại sản phẩm: " + cart.getItems().size());
            System.out.println("Tổng số lượng: " + cart.getTotalQuantity());
            System.out.println("Tổng tiền giỏ hàng: " + cart.calculateTotal());
            break;
          case 6:
            if (cart.getItems().isEmpty()) {
              System.out.println("Giỏ hàng trống, chưa thể tạo đơn.");
              break;
            }
            showCartItems(cart.getItems());
            System.out.println("Tổng tiền: " + cart.calculateTotal());
            String orderId = readText("Nhập mã đơn hàng mới: ");
            if (!readText("Xác nhận tạo đơn? (Y/N): ").equalsIgnoreCase("Y")) {
              System.out.println("Đã hủy thao tác tạo đơn.");
              break;
            }
            Order order = manager.createOrderFromCart(orderId, user.getId());
            saveData();
            System.out.println("Tạo đơn hàng thành công!");
            System.out.println(order);
            for (var item : order.getItems()) {
              System.out.println(item);
            }
            break;
          case 0: running = false; break;
          default: System.out.println("Lựa chọn không hợp lệ.");
        }
        DataStore.save(manager);
      } catch (IllegalArgumentException | IllegalStateException e) {
        System.out.println("Không thể thực hiện: " + e.getMessage());
      }
    }
  }

  private static void showCartItems(List<CartItem> items) {
    if (items.isEmpty()) {
      System.out.println("Không có sản phẩm trong giỏ phù hợp.");
      return;
    }
    for (CartItem item : items) {
      System.out.println(item);
    }
  }

  private static void userMenu() {
    boolean running = true;
    while (running) {
      System.out.println("\n--------- QUẢN LÝ NGƯỜI DÙNG ---------");
      System.out.println("1. Thêm người dùng");
      System.out.println("2. Sửa người dùng");
      System.out.println("3. Xóa người dùng");
      System.out.println("4. Tìm theo ID");
      System.out.println("5. Tìm theo ID, tên hoặc email");
      System.out.println("6. Xem tất cả người dùng");
      System.out.println("7. Thống kê Buyer/Seller/Admin");
      System.out.println("0. Quay lại");
      try {
        switch (readInt("Chọn chức năng: ")) {
          case 1: addUser(); break;
          case 2: editUser(); break;
          case 3: deleteUser(); break;
          case 4:
            User user = manager.findUserById(readText("Nhập ID người dùng: "));
            System.out.println(user == null ? "Không tìm thấy người dùng." : user);
            break;
          case 5:
            showUsers(manager.findUsersByKeyword(readText("Nhập từ khóa: ")));
            break;
          case 6: showUsers(manager.getUsers()); break;
          case 7:
            System.out.println("Buyer: " + manager.getUserCountByType(Buyer.class));
            System.out.println("Seller: " + manager.getUserCountByType(Seller.class));
            System.out.println("Admin: " + manager.getUserCountByType(Admin.class));
            System.out.println("Tổng số người dùng: " + manager.getUserCount());
            break;
          case 0: running = false; break;
          default: System.out.println("Lựa chọn không hợp lệ.");
        }
        DataStore.save(manager);
      } catch (IllegalArgumentException | IllegalStateException e) {
        System.out.println("Không thể thực hiện: " + e.getMessage());
      }
    }
  }

  private static void showUsers(List<User> users) {
    if (users.isEmpty()) {
      System.out.println("Không có người dùng phù hợp.");
      return;
    }
    for (User user : users) {
      System.out.println(user);
    }
    System.out.println("Số người dùng: " + users.size());
  }

  private static void addUser() {
    String id = readText("Nhập ID người dùng: ");
    if (manager.findUserById(id) != null) {
      System.out.println("ID người dùng đã tồn tại.");
      return;
    }
    System.out.println("1. Buyer | 2. Seller | 3. Admin");
    int type = readInt("Chọn loại người dùng: ");
    if (type < 1 || type > 3) {
      System.out.println("Loại người dùng không hợp lệ.");
      return;
    }
    String name = readText("Nhập tên: ");
    String email = readText("Nhập email: ");
    User user;
    switch (type) {
      case 1:
        user = new Buyer(id, name, email, readText("Nhập địa chỉ giao hàng: "));
        break;
      case 2:
        user = new Seller(id, name, email, readText("Nhập tên cửa hàng: "));
        break;
      default:
        user = new Admin(id, name, email, readText("Nhập cấp quyền: "));
    }
    manager.addUser(user);
    System.out.println("Thêm người dùng thành công!");
    System.out.println(user);
  }

  private static void editUser() {
    User user = manager.findUserById(readText("Nhập ID người dùng cần sửa: "));
    if (user == null) {
      System.out.println("Không tìm thấy người dùng.");
      return;
    }
    System.out.println(user);
    String name = readText("Nhập tên mới: ");
    String email = readText("Nhập email mới: ");
    // Validate every field before changing the existing object.
    if (user instanceof Buyer) {
      Buyer validated = new Buyer(user.getId(), name, email,
          readText("Nhập địa chỉ giao hàng mới: "));
      ((Buyer) user).setShippingAddress(validated.getShippingAddress());
    } else if (user instanceof Seller) {
      Seller validated = new Seller(user.getId(), name, email,
          readText("Nhập tên cửa hàng mới: "));
      ((Seller) user).setShopName(validated.getShopName());
    } else if (user instanceof Admin) {
      Admin validated = new Admin(user.getId(), name, email,
          readText("Nhập cấp quyền mới: "));
      ((Admin) user).setPermissionLevel(validated.getPermissionLevel());
    }
    user.setName(name);
    user.setEmail(email);
    System.out.println("Cập nhật người dùng thành công!");
    System.out.println(user);
  }

  private static void deleteUser() {
    String id = readText("Nhập ID người dùng cần xóa: ");
    User user = manager.findUserById(id);
    if (user == null) {
      System.out.println("Không tìm thấy người dùng.");
      return;
    }
    System.out.println(user);
    if (!readText("Bạn có chắc chắn muốn xóa? (Y/N): ").equalsIgnoreCase("Y")) {
      System.out.println("Đã hủy thao tác xóa.");
      return;
    }
    manager.removeUser(id);
    System.out.println("Xóa người dùng thành công!");
  }

  private static void productMenu() {

    boolean running = true;

    while (running) {

      System.out.println();
      System.out.println(
          "========================================"
      );
      System.out.println(
          "          QUẢN LÝ SẢN PHẨM"
      );
      System.out.println(
          "========================================"
      );
      System.out.println(
          "1. Thêm sản phẩm"
      );
      System.out.println(
          "2. Sửa sản phẩm"
      );
      System.out.println(
          "3. Xóa sản phẩm"
      );
      System.out.println(
          "4. Tìm sản phẩm theo ID"
      );
      System.out.println(
          "5. Tìm sản phẩm theo tên"
      );
      System.out.println(
          "6. Xem tất cả sản phẩm"
      );
      System.out.println(
          "7. Thống kê sản phẩm"
      );
      System.out.println(
          "0. Quay lại"
      );
      System.out.println(
          "========================================"
      );

      int choice = readInt(
          "Chọn chức năng: "
      );

      switch (choice) {

        case 1:
          addProduct();
          break;

        case 2:
          editProduct();
          break;

        case 3:
          deleteProduct();
          break;

        case 4:
          searchProductById();
          break;

        case 5:
          searchProduct();
          break;

        case 6:
          showAllProducts();
          break;

        case 7:
          showBasicStatistics();
          break;

        case 0:
          running = false;
          break;

        default:
          System.out.println(
              "Lựa chọn không hợp lệ."
          );
      }
    }
  }

  // ==============================
  // ADD PRODUCT
  // ==============================

  private static void addProduct() {

    System.out.println();
    System.out.println(
        "--------- THÊM SẢN PHẨM ---------"
    );

    String id = readText(
        "Nhập ID sản phẩm: "
    );

    if (manager.findProductById(id) != null) {

      System.out.println(
          "ID sản phẩm đã tồn tại."
      );

      return;
    }

    String name = readText(
        "Nhập tên sản phẩm: "
    );

    double price = readPositiveDouble(
        "Nhập giá sản phẩm: "
    );

    int stock = readNonNegativeInt(
        "Nhập số lượng tồn kho: "
    );

    System.out.println();
    System.out.println(
        "Chọn loại sản phẩm:"
    );
    System.out.println(
        "1. BASIC"
    );
    System.out.println(
        "2. PREMIUM"
    );

    int typeChoice = readInt(
        "Lựa chọn: "
    );

    String type;

    if (typeChoice == 1) {

      type = ProductFactory.TYPE_BASIC;

    } else if (typeChoice == 2) {

      type = ProductFactory.TYPE_PREMIUM;

    } else {

      System.out.println(
          "Loại sản phẩm không hợp lệ."
      );

      return;
    }

    try {

      Product product =
          manager.createProduct(
              type,
              id,
              name,
              price,
              stock
          );

      DataStore.save(manager);

      System.out.println();
      System.out.println(
          "Thêm sản phẩm thành công!"
      );
      System.out.println(product);

    } catch (IllegalArgumentException e) {

      System.out.println(
          "Không thể thêm sản phẩm: "
              + e.getMessage()
      );
    }
  }

  // ==============================
  // EDIT PRODUCT
  // ==============================

  private static void editProduct() {

    System.out.println();
    System.out.println(
        "--------- SỬA SẢN PHẨM ---------"
    );

    String id = readText(
        "Nhập ID sản phẩm cần sửa: "
    );

    Product product =
        manager.findProductById(id);

    if (product == null) {

      System.out.println(
          "Không tìm thấy sản phẩm."
      );

      return;
    }

    System.out.println();
    System.out.println(
        "Thông tin hiện tại:"
    );
    System.out.println(product);

    String newName = readText(
        "Nhập tên mới: "
    );

    double newPrice = readPositiveDouble(
        "Nhập giá mới: "
    );

    int newStock = readNonNegativeInt(
        "Nhập tồn kho mới: "
    );

    try {

    product.setName(newName);
    product.setPrice(newPrice);
    product.setStock(newStock);

    DataStore.save(manager);

    System.out.println();
    System.out.println(
        "Cập nhật sản phẩm thành công!"
    );

    System.out.println(product);

    } catch (InvalidPriceException e) {

    System.out.println(
        "Không thể cập nhật giá: "
            + e.getMessage()
    );

    } catch (IllegalArgumentException e) {

    System.out.println(
        "Không thể cập nhật: "
            + e.getMessage()
    );
    }
  }

  // ==============================
  // DELETE PRODUCT
  // ==============================

  private static void deleteProduct() {

    System.out.println();
    System.out.println(
        "--------- XÓA SẢN PHẨM ---------"
    );

    String id = readText(
        "Nhập ID sản phẩm cần xóa: "
    );

    Product product =
        manager.findProductById(id);

    if (product == null) {

      System.out.println(
          "Không tìm thấy sản phẩm."
      );

      return;
    }

    System.out.println(
        "Sản phẩm sẽ bị xóa:"
    );

    System.out.println(product);

    String confirm = readText(
        "Bạn có chắc chắn muốn xóa? (Y/N): "
    );

    if (!confirm.equalsIgnoreCase("Y")) {

      System.out.println(
          "Đã hủy thao tác xóa."
      );

      return;
    }

    boolean removed;
    try {
      removed = manager.removeProduct(id);
    } catch (IllegalArgumentException e) {
      System.out.println("Không thể xóa sản phẩm: " + e.getMessage());
      return;
    }

    if (removed) {

      DataStore.save(manager);

      System.out.println(
          "Xóa sản phẩm thành công."
      );

    } else {

      System.out.println(
          "Không thể xóa sản phẩm."
      );
    }
  }

  // ==============================
  // SEARCH BY ID
  // ==============================

  private static void searchProductById() {

    System.out.println();
    System.out.println(
        "--------- TÌM THEO ID ---------"
    );

    String id = readText(
        "Nhập ID sản phẩm: "
    );

    Product product =
        manager.findById(id);

    if (product == null) {

      System.out.println(
          "Không tìm thấy sản phẩm."
      );

      return;
    }

    System.out.println(
        "Sản phẩm tìm thấy:"
    );

    System.out.println(product);
  }

  // ==============================
  // SEARCH BY KEYWORD
  // ==============================

  private static void searchProduct() {

    System.out.println();
    System.out.println(
        "--------- TÌM THEO TÊN ---------"
    );

    String keyword = readText(
        "Nhập từ khóa: "
    );

    List<Product> results =
        manager.findByKeyword(keyword);

    if (results.isEmpty()) {

      System.out.println(
          "Không tìm thấy sản phẩm phù hợp."
      );

      return;
    }

    System.out.println();
    System.out.println(
        "Kết quả tìm kiếm:"
    );

    for (Product product : results) {

      System.out.println(product);
    }
  }

  // ==============================
  // SHOW ALL PRODUCTS
  // ==============================

  private static void showAllProducts() {

    System.out.println();
    System.out.println(
        "--------- DANH SÁCH SẢN PHẨM ---------"
    );

    List<Product> products =
        manager.getProducts();

    if (products.isEmpty()) {

      System.out.println(
          "Chưa có sản phẩm."
      );

      return;
    }

    for (Product product : products) {

      System.out.println(product);
    }

    System.out.println();
    System.out.println(
        "Tổng số sản phẩm: "
            + products.size()
    );
  }

  // ==============================
  // STATISTICS
  // ==============================

  private static void showBasicStatistics() {

    System.out.println();
    System.out.println(
        "--------- THỐNG KÊ SẢN PHẨM ---------"
    );

    List<Product> products =
        manager.getProducts();

    int totalProducts =
        products.size();

    long totalStock = 0;

    double totalInventoryValue = 0;

    double highestPrice = 0;

    Product mostExpensiveProduct = null;

    for (Product product : products) {

      totalStock += product.getStock();

      totalInventoryValue +=
          product.getPrice()
              * product.getStock();

      if (product.getPrice() > highestPrice) {

        highestPrice =
            product.getPrice();

        mostExpensiveProduct =
            product;
      }
    }

    System.out.println(
        "Số loại sản phẩm: "
            + totalProducts
    );

    System.out.println(
        "Tổng số lượng tồn kho: "
            + totalStock
    );

    System.out.println(
        "Tổng giá trị tồn kho: "
            + totalInventoryValue
    );

    if (mostExpensiveProduct != null) {

      System.out.println(
          "Sản phẩm có giá cao nhất: "
      );

      System.out.println(
          mostExpensiveProduct
      );
    }
  }

  private static void showBusinessReports() {

    boolean running = true;

    while (running) {

        System.out.println();
        System.out.println(
            "========================================"
        );
        System.out.println(
            "           BÁO CÁO KINH DOANH"
        );
        System.out.println(
            "========================================"
        );

        System.out.println(
            "1. Tổng quan kinh doanh"
        );

        System.out.println(
            "2. Sản phẩm bán chạy nhất"
        );

        System.out.println(
            "3. Báo cáo doanh thu đơn hàng"
        );

        System.out.println(
            "4. Báo cáo tồn kho"
        );

        System.out.println(
            "0. Quay lại"
        );

        System.out.println(
            "========================================"
        );

        int choice = readInt(
            "Chọn chức năng: "
        );

        switch (choice) {

        case 1:
            showBusinessOverview();
            break;

        case 2:
            showBestSellingProduct();
            break;

        case 3:
            showOrderRevenueReport();
            break;

        case 4:
            showInventoryReport();
            break;

        case 0:
            running = false;
            break;

        default:
            System.out.println(
                "Lựa chọn không hợp lệ."
            );
        }
    }
}

    private static void showBusinessOverview() {
    System.out.println("Doanh thu và số lượng bán chỉ tính đơn COMPLETED.");
    System.out.println("Buyer: " + manager.getUserCountByType(Buyer.class));
    System.out.println("Seller: " + manager.getUserCountByType(Seller.class));
    System.out.println("Admin: " + manager.getUserCountByType(Admin.class));
    for (String status : List.of(Order.STATUS_PENDING, Order.STATUS_CONFIRMED,
        Order.STATUS_COMPLETED, Order.STATUS_CANCELLED)) {
      System.out.println("Đơn " + status + ": " + manager.getOrderCountByStatus(status));
    }
    System.out.println("Tổng tồn kho: " + manager.getTotalStock());
    System.out.println("Giá trị tồn kho: " + manager.calculateInventoryValue());
    System.out.println("Số sản phẩm hết hàng: " + manager.getOutOfStockProductCount());

    System.out.println();
    System.out.println(
        "--------- TỔNG QUAN KINH DOANH ---------"
    );

    System.out.println(
        "Số người dùng: "
            + manager.getUserCount()
    );

    System.out.println(
        "Số sản phẩm: "
            + manager.getProductCount()
    );

    System.out.println(
        "Số đơn hàng: "
            + manager.getOrderCount()
    );

    System.out.println(
        "Tổng sản phẩm đã bán: "
            + manager.calculateTotalItemsSold()
    );

    System.out.println(
        "Tổng doanh thu: "
            + manager.calculateTotalRevenue()
    );
    }

    private static void showBestSellingProduct() {
    System.out.println("Chỉ tính sản phẩm trong đơn COMPLETED.");

    System.out.println();
    System.out.println(
        "--------- SẢN PHẨM BÁN CHẠY ---------"
    );

    Product product =
        manager.findBestSellingProduct();

    if (product == null) {

        System.out.println(
            "Chưa có dữ liệu bán hàng."
        );

        return;
    }

    int soldQuantity =
        manager.getSoldQuantity(
            product.getId()
        );

    System.out.println(
        "Sản phẩm bán chạy nhất:"
    );

    System.out.println(product);

    System.out.println(
        "Số lượng đã bán: "
            + soldQuantity
    );
    }

    private static void showOrderRevenueReport() {
    System.out.println("Doanh thu ghi nhận chỉ tính đơn COMPLETED.");

    System.out.println();
    System.out.println(
        "--------- DOANH THU ĐƠN HÀNG ---------"
    );

    if (manager.getOrders().isEmpty()) {

        System.out.println(
            "Chưa có đơn hàng."
        );

        return;
    }

    for (Order order :
        manager.getOrders()) {

        System.out.println("Doanh thu ghi nhận: "
            + (Order.STATUS_COMPLETED.equals(order.getStatus()) ? order.calculateTotal() : 0));

        System.out.println(
            "Mã đơn: "
                + order.getId()
        );

        System.out.println(
            "Trạng thái: "
                + order.getStatus()
        );

        System.out.println(
            "Tổng tiền: "
                + order.calculateTotal()
        );

        System.out.println(
            "----------------------------------------"
        );
    }

    System.out.println(
        "TỔNG DOANH THU: "
            + manager.calculateTotalRevenue()
    );
    }

    private static void showInventoryReport() {

    System.out.println();
    System.out.println(
        "--------- BÁO CÁO TỒN KHO ---------"
    );

    List<Product> products =
        manager.getProducts();

    if (products.isEmpty()) {

        System.out.println(
            "Chưa có sản phẩm."
        );

        return;
    }

    long totalStock = 0;

    double totalInventoryValue = 0;

    for (Product product : products) {

        totalStock += product.getStock();

        totalInventoryValue +=
            product.getPrice()
                * product.getStock();
    }

    Product lowestStockProduct =
        manager.findLowestStockProduct();

    System.out.println(
        "Tổng số lượng tồn kho: "
            + totalStock
    );

    System.out.println(
        "Tổng giá trị tồn kho: "
            + totalInventoryValue
    );

    if (lowestStockProduct != null) {

        System.out.println();
        System.out.println(
            "Sản phẩm có tồn kho thấp nhất:"
        );

        System.out.println(
            lowestStockProduct
        );
    }
    }

  // ==============================
  // FILE PERSISTENCE
  // ==============================

  private static void saveData() {

    DataStore.save(manager);
  }

  // ==============================
  // INPUT HELPERS
  // ==============================

  private static String readText(
      String message) {

    while (true) {

      System.out.print(message);

      String input =
          scanner.nextLine().trim();

      if (!input.isEmpty()) {

        return input;
      }

      System.out.println(
          "Dữ liệu không được để trống."
      );
    }
  }

  private static int readInt(
      String message) {

    while (true) {

      System.out.print(message);

      String input =
          scanner.nextLine().trim();

      try {

        return Integer.parseInt(input);

      } catch (NumberFormatException e) {

        System.out.println(
            "Vui lòng nhập một số nguyên."
        );
      }
    }
  }

  private static int readNonNegativeInt(
      String message) {

    while (true) {

      int value = readInt(message);

      if (value >= 0) {

        return value;
      }

      System.out.println(
          "Giá trị không được nhỏ hơn 0."
      );
    }
  }

  private static double readPositiveDouble(
      String message) {

    while (true) {

      System.out.print(message);

      String input =
          scanner.nextLine().trim();

      try {

        double value =
            Double.parseDouble(input);

        if (Double.isFinite(value) && value > 0) {

          return value;
        }

        System.out.println(
            "Giá phải lớn hơn 0."
        );

      } catch (NumberFormatException e) {

        System.out.println(
            "Vui lòng nhập một số hợp lệ."
        );
      }
    }
  }
}
