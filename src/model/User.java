package model;

import java.util.Objects;

public abstract class User implements Identifiable {

  public static final String DEFAULT_NAME = "User 1";

  private String id;
  private String name;
  private String email;

  public User() {
    this("U000", DEFAULT_NAME, "user1@gmail.com");
  }

  public User(String id, String name, String email) {
    setId(id);
    setName(name);
    setEmail(email);
  }

  // Getter & Setter
  public String getId() {
    return id;
  }

  public void setId(String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("User ID cannot be empty.");
    }
    this.id = id.trim();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name == null || name.trim().length() < 2) {
      throw new IllegalArgumentException(
          "Name must contain at least 2 characters.");
    }
    this.name = name.trim();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {

    if (email == null ||
        !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {

      throw new IllegalArgumentException(
          "Invalid email format.");
    }

    this.email = email.trim();
  }

  public abstract void displayRole();

  // Override toString
  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        '}';
  }

  // Override equals
  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;

    if (obj == null ||
        getClass() != obj.getClass())
      return false;

    User user = (User) obj;

    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}