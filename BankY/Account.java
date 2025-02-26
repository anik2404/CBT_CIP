// Account.java - Account class representing a bank account
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private String customerName;
    private double balance;
    private LocalDateTime creationDate;
    private List<Transaction> transactionHistory;
    
    public Account(String accountNumber, String customerName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = initialDeposit;
        this.creationDate = LocalDateTime.now();
        this.transactionHistory = new ArrayList<>();
        
        // Record initial deposit if greater than zero
        if (initialDeposit > 0) {
            addTransaction(TransactionType.DEPOSIT, initialDeposit, "Initial deposit");
        }
    }
    
    public void deposit(double amount) {
        balance += amount;
        addTransaction(TransactionType.DEPOSIT, amount, "Deposit");
    }
    
    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            addTransaction(TransactionType.WITHDRAWAL, amount, "Withdrawal");
            return true;
        }
        return false;
    }
    
    private void addTransaction(TransactionType type, double amount, String description) {
        Transaction transaction = new Transaction(type, amount, description);
        transactionHistory.add(transaction);
    }
    
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return creationDate.format(formatter);
    }
    
    // Method for database storage
    public String toCsvString() {
        return accountNumber + "," + customerName + "," + balance + "," + getCreationDate();
    }
    
    // Method to create account from stored data
    public static Account fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 3) {
            String accNumber = parts[0];
            String name = parts[1];
            double bal = Double.parseDouble(parts[2]);
            
            Account account = new Account(accNumber, name, 0); // Create with zero initial balance
            account.balance = bal; // Set actual balance
            
            // Set creation date if available
            if (parts.length >= 4) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    account.creationDate = LocalDateTime.parse(parts[3], formatter);
                } catch (Exception e) {
                    // Use current time if parsing fails
                    account.creationDate = LocalDateTime.now();
                }
            }
            
            return account;
        }
        return null;
    }
}