import java.io.*;
import java.util.Scanner;

import static java.lang.System.out;
import static java.lang.System.setOut;

public class Administration implements java.io.Serializable {

    public static boolean adminLogin() {

        try {
            File adminsFile = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\admins.txt");
            Scanner myReader = new Scanner (adminsFile);
            Scanner scanner = new Scanner (System.in);

            System.out.println ("Enter username:");
            String username = scanner.nextLine ();

            System.out.println ("Enter password");
            String password = scanner.nextLine ();

            boolean isFound = false;

            while (myReader.hasNext ()) {

                String[] adminToCheck = myReader.nextLine ().split (", ");
                String nameToCheck = adminToCheck[0];
                String passToCheck = adminToCheck[1];

                if (username.equals (nameToCheck)) {
                    isFound = true;
                    if (!password.equals (passToCheck)) {
                        System.out.println ("Password incorrect!");
                        return adminLogin ();
                    } else {
                        System.out.println ("Login successful!");
                        return true;
                    }

                }

            }

            System.out.println ("User not found!");
            return false;

        } catch (FileNotFoundException e) {
            System.out.println ("Something went wrong!");
            return adminLogin ();
        }
    }


    public static boolean regUser() {
        try {
            Scanner scanner = new Scanner (System.in);

            System.out.println ("Enter username:");
            String username = scanner.nextLine ();

            System.out.println ("Enter password:");
            String password = scanner.nextLine ();

            System.out.println ("Enter email:");
            String email = scanner.nextLine ();

            if (User.isPasswordValid (password)) {

                User newUser = new User (username, password, email);

//                FileWriter myWriter = new FileWriter("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\users.txt");
//                myWriter.append(newUser.id).append(", ").append(newUser.nickname).append("\n");
//                myWriter.close();

                Writer myWriter = new BufferedWriter (new FileWriter ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\users.txt", true));
                myWriter.append (newUser.id).append (", ").append (newUser.nickname).append ("\n");
                myWriter.close ();

                FileOutputStream userFile = new FileOutputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\" + newUser.id + ".ser");
                ObjectOutputStream out = new ObjectOutputStream (userFile);
                out.writeObject (newUser);
                out.close ();

                System.out.println ("Successful registration.");
                return true;
            } else {
                System.out.println ("Enter a valid password. Password must consist of 8 symbols or more and must contain at least one letter, one digit and one special symbol.");
                return regUser ();
            }

        } catch (IOException e) {
            System.out.println ("Something went wrong!");
            return regUser ();
        }
    }

    public static User loginUser() {
        User user = null;
        try {
            File usersFile = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\users.txt");

            Scanner myReader = new Scanner (usersFile);
            Scanner scanner = new Scanner (System.in);

            System.out.println ("Enter username:");
            String username = scanner.nextLine ();

            System.out.println ("Enter password");
            String password = scanner.nextLine ();
            boolean isFound = false;

            while (myReader.hasNext ()) {
                String[] userToCheck = myReader.nextLine ().split (", ");
                String idToCheck = userToCheck[0];
                String usernameToCheck = userToCheck[1];

                if (username.equals (usernameToCheck)) {
                    isFound = true;
                    FileInputStream userFile = new FileInputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\" + idToCheck + ".ser");
                    ObjectInputStream in = new ObjectInputStream (userFile);
                    User userToLog = (User) in.readObject ();
                    in.close ();

                    if (!idToCheck.equals (userToLog.id)) {
                        continue;
                    }

                    String passToCheck = userToLog.getPassword ();

                    if (!password.equals (passToCheck)) {
                        System.out.println ("Password incorrect!");
                        Administration.loginUser ();
                    } else {
                        System.out.println ("Login successful!");
                        return userToLog;
                    }
                }
            }

            if (!isFound) {
                System.out.println ("User not found!");
                Administration.loginUser ();
            }

            myReader.close ();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println ("Something went wrong");
            return Administration.loginUser ();

        }

        return null;
    }

    public static boolean addBook() {
        try {
            Scanner scanner = new Scanner (System.in);
            System.out.println ("Enter the book title.");
            String title = scanner.nextLine ();
            System.out.println ("Enter the book author.");
            String author = scanner.nextLine ();
            System.out.println ("Enter the book content.");
            String content = scanner.nextLine ();
            System.out.println ("Enter the ISBN code.");
            String isbn = scanner.nextLine ();

            Book newBook = new Book (title, author, content, isbn);

            Writer myWriter = new BufferedWriter (new FileWriter ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\catalogue.txt", true));
            myWriter.append (newBook.title).append (", ").append (newBook.isbn).append ("\n");
            myWriter.close ();

            FileOutputStream bookFile = new FileOutputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\" + newBook.isbn + ".ser");
            ObjectOutputStream out = new ObjectOutputStream (bookFile);
            out.writeObject (newBook);
            out.close ();

            System.out.println ("Book added successfully.");
            return true;

        } catch (Exception e) {
            System.out.println ("Something went wrong. Try again.");
            return addBook ();
        }
    }

    public static boolean removeBook() {
        try {


            File currentFile = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\catalogue.txt");
            File tempFile = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\catalogueTemp.txt");
            tempFile.createNewFile ();

            Scanner scanner = new Scanner (System.in);
            Scanner myReader = new Scanner (currentFile);

            System.out.println ("Enter the ISBN code of the book you would like to remove.");
            String isbn = scanner.nextLine ();
            boolean isFound = false;

            String bookToDelete = "";

            while ((myReader.hasNextLine ())) {
                String bookData = myReader.nextLine ();
                String[] bookDataArr = bookData.split (", ");
                String isbnToCheck = bookDataArr[1];

                if (isbnToCheck.equals (isbn)) {
                    isFound = true;
                    bookToDelete = bookData;
                    break;
                }
            }
            myReader.close ();

            BufferedReader reader = new BufferedReader (new FileReader (currentFile));
            BufferedWriter writer = new BufferedWriter (new FileWriter (tempFile));

            String currentLine;

            while ((currentLine = reader.readLine ()) != null) {

                if (currentLine.equals (bookToDelete)) {
                    continue;
                }
                writer.write (currentLine + "\n");

            }
            writer.close ();
            reader.close ();

            currentFile.delete();
            tempFile.renameTo(currentFile);

            if (isFound) {
                File myObj = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\" + isbn + ".ser");
                try {
                    myObj.delete ();
                    return true;
                } catch (Exception e) {
                    System.out.println ("Something went wrong");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
            return false;
        }
        return false;
    }

//
//            File newBooksFile = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\catalogueTemp.txt");
//            Scanner myReader = new Scanner (newBooksFile);
//            System.out.println ("Enter the ISBN code of the book you would like to remove.");
//            String isbn = scanner.nextLine ();
//
//            if (newBooksFile.createNewFile ()) {
//
//                Writer myWriter = new BufferedWriter (new FileWriter (newBooksFile, true));
//
//
//                while (myReader.hasNext ()) {
//                    String bookData = myReader.nextLine ();
//                    String[] bookDataArr = bookData.split (",");
//                    String titleToCheck = bookDataArr[0];
//                    String isbnToCheck = bookDataArr[1];
//
//                    if (!isbn.equals (isbnToCheck)) {
//                        myWriter.append (bookData).append ("\n");
//                    }
//                }
//                myWriter.close ();
//            }
//            File oldBooksFile = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\catalogue.txt");
//
//
//            if (newBooksFile.renameTo (oldBooksFile) && oldBooksFile.delete () ) {
//                File fileToDelete = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\" + isbn + ".ser");
//                if (fileToDelete.delete ()) {
//                    FileInputStream userFile = new FileInputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\" + isbn + ".ser");
//                    ObjectInputStream in = new ObjectInputStream (userFile);
//                    User user = (User) in.readObject ();
//                    System.out.println ("Book removed!");
//                    return true;
//                }
//            }
//
//        } catch (IOException | ClassNotFoundException e) {
//            System.out.println ("Something went wrong");
//            return removeBook ();
//        }
//        return removeBook ();


    public static boolean findBook(User loggedUser) {
        try {
            File booksFile = new File ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\catalogue.txt");
            Scanner scanner = new Scanner (System.in);
            Scanner myReader = new Scanner (booksFile);
            System.out.println ("Write the title of the book you are looking for.");
            String bookTitle = scanner.nextLine ();
            while (myReader.hasNext ()) {
                String[] bookDataArr = myReader.nextLine ().split (", ");
                String titleToCheck = bookDataArr[0];
                String isbnToCheck = bookDataArr[1];


                if (titleToCheck.equals (bookTitle)) {
                    FileInputStream bookFile = new FileInputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\books\\" + isbnToCheck + ".ser");
                    ObjectInputStream in = new ObjectInputStream (bookFile);
                    Book bookToRent = (Book) in.readObject ();
                    in.close ();

                    System.out.println ("Book found!");
                    System.out.println ("Title: " + bookToRent.title);
                    System.out.println ("Author :" + bookToRent.author);

                    System.out.println ("1. Rent the book.");
                    System.out.println ("2. New Search");
                    System.out.println ("3. Exit");

                    String command = scanner.nextLine ();

                    switch (command) {
                        case "1":
                            checkDepositToRentBook (loggedUser, bookToRent);
                            break;
                        case "2":
                            return findBook (loggedUser);
                        case "3":
                            System.exit (0);
                    }


                    return true;
                }
            }

            System.out.println ("The book was not found.");
            return false;

        } catch (IOException | ClassNotFoundException e) {
            System.out.println ("Something went wrong");
        }

        return false;
    }

    public static void checkDepositToRentBook(User loggedUser, Book bookToRent) {
        try {
            Scanner scanner = new Scanner (System.in);
            FileInputStream userFile = new FileInputStream ("C:\\Users\\Chomovi\\IdeaProjects\\VirtualLibrary\\src\\Users\\" + loggedUser.id + ".ser");
            ObjectInputStream userIn = new ObjectInputStream (userFile);
            User user = (User) userIn.readObject ();
            userIn.close ();
            if (user.deposit >= 2) {
                user.deposit -= 2;
                System.out.println ("Book rented successfully!");


                System.out.println ("Content:");
                System.out.println (bookToRent.content);

            } else {
                System.out.println ("Deposit is not enough to rent the book.");
            }
        } catch (Exception e) {
            System.out.println ("Something went wrong!");
            findBook (loggedUser);
        }
    }
}
