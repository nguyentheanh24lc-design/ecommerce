package strategy;

public class CashPayment implements PaymentStrategy {

    @Override
    public void pay(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than 0."
            );
        }

        System.out.println(
                "Paid " + amount + " VND by cash."
        );
    }

    @Override
    public void showPaymentInfo() {
        System.out.println("Payment method: Cash");
    }
}