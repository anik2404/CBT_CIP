// BankDatabase.java - Simple file-based database
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BankDatabase {
    private String filename;
    
    public BankDatabase(String filename) {
        this.filename = filename;
    }
    
    public void saveAccounts(List<Account> accounts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Account account : accounts) {
                writer.println(account.toCsvString());
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts to file: " + e.getMessage());
        }
    }
    
    public List<Account> loadAccounts() {
        List<Account> loadedAccounts = new ArrayList<>();
        File file = new File(filename);
        
        if (!file.exists()) {
            return loadedAccounts; // Return empty list if file doesn't exist yet
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Account account = Account.fromCsvString(line);
                if (account != null) {
                    loadedAccounts.add(account);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts from file: " + e.getMessage());
        }
        
        return loadedAccounts;
    }
}