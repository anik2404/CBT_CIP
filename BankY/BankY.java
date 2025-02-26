// BankY.java - Main application class
import java.util.Scanner;

public class BankY {
    private static Scanner scanner = new Scanner(System.in);
    private static Bank bank = new Bank("BankY");
    
    public static void main(String[] args) {
        System.out.println("Welcome to BankY Banking System");
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    checkBalance();
                    break;
                case 6:
                    displayAllAccounts();
                    break;
                case 7:
                    running = false;
                    System.out.println("Thank you for using BankY. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            // Pause before showing menu again
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n==== BankY Menu ====");
        System.out.println("1. Create New Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Check Balance");
        System.out.println("6. Display All Accounts");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private static int getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return choice;
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }
    
    private static void createAccount() {
        System.out.println("\n==== Create New Account ====");
        
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter initial deposit amount: ");
        try {
            double initialDeposit = Double.parseDouble(scanner.nextLine());
            if (initialDeposit < 0) {
                System.out.println("Initial deposit cannot be negative.");
                return;
            }
            
            Account account = bank.createAccount(name, initialDeposit);
            System.out.println("Account created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Current Balance: $" + account.getBalance());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    private static void deposit() {
        System.out.println("\n==== Deposit Funds ====");
        
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = bank.findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.print("Enter deposit amount: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Deposit amount must be positive.");
                return;
            }
            
            account.deposit(amount);
            System.out.println("Deposit successful!");
            System.out.println("Current Balance: $" + account.getBalance());
            bank.saveData();
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    private static void withdraw() {
        System.out.println("\n==== Withdraw Funds ====");
        
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = bank.findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.print("Enter withdrawal amount: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Withdrawal amount must be positive.");
                return;
            }
            
            boolean success = account.withdraw(amount);
            if (success) {
                System.out.println("Withdrawal successful!");
                System.out.println("Current Balance: $" + account.getBalance());
                bank.saveData();
            } else {
                System.out.println("Insufficient funds. Withdrawal failed.");
                System.out.println("Current Balance: $" + account.getBalance());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    private static void transfer() {
        System.out.println("\n==== Transfer Funds ====");
        
        System.out.print("Enter source account number: ");
        String sourceAccountNumber = scanner.nextLine();
        
        Account sourceAccount = bank.findAccount(sourceAccountNumber);
        if (sourceAccount == null) {
            System.out.println("Source account not found.");
            return;
        }
        
        System.out.print("Enter destination account number: ");
        String destAccountNumber = scanner.nextLine();
        
        Account destAccount = bank.findAccount(destAccountNumber);
        if (destAccount == null) {
            System.out.println("Destination account not found.");
            return;
        }
        
        if (sourceAccountNumber.equals(destAccountNumber)) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }
        
        System.out.print("Enter transfer amount: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Transfer amount must be positive.");
                return;
            }
            
            boolean success = bank.transfer(sourceAccount, destAccount, amount);
            if (success) {
                System.out.println("Transfer successful!");
                System.out.println("Source Account Balance: $" + sourceAccount.getBalance());
                System.out.println("Destination Account Balance: $" + destAccount.getBalance());
            } else {
                System.out.println("Insufficient funds. Transfer failed.");
                System.out.println("Source Account Balance: $" + sourceAccount.getBalance());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    private static void checkBalance() {
        System.out.println("\n==== Check Balance ====");
        
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        
        Account account = bank.findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        
        System.out.println("Account Holder: " + account.getCustomerName());
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Current Balance: $" + account.getBalance());
        System.out.println("Account Created: " + account.getCreationDate());
    }
    
    private static void displayAllAccounts() {
        System.out.println("\n==== All Accounts ====");
        bank.displayAllAccounts();
    }
}
