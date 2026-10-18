package util;

import manager.MarketplaceManager;
import model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public final class DataStore {
  private static final Path DATA_DIRECTORY = Path.of("data");

  private static Path dataPath(String file) {
    return DATA_DIRECTORY.resolve(file);
  }
  private DataStore() { }

  public static void save(MarketplaceManager manager) {
    Map<String, List<String[]>> files = new LinkedHashMap<>();
    files.put("users.csv", new ArrayList<>(List.<String[]>of(new String[]{"id","role","name","email","detail"})));
    files.put("products.csv", new ArrayList<>(List.<String[]>of(new String[]{"id","name","price","stock"})));
    files.put("orders.csv", new ArrayList<>(List.<String[]>of(new String[]{"id","buyer_id","order_date","status"})));
    files.put("order_items.csv", new ArrayList<>(List.<String[]>of(new String[]{"order_id","product_id","quantity","unit_price"})));
    files.put("cart_items.csv", new ArrayList<>(List.<String[]>of(new String[]{"buyer_id","product_id","quantity"})));
    for (User user : manager.getUsers()) {
      String role;
      String detail;
      if (user instanceof Buyer) {
        role = "BUYER"; detail = ((Buyer) user).getShippingAddress();
        for (CartItem item : ((Buyer) user).getCart().getItems()) {
          files.get("cart_items.csv").add(new String[]{user.getId(), item.getProduct().getId(), "" + item.getQuantity()});
        }
      } else if (user instanceof Seller) {
        role = "SELLER"; detail = ((Seller) user).getShopName();
      } else if (user instanceof Admin) {
        role = "ADMIN"; detail = ((Admin) user).getPermissionLevel();
      } else {
        throw new IllegalArgumentException("Unsupported user role.");
      }
      files.get("users.csv").add(new String[]{user.getId(), role, user.getName(), user.getEmail(), detail});
    }
    for (Product p : manager.getProducts()) {
      files.get("products.csv").add(new String[]{p.getId(), p.getName(), "" + p.getPrice(), "" + p.getStock()});
    }
    for (Order order : manager.getOrders()) {
      files.get("orders.csv").add(new String[]{order.getId(), order.getBuyer().getId(), order.getOrderDate().toString(), order.getStatus()});
      for (OrderItem item : order.getItems()) {
        files.get("order_items.csv").add(new String[]{order.getId(), item.getProduct().getId(), "" + item.getQuantity(), "" + item.getUnitPrice()});
      }
    }
    Map<String, byte[]> previous = new LinkedHashMap<>();
    List<String> replaced = new ArrayList<>();
    try {
      Files.createDirectories(DATA_DIRECTORY);
      // Prepare every file before replacing existing data.
      for (var entry : files.entrySet()) {
        Path target = dataPath(entry.getKey());
        previous.put(entry.getKey(), Files.exists(target) ? Files.readAllBytes(target) : null);
        Files.writeString(dataPath(entry.getKey() + ".tmp"), encode(entry.getValue()), StandardCharsets.UTF_8);
      }
      for (String file : files.keySet()) {
        try {
          Files.move(dataPath(file + ".tmp"), dataPath(file), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
          Files.move(dataPath(file + ".tmp"), dataPath(file), StandardCopyOption.REPLACE_EXISTING);
        }
        replaced.add(file);
      }
    } catch (IOException e) {
      for (String file : replaced) {
        try {
          if (previous.get(file) == null) Files.deleteIfExists(dataPath(file));
          else Files.write(dataPath(file), previous.get(file));
        } catch (IOException restoreError) {
          e.addSuppressed(restoreError);
        }
      }
      throw new IllegalStateException("Cannot save data: " + e.getMessage(), e);
    }
  }

  public static void load(MarketplaceManager manager) {
    if (!manager.getUsers().isEmpty() || !manager.getProducts().isEmpty() || !manager.getOrders().isEmpty()) {
      throw new IllegalStateException("Data must be loaded into an empty manager.");
    }
    List<String> dataset = List.of("users.csv", "products.csv", "orders.csv", "order_items.csv", "cart_items.csv");
    boolean hasSavedDataset = dataset.stream()
        .filter(file -> !file.equals("products.csv"))
        .anyMatch(file -> Files.exists(dataPath(file)));
    if (hasSavedDataset) {
      for (String file : dataset) {
        if (!Files.exists(dataPath(file))) {
          throw new IllegalStateException("Missing data file: " + file);
        }
      }
    }
    // Validate the entire dataset before installing it in the manager.
    Map<String, User> users = new LinkedHashMap<>();
    Map<String, Product> products = new LinkedHashMap<>();
    Map<String, Order> orders = new LinkedHashMap<>();
    for (String[] row : read("users.csv", "id,role,name,email,detail")) {
      User user;
      switch (row[1]) {
        case "BUYER": user = new Buyer(row[0], row[2], row[3], row[4]); break;
        case "SELLER": user = new Seller(row[0], row[2], row[3], row[4]); break;
        case "ADMIN": user = new Admin(row[0], row[2], row[3], row[4]); break;
        default: throw new IllegalArgumentException("Unknown user role: " + row[1]);
      }
      unique(users, user.getId(), user);
    }
    for (String[] row : read("products.csv", "id,name,price,stock")) {
      double price = Double.parseDouble(row[2]);
      if (!Double.isFinite(price)) throw new IllegalArgumentException("Invalid product price.");
      Product p = new Product(row[0], row[1], price, Integer.parseInt(row[3]));
      unique(products, p.getId(), p);
    }
    for (String[] row : read("orders.csv", "id,buyer_id,order_date,status")) {
      User user = users.get(key(row[1]));
      if (!(user instanceof Buyer)) throw new IllegalArgumentException("Missing order buyer: " + row[1]);
      Order order = new Order(row[0], (Buyer) user, LocalDateTime.parse(row[2]), row[3]);
      unique(orders, order.getId(), order);
    }
    Set<List<String>> seenItems = new HashSet<>();
    for (String[] row : read("order_items.csv", "order_id,product_id,quantity,unit_price")) {
      Order order = orders.get(key(row[0]));
      Product p = products.get(key(row[1]));
      if (order == null || p == null || !seenItems.add(List.of(key(row[0]), key(row[1])))) {
        throw new IllegalArgumentException("Invalid or duplicate order item.");
      }
      order.addItem(p, Integer.parseInt(row[2]), Double.parseDouble(row[3]));
    }
    for (Order order : orders.values()) {
      if (order.getItems().isEmpty()) throw new IllegalArgumentException("Saved order has no items: " + order.getId());
    }
    for (String[] row : read("cart_items.csv", "buyer_id,product_id,quantity")) {
      User user = users.get(key(row[0]));
      Product p = products.get(key(row[1]));
      if (!(user instanceof Buyer) || p == null) throw new IllegalArgumentException("Missing cart buyer or product.");
      ((Buyer) user).getCart().restoreProduct(p, Integer.parseInt(row[2]));
    }
    users.values().forEach(manager::addUser);
    products.values().forEach(manager::addProduct);
    // Stock already contains deductions/restorations; loading must not apply them again.
    orders.values().forEach(manager::addOrder);
  }

  private static String key(String id) { return id.trim().toLowerCase(Locale.ROOT); }

  private static <T> void unique(Map<String, T> map, String id, T value) {
    if (map.putIfAbsent(key(id), value) != null) throw new IllegalArgumentException("Duplicate ID: " + id);
  }

  private static String encode(List<String[]> rows) {
    StringBuilder result = new StringBuilder();
    for (String[] row : rows) {
      for (int i = 0; i < row.length; i++) {
        if (i > 0) result.append(',');
        result.append('"').append(row[i].replace("\"", "\"\"")).append('"');
      }
      result.append('\n');
    }
    return result.toString();
  }

  private static List<String[]> read(String file, String header) {
    Path path = dataPath(file);
    if (!Files.exists(path)) return new ArrayList<>();
    try {
      String text = Files.readString(path, StandardCharsets.UTF_8);
      if (text.startsWith("\uFEFF")) text = text.substring(1);
      List<String[]> rows = parse(text);
      if (rows.isEmpty() || !Arrays.equals(rows.remove(0), header.split(","))) {
        throw new IllegalArgumentException("Invalid header in " + file);
      }
      int columns = header.split(",").length;
      for (String[] row : rows) {
        if (row.length != columns) throw new IllegalArgumentException("Invalid column count in " + file);
      }
      return rows;
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read " + file, e);
    }
  }

  private static List<String[]> parse(String text) {
    List<String[]> rows = new ArrayList<>();
    List<String> row = new ArrayList<>();
    StringBuilder field = new StringBuilder();
    boolean quoted = false;
    boolean closed = false;
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (quoted) {
        if (c == '"') {
          if (i + 1 < text.length() && text.charAt(i + 1) == '"') { field.append('"'); i++; }
          else { quoted = false; closed = true; }
        } else field.append(c);
      } else if (c == ',' || c == '\n' || c == '\r') {
        row.add(field.toString()); field.setLength(0); closed = false;
        if (c != ',') {
          if (c == '\r' && i + 1 < text.length() && text.charAt(i + 1) == '\n') i++;
          if (!(row.size() == 1 && row.get(0).isEmpty())) rows.add(row.toArray(new String[0]));
          row.clear();
        }
      } else if (c == '"' && field.length() == 0 && !closed) quoted = true;
      else {
        if (closed || c == '"') throw new IllegalArgumentException("Malformed CSV field.");
        field.append(c);
      }
    }
    if (quoted) throw new IllegalArgumentException("Unclosed CSV quote.");
    if (closed || field.length() > 0 || !row.isEmpty()) {
      row.add(field.toString()); rows.add(row.toArray(new String[0]));
    }
    return rows;
  }
}
