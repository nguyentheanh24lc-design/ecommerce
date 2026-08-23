import model.Buyer;
import model.Seller;

public class Main {

    public static void main(String[] args) {

        Buyer buyer = new Buyer(
                "B001",
                "Nguyen Van A",
                "vana@gmail.com",
                "Ha Noi"
        );

        Seller seller = new Seller(
                "S001",
                "Tran Van B",
                "seller@gmail.com",
                "Tech Store"
        );

        buyer.displayRole();
        seller.displayRole();

        System.out.println();
        System.out.println(buyer);
        System.out.println(seller);
    }
}