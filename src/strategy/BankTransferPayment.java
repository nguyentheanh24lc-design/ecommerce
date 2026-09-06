package strategy;

public class BankTransferPayment implements PaymentStrategy {

    private static final double MIN_PAYMENT = 1.0;

    private String bankAccount;
    private double lastPaymentAmount;

    public BankTransferPayment() {
        this("UNKNOWN", 0);
    }

    public BankTransferPayment(String bankAccount) {
        this(bankAccount, 0);
    }

    public BankTransferPayment(
            String bankAccount,
            double lastPaymentAmount) {

        setBankAccount(bankAccount);
        setLastPaymentAmount(lastPaymentAmount);
    }

    // =========================
    // BANK ACCOUNT
    // =========================

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {

        if (bankAccount == null
                || bankAccount.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Bank account cannot be empty."
            );
        }

        this.bankAccount = bankAccount.trim();
    }

    // =========================
    // PAYMENT AMOUNT
    // =========================

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

    // =========================
    // PAYMENT
    // =========================

    @Override
    public void pay(double amount) {

        if (amount < MIN_PAYMENT) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than 0."
            );
        }

        lastPaymentAmount = amount;

        System.out.println(
                "Paid " + amount
                        + " VND by bank transfer."
        );
    }

    @Override
    public void showPaymentInfo() {

        System.out.println(
                "Payment method: Bank Transfer"
        );

        System.out.println(
                "Bank account: " + bankAccount
        );

        System.out.println(
                "Last payment: "
                        + lastPaymentAmount
                        + " VND"
        );
    }

    @Override
    public String toString() {

        return "BankTransferPayment{" +
                "bankAccount='" + bankAccount + '\'' +
                ", lastPaymentAmount=" +
                lastPaymentAmount +
                '}';
    }
}