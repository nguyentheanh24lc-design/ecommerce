package strategy;

public interface PaymentStrategy {

  void pay(double amount);

  default void showPaymentInfo() {
    System.out.println("Payment method is ready.");
  }
}