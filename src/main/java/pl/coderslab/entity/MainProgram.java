package pl.coderslab.entity;

import java.util.Arrays;
import java.util.Scanner;

public class MainProgram {


    private static int usersCount = 0;

    private static int[] activeIDs = new int[0];

    private static boolean running = true;

    public static void main(String[] args) {

        createDatabaseAndTable();
        showWelcomeMessage();
        showOptions();
        while (running) {
            refreshingData();
            String option = chooseOption();
            switch (option) {
                case "edit":
                    editUser();
                    question();
                    break;
                case "delete":
                    deleteUser();
                    question();
                    break;
                case "exit":
                    leave();
                    question();
                    break;
                case "showUsers":
                    showUsers();
                    question();
                    break;
                case "showOptions":
                    showOptions();
                    break;
                case "add":
                    addUser();
                    question();
                    break;
                default:
                    warnInvalidOption(option);
                    help();
                    question();
                    break;
            }
        }
        showExitMessage();
    }

    private static void addUser() {

        System.out.println("Podaj proszę nazwę nowego uźytkownika.");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("Podaj teraz email nowego uźytkownika.");
        String email = scanner.nextLine();
        System.out.println("Czas teraz podać hasło dla nowego użytkownika.");
        String password = scanner.nextLine();
        User user = new User(name, email, password);
        UserDao.create(user);

    } //done

    private static void refreshingData() {

        usersCount = UserDao.dataToPrintAll().length;
        activeIDs = setActiveIDs();

    } //done

    private static void showExitMessage() {

        System.out.println("Do zobaczenia następnym razem :)");
    } //done

    private static void warnInvalidOption(String option) {

        System.out.println("Nie wybrałeś odpowiedniej opcji.");
    } //done

    private static void leave() {

        running = false;
    } //done

    private static void deleteUser() {

        System.out.println("Podaj proszę numer ID użytkownika, którego chesz usunąć.");
        Scanner scanner = new Scanner(System.in);
        int id = 0;
        while (!scanner.hasNextInt()) {
            System.out.println("Podaj proszę liczbę.");
            scanner.next();
        }
        int isInt = scanner.nextInt();

        for (int i = 0; i < activeIDs.length; i++) {

            if (activeIDs[i] == isInt) {
                id = isInt;
            }
        }
        if (id != 0) {
            UserDao.deleteUser(id);
        } else {
            System.out.println("Nie ma w bazie użytkownika o podanym ID. Sprawdź listę dostępnych użytkowników i spróbuj jeszcze raz.");
        }
    } // done

    private static void editUser() {

        System.out.println("Podaj proszę numer ID użytkownika, którego chesz zaktualizować.");
        Scanner scanner = new Scanner(System.in);
        int id = 0;

        while (!scanner.hasNextInt()) {
            System.out.println("Podaj proszę liczbę.");
            scanner.next();
        }
        int isInt = scanner.nextInt();
        for (int i = 0; i < activeIDs.length; i++) {

            if (activeIDs[i] == isInt) {

                id = isInt;
            }
        }

        if (id != 0) {

            System.out.println("Edytujesz użytkownika o danych :");
            System.out.println(UserDao.printUser(id).toString());
            Scanner scanner1 = new Scanner(System.in);
            System.out.println("Podaj nową nazwę użytkownika.");
            String newName= scanner1.nextLine();
            System.out.println("Podaj nowy email.");
            String newEmail = scanner1.nextLine();
            System.out.println("Podaj nowe hasło.");
            String newPassword = scanner1.nextLine();
            User user = new User();
            user.setUserName(newName);
            user.setEmail(newEmail);
            user.setPassword(newPassword);
            UserDao.updateUser(user, id);

        } else {
            System.out.println("Nie ma w bazie użytkownika o podanym ID. Sprawdź listę dostępnych użytkowników i spróbuj jeszcze raz.");
        }
    }//done

    private static void showOptions() {

        System.out.println("Dostępne akcje:");
        System.out.println("add - dodaj użytkownika");
        System.out.println("edit - zaktualizuj użytkownika");
        System.out.println("delete - usuń użytkownika");
        System.out.println("showUsers - pokaż wszystkich użytkowników w bazie ");
        System.out.println("exit - zakończ działanie programu");
        System.out.println("Jaki jest Twój wybór?");
    } // done

    private static void showWelcomeMessage() {

        System.out.println("Witaj w programie zarządającym bazą danych użytkowników.");
        System.out.println("Wersja 1.0");
    } //done

    private static String chooseOption() {

        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    } //done

    private static void showUsers() {

        System.out.println("Aktualnie jest " + usersCount + " aktywnych użytkowników.");
        System.out.println("Lista: ");
        System.out.println(Arrays.toString(UserDao.dataToPrintAll()));
    } //done

    private static void help() {

        System.out.println("Wpisz \"showOptions\" ,aby wyświetlić dostępne opcje programu.");
    } //done

    private static void createDatabaseAndTable() {

        UserDao.createRequiredData();

    } //done

    private static void question() {

        System.out.println("Co robimy dalej?");
    } //done

    private static int[] setActiveIDs() {

        int[] ids = new int[0];

        for (int i = 0; i < UserDao.dataToPrintAll().length; i++) {
            ids = Arrays.copyOf(ids, ids.length + 1);
            ids[i] = UserDao.dataToPrintAll()[i].getId();
        }
        return ids;
    } //done
}