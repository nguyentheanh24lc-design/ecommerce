package exception;

public class InvalidPriceException extends Exception {

    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException() {
        this("Product price must be greater than 0.");
    }
}