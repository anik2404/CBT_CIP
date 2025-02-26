import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private List<Account> accounts;
    private int nextAccountNumber;
    private BankDatabase database;
    
    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<>();
        this.nextAccountNumber = 1000; // Start account numbers from 1000
        this.database = new BankDatabase("banky_data.txt");
        
        // Load existing accounts from database
        List<Account> loadedAccounts = database.loadAccounts();
        if (loadedAccounts != null && !loadedAccounts.isEmpty()) {
            accounts.addAll(loadedAccounts);
            
            // Find the highest account number to continue from
            for (Account account : accounts) {
                int accountNum = Integer.parseInt(account.getAccountNumber());
                if (accountNum >= nextAccountNumber) {
                    nextAccountNumber = accountNum + 1;
                }
            }
        }
    }
    
    public Account createAccount(String customerName, double initialDeposit) {
        String accountNumber = String.valueOf(nextAccountNumber++);
        Account newAccount = new Account(accountNumber, customerName, initialDeposit);
        accounts.add(newAccount);
        database.saveAccounts(accounts);
        return newAccount;
    }
    
    public Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    public boolean transfer(Account sourceAccount, Account destinationAccount, double amount) {
        if (sourceAccount.getBalance() >= amount) {
            sourceAccount.withdraw(amount);
            destinationAccount.deposit(amount);
            database.saveAccounts(accounts);
            return true;
        }
        return false;
    }
    
    public void saveData() {
        database.saveAccounts(accounts);
    }
    
    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found in the system.");
            return;
        }
        
        System.out.printf("%-15s %-25s %-15s%n", "Account Number", "Customer Name", "Balance");
        System.out.println("------------------------------------------------");
        
        for (Account account : accounts) {
            System.out.printf("%-15s %-25s $%-15.2f%n", 
                account.getAccountNumber(), 
                account.getCustomerName(), 
                account.getBalance());
        }
    }
    
    public List<Account> getAccounts() {
        return accounts;
    }
}