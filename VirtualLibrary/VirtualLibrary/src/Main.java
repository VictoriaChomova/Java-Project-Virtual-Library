import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        mainMenu ();


    }

    public static void mainMenu() {
        Scanner scanner = new Scanner (System.in);
        System.out.println ("Welcome to our virtual library! Choose an option /type the number in front/.");
        System.out.println ("1. Log in user\n2. Log in admin\n3. Register\n4. Exit");
        String choice = scanner.nextLine ();

        switch (choice) {
            case "1":
                User loggedUser = Administration.loginUser ();
                if (loggedUser != null) {
                    userMenu (loggedUser);
                }

                break;
            case "2":
                if (Administration.adminLogin ()) {
                    adminMenu ();
                }
                ;
                break;
            case "3":
                if (Administration.regUser ()) {
                    loggedUser = Administration.loginUser ();
                    if (loggedUser != null) {
                        userMenu (loggedUser);
                    }
                };
                break;
            case "4":
                System.exit (0);
        }

    }

    public static void userMenu(User loggedUser) {
        Scanner scanner = new Scanner (System.in);
        System.out.println ("Welcome to User menu:\n1. Rent a book\n2. Deposit\n3. Log out\n");

        String choice = scanner.nextLine ();

        switch (choice) {
            case "1":
                Administration.findBook (loggedUser);
                userMenu (loggedUser);
                break;
            case "2":
                User.makeDeposit (loggedUser);
                userMenu (loggedUser);
                break;
            case "3":
                mainMenu ();
                break;

        }
    }

    public static void adminMenu() {
        Scanner scanner = new Scanner (System.in);

        System.out.println ("Welcome to Admin menu:\n1. Add a book\n2. Remove book\n3.Log out\n");

        String choice = scanner.nextLine ();

        switch (choice) {
            case "1":
                Administration.addBook ();
                adminMenu ();
                break;
            case "2":
                Administration.removeBook ();
                adminMenu ();
                break;
            case "3":
                mainMenu ();

        }
    }
}
