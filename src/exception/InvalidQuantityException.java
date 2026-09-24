package exception;

public class InvalidQuantityException extends Exception {

  public InvalidQuantityException(String message) {
    super(message);
  }

  public InvalidQuantityException() {
    this("Product quantity must be greater than 0.");
  }
}