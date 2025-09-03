package BankManage;
import java.sql.*;
import java.util.Scanner;

public class BankManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String USER = "root";   // your MySQL username
    private static final String PASSWORD = "root"; // your MySQL password

    private Connection conn;
    private Scanner sc;

    public BankManagementSystem() throws Exception {
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        sc = new Scanner(System.in);
    }

    // Create new account
    public void createAccount() throws Exception {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        System.out.print("Enter initial balance: ");
        double balance = sc.nextDouble();
        sc.nextLine(); // consume newline

        String sql = "INSERT INTO accounts (name, balance) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setDouble(2, balance);
        ps.executeUpdate();

        System.out.println("Account created successfully!");
    }

    // Deposit money
    public void deposit() throws Exception {
        System.out.print("Enter account number: ");
        int accNo = sc.nextInt();
        System.out.print("Enter deposit amount: ");
        double amount = sc.nextDouble();

        String sql = "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, amount);
        ps.setInt(2, accNo);
        int rows = ps.executeUpdate();

        if (rows > 0) System.out.println("Deposit successful!");
        else System.out.println("Account not found.");
    }

    // Withdraw money
    public void withdraw() throws Exception {
        System.out.print("Enter account number: ");
        int accNo = sc.nextInt();
        System.out.print("Enter withdrawal amount: ");
        double amount = sc.nextDouble();

        // Check balance first
        String checkSql = "SELECT balance FROM accounts WHERE acc_no = ?";
        PreparedStatement checkPs = conn.prepareStatement(checkSql);
        checkPs.setInt(1, accNo);
        ResultSet rs = checkPs.executeQuery();

        if (rs.next()) {
            double balance = rs.getDouble("balance");
            if (balance >= amount) {
                String sql = "UPDATE accounts SET balance = balance - ? WHERE acc_no = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, amount);
                ps.setInt(2, accNo);
                ps.executeUpdate();
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Account not found.");
        }
    }

    // Check balance
    public void checkBalance() throws Exception {
        System.out.print("Enter account number: ");
        int accNo = sc.nextInt();

        String sql = "SELECT name, balance FROM accounts WHERE acc_no = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accNo);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Account Holder: " + rs.getString("name"));
            System.out.println("Balance: " + rs.getDouble("balance"));
        } else {
            System.out.println("Account not found.");
        }
    }

    // Menu
    public void start() throws Exception {
        while (true) {
            System.out.println("\n--- Bank Management System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> checkBalance();
                case 5 -> {
                    System.out.println("Thank you! Exiting...");
                    conn.close();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            BankManagementSystem app = new BankManagementSystem();
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
