import model.Admin;
import model.Buyer;
import model.Seller;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<User> users = new ArrayList<>();

        users.add(
                new Buyer(
                        "B001",
                        "Nguyen Van A",
                        "vana@gmail.com",
                        "Ha Noi"
                )
        );

        users.add(
                new Seller(
                        "S001",
                        "Tran Van B",
                        "seller@gmail.com",
                        "Tech Store"
                )
        );

        users.add(
                new Admin(
                        "A001",
                        "Le Van C",
                        "admin@gmail.com",
                        "HIGH"
                )
        );

        for (User user : users) {
            user.displayRole();
            System.out.println(user);
            System.out.println("------------------");
        }
    }
}