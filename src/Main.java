import model.Buyer;

public class Main {

    public static void main(String[] args) {

        Buyer buyer = new Buyer(
                "B001",
                "Nguyen Van A",
                "vana@gmail.com",
                "Ha Noi"
        );

        buyer.displayRole();

        System.out.println(buyer);
    }
}