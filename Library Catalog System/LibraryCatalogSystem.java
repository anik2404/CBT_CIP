import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LibraryCatalogSystem {
    private static List<Book> bookCollection = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Library Catalog System");
        
        // Add some sample books
        addSampleBooks();
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getUserChoice();
            
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    searchByTitle();
                    break;
                case 3:
                    searchByAuthor();
                    break;
                case 4:
                    listAllBooks();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n===== LIBRARY CATALOG MENU =====");
        System.out.println("1. Add a new book");
        System.out.println("2. Search book by title");
        System.out.println("3. Search book by author");
        System.out.println("4. List all books");
        System.out.println("5. Exit");
        System.out.print("Enter your choice (1-5): ");
    }

    private static int getUserChoice() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Clear the invalid input
            return -1; // Return an invalid choice
        } finally {
            scanner.nextLine(); // Clear the buffer
        }
    }

    private static void addBook() {
        System.out.println("\n----- Add a New Book -----");
        
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        
        System.out.print("Enter ISBN (or press Enter to skip): ");
        String isbn = scanner.nextLine();
        
        System.out.print("Enter publication year (or 0 to skip): ");
        int year;
        try {
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            year = 0;
        }
        
        Book newBook = new Book(title, author, isbn, year);
        bookCollection.add(newBook);
        
        System.out.println("\nBook added successfully!");
        System.out.println(newBook);
    }

    private static void searchByTitle() {
        System.out.println("\n----- Search Book by Title -----");
        System.out.print("Enter title to search: ");
        String searchTitle = scanner.nextLine().toLowerCase();
        
        List<Book> results = new ArrayList<>();
        for (Book book : bookCollection) {
            if (book.getTitle().toLowerCase().contains(searchTitle)) {
                results.add(book);
            }
        }
        
        displaySearchResults(results, "title");
    }

    private static void searchByAuthor() {
        System.out.println("\n----- Search Book by Author -----");
        System.out.print("Enter author name to search: ");
        String searchAuthor = scanner.nextLine().toLowerCase();
        
        List<Book> results = new ArrayList<>();
        for (Book book : bookCollection) {
            if (book.getAuthor().toLowerCase().contains(searchAuthor)) {
                results.add(book);
            }
        }
        
        displaySearchResults(results, "author");
    }

    private static void displaySearchResults(List<Book> results, String searchType) {
        if (results.isEmpty()) {
            System.out.println("No books found matching the " + searchType + ".");
        } else {
            System.out.println("\nFound " + results.size() + " book(s):");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    private static void listAllBooks() {
        System.out.println("\n----- All Books in the Library -----");
        
        if (bookCollection.isEmpty()) {
            System.out.println("The library catalog is empty.");
        } else {
            System.out.println("Total books: " + bookCollection.size());
            for (int i = 0; i < bookCollection.size(); i++) {
                System.out.println((i + 1) + ". " + bookCollection.get(i));
            }
        }
    }
    
    private static void addSampleBooks() {
        bookCollection.add(new Book("The Hobbit", "J.R.R. Tolkien", "978061800221", 1937));
        bookCollection.add(new Book("The Alchemist", "Paulo Coelho", "978062315007", 1988));
        bookCollection.add(new Book("War and Peace", "Herman Melville", "978014303999", 1869));
        bookCollection.add(new Book("Pride and Prejudice", "Jane Austen", "9780141439518", 1813));
        bookCollection.add(new Book("The Picture of Dorian Grey", "Oscar Wilde", "978014144246", 1890));
        bookCollection.add(new Book("The Raod", "Cormac McCarthy", "9780307387899", 2006));
    }
}

class Book {
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;

    public Book(String title, String author, String isbn, int publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(title).append("\" by ").append(author);
        
        if (publicationYear > 0) {
            sb.append(" (").append(publicationYear).append(")");
        }
        
        if (isbn != null && !isbn.isEmpty()) {
            sb.append(" [ISBN: ").append(isbn).append("]");
        }
        
        return sb.toString();
    }
}