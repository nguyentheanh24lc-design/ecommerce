package util;

import model.Product;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

  private static final String PRODUCT_FILE =
      "products.csv";

  private FileManager() {
    // Prevent creating FileManager objects
  }

  // =========================
  // SAVE PRODUCTS
  // =========================

  public static void saveProducts(
      List<Product> products) {

    try (BufferedWriter writer =
            new BufferedWriter(
              new FileWriter(PRODUCT_FILE))) {

      writer.write("id,name,price,stock");
      writer.newLine();

      for (Product product : products) {

        writer.write(
            product.getId() + ","
                + product.getName() + ","
                + product.getPrice() + ","
                + product.getStock()
        );

        writer.newLine();
      }

      System.out.println(
          "Products saved successfully."
      );

    } catch (IOException e) {

      System.out.println(
          "Error saving products: "
              + e.getMessage()
      );
    }
  }

  // =========================
  // LOAD PRODUCTS
  // =========================

  public static List<Product> loadProducts() {

    List<Product> products = new ArrayList<>();

    File file = new File(PRODUCT_FILE);

    if (!file.exists()) {
      return products;
    }

    try (BufferedReader reader =
            new BufferedReader(
              new FileReader(PRODUCT_FILE))) {

      // Skip CSV header
      reader.readLine();

      String line;

      while ((line = reader.readLine()) != null) {

        if (line.trim().isEmpty()) {
          continue;
        }

        String[] data = line.split(",");

        if (data.length != 4) {
          System.out.println(
              "Invalid product data: "
                  + line
          );
          continue;
        }

        try {

          String id = data[0].trim();
          String name = data[1].trim();
          double price =
              Double.parseDouble(data[2].trim());
          int stock =
              Integer.parseInt(data[3].trim());

          Product product = new Product(
              id,
              name,
              price,
              stock
          );

          products.add(product);

        } catch (NumberFormatException e) {

          System.out.println(
              "Invalid number in product data: "
                  + line
          );

        } catch (IllegalArgumentException e) {

          System.out.println(
              "Invalid product data: "
                  + e.getMessage()
          );
        }
      }

    } catch (IOException e) {

      System.out.println(
          "Error loading products: "
              + e.getMessage()
      );
    }

    return products;
  }
}