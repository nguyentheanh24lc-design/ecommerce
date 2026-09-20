import exception.InvalidPriceException;
import factory.ProductFactory;
import manager.MarketplaceManager;
import model.Product;
import util.FileManager;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final MarketplaceManager manager =
            MarketplaceManager.getInstance();

    public static void main(String[] args) {

        loadProducts();

        boolean running = true;

        while (running) {

            showMainMenu();

            int choice = readInt(
                    "Chọn chức năng: "
            );

            switch (choice) {

                case 1:
                    productMenu();
                    break;

                case 2:
                    showAllProducts();
                    break;

                case 3:
                    searchProduct();
                    break;

                case 4:
                    showBasicStatistics();
                    break;

                case 0:
                    saveProducts();
                    running = false;
                    System.out.println(
                            "Đã thoát chương trình."
                    );
                    break;

                default:
                    System.out.println(
                            "Lựa chọn không hợp lệ."
                    );
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
                "0. Thoát"
        );
        System.out.println(
                "========================================"
        );
    }

    // ==============================
    // PRODUCT MENU
    // ==============================

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

            FileManager.saveProducts(
                    manager.getProducts()
            );

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

        FileManager.saveProducts(
                manager.getProducts()
        );

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

        boolean removed =
                manager.removeProduct(id);

        if (removed) {

            FileManager.saveProducts(
                    manager.getProducts()
            );

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

        int totalStock = 0;

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

    // ==============================
    // FILE PERSISTENCE
    // ==============================

    private static void loadProducts() {

        List<Product> loadedProducts =
                FileManager.loadProducts();

        for (Product product :
                loadedProducts) {

            try {

                manager.addProduct(product);

            } catch (IllegalArgumentException e) {

                System.out.println(
                        "Không thể load sản phẩm: "
                                + e.getMessage()
                );
            }
        }

        if (!loadedProducts.isEmpty()) {

            System.out.println(
                    "Đã tải "
                            + loadedProducts.size()
                            + " sản phẩm từ products.csv."
            );
        }
    }

    private static void saveProducts() {

        FileManager.saveProducts(
                manager.getProducts()
        );
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

                if (value > 0) {

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