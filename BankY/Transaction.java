// Transaction.java - Class to track account transactions
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime timestamp;
    
    public Transaction(TransactionType type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s: $%.2f - %s", 
            getTimestamp(), type, amount, description);
    }
}