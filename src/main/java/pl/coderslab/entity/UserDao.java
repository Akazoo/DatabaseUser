package pl.coderslab.entity;

import java.sql.*;
import java.util.Arrays;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String DATA_USER_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE  FROM users WHERE id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM users";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users(" +
            "    id INT(11) NOT NULL auto_increment ," +
            "    email VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL UNIQUE ," +
            "    username VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL ," +
            "    password VARCHAR(60) COLLATE utf8mb4_unicode_ci NOT NULL," +
            "    PRIMARY KEY (id)" +
            ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    private static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS workshop2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

    public static void create(User u) {

        try (Connection conn = DBUtils.connect("workshop2")) {
            String sql = CREATE_USER_QUERY;
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, u.getUserName());
            pstmt.setString(2, u.getEmail());
            pstmt.setString(3, hashPassword(u.getPassword()));
            pstmt.executeUpdate();
            System.out.println("Użytkownik został stworzony.");
            ResultSet rs = pstmt.getGeneratedKeys(); // Poznanie id dla nowo utworzonego rekordu(usera)
            if (rs.next()) {
                long id = rs.getLong(1);
                System.out.println("ID nowo dodanego użytkownika: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Wystąpił bład podczas dodawania użytkownika. W pierszej kolejnosci sprawdz czy email jest unikalny.");
        }
    } //done

    public static void printAllTRY() {

        try (Connection conn = DBUtils.connect("workshop2")) {
            String sql = SELECT_ALL_QUERY;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] columnNames = {"id", "email", "username", "password"};
                for (int i = 0; i < columnNames.length; i++) {
                    if (i < columnNames.length - 1) {
                        System.out.print(rs.getString(i + 1) + ", ");
                    } else {
                        System.out.print(rs.getString(i + 1) + '\n');
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie udało się połączyć z bazą danych.");
        }
    } //dziala ale lepiej to nizej bo funkcja nie nic nie wyswietla

    public static String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static void printOne(int id) {

        try (Connection conn = DBUtils.connect("workshop2")) {
            String sql = DATA_USER_QUERY;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] columnNames = {"id", "email", "username", "password"};
                for (String param : columnNames) {
                    System.out.println(rs.getString(param));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie udało się połączyć z bazą danych.");
        }
    } // same as printALLTRY

    public static void deleteUser(int id) {

        try (Connection conn = DBUtils.connect("workshop2")) {
            String sql = DELETE_USER_QUERY;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Użytkownik został usunięty.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie udało się usunąć użytkownika.");
        }
    } //done

    public static void updateUser(User user, int id) {

        try (Connection conn = DBUtils.connect("workshop2")) {
            String sql = UPDATE_USER_QUERY;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Udało się zaktualizować użytkownika. Jego nowe dane :");
            System.out.println(printUser(id));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Nie udało się zaktualizować użytkownika.");
        }
    } //done

    public static User[] dataToPrintAll() {

        User[] users = new User[0];

        try (Connection connection = DBUtils.connect("workshop2")) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
                users = Arrays.copyOf(users, users.length + 1);
                users[users.length - 1] = user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    } //done

    public static void createRequiredData() {

        try (Connection connection = DBUtils.connect()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_DATABASE);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Nie udało się stworzyć bazy danych.");
        }

        try (Connection connection = DBUtils.connect("workshop2")) {
            PreparedStatement statement = connection.prepareStatement(CREATE_TABLE);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Nie udało się stworzyć tabeli uxytkowników.");
        }

    } //done

    public static User printUser(int id) {

        User user = new User();

        try (Connection connection = DBUtils.connect("workshop2")) {
            PreparedStatement statement = connection.prepareStatement(DATA_USER_QUERY);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Pojawił się nieoczekiwany problem z wczytaniem danych uźytkownika.");
        }
        return user;
    } //done

}