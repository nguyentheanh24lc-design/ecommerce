package model;

public class Admin extends User {

  private String permissionLevel;

  public Admin() {
    this(
        "A000",
        "Admin1",
        "admin1@gmail.com",
        "MEDIUM"
    );
  }

  public Admin(
      String id,
      String name,
      String email,
      String permissionLevel) {

    super(id, name, email);
    setPermissionLevel(permissionLevel);
  }

  public String getPermissionLevel() {
    return permissionLevel;
  }

  public void setPermissionLevel(String permissionLevel) {

    if (permissionLevel == null ||
        permissionLevel.trim().isEmpty()) {

      throw new IllegalArgumentException(
          "Permission level cannot be empty."
      );
    }

    this.permissionLevel = permissionLevel.trim().toUpperCase();
  }

  @Override
  public void displayRole() {
    System.out.println("Role: Admin");
  }

  @Override
  public String toString() {
    return "Admin{" +
        "id='" + getId() + '\'' +
        ", name='" + getName() + '\'' +
        ", email='" + getEmail() + '\'' +
        ", permissionLevel='" + permissionLevel + '\'' +
        '}';
  }
}