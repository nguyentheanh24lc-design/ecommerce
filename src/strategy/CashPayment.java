package strategy;

public class CashPayment implements PaymentStrategy {

  private static final double MIN_PAYMENT = 1.0;

  private double lastPaymentAmount;

  public CashPayment() {
    this(0);
  }

  public CashPayment(double lastPaymentAmount) {
    setLastPaymentAmount(lastPaymentAmount);
  }

  public double getLastPaymentAmount() {
    return lastPaymentAmount;
  }

  public void setLastPaymentAmount(double lastPaymentAmount) {

    if (lastPaymentAmount < 0) {
      throw new IllegalArgumentException(
          "Payment amount cannot be negative."
      );
    }

    this.lastPaymentAmount = lastPaymentAmount;
  }

  @Override
  public void pay(double amount) {

    if (amount < MIN_PAYMENT) {
      throw new IllegalArgumentException(
          "Payment amount must be greater than 0."
      );
    }

    lastPaymentAmount = amount;

    System.out.println(
        "Paid " + amount + " VND by cash."
    );
  }

  @Override
  public void showPaymentInfo() {
    System.out.println("Payment method: Cash");
    System.out.println(
        "Last payment: " + lastPaymentAmount + " VND"
    );
  }

  @Override
  public String toString() {
    return "CashPayment{" +
        "lastPaymentAmount=" + lastPaymentAmount +
        '}';
  }
}