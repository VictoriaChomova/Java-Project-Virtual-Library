import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements java.io.Serializable {
    String nickname;
    private String password;
    double deposit;
    String id;
    LocalDateTime registrationDate;
    String email;

    public User() {

    }

    public User(String nickname, String password, String email) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.registrationDate = LocalDateTime.now ();
        this.id = String.valueOf (Instant.now ().getEpochSecond ());
        this.deposit = 0;

    }


    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public boolean checkPassword(String password) {
        return password.equals (this.password);
    }

    public static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile ("[\\w+\\d+\\W+]");
        Matcher matcher = pattern.matcher (password);

        return matcher.find () && password.length () >= 8;
    }

    public static boolean makeDeposit(User loggedUser) {

        Scanner scanner = new Scanner (System.in);
        System.out.println ("Enter the amount you would like to deposit!");

        double amount = Double.parseDouble (scanner.nextLine ());

        try {
            FileInputStream userFile = new FileInputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\" + loggedUser.id + ".ser");
            ObjectInputStream in = new ObjectInputStream (userFile);
            User user = (User) in.readObject ();
            in.close ();
            user.deposit += amount;
            userFile.close ();

            FileOutputStream newUserFile = new FileOutputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\" + user.id + ".ser");
            ObjectOutputStream out = new ObjectOutputStream (newUserFile);
            out.writeObject (user);
            out.close ();


            System.out.printf ("Deposit successful. You added %.2flv. to your account.%nTotal amount: %.2f%n", amount, user.deposit);

        } catch (Exception e) {
            System.out.println ("Deposit was unsuccessful");
            return false;
        }
        return true;

    }

}
