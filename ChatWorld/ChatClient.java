import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9876;
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the Chat Application!");
        System.out.println("Type /quit to exit the chat.");
        
        try {
            // Connect to server
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server.");
            
            // Set up input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
            // Start a thread to receive messages from the server
            new Thread(new MessageReceiver(in)).start();
            
            // Process user input
            String username = null;
            String input;
            
            while (true) {
                if (username == null) {
                    System.out.print("Enter your username: ");
                    username = scanner.nextLine().trim();
                    out.println(username);
                } else {
                    System.out.print("> ");
                    input = scanner.nextLine();
                    
                    // Send message to server
                    out.println(input);
                    
                    // Check if the user wants to quit
                    if (input.toLowerCase().equals("/quit")) {
                        break;
                    }
                }
            }
            
            // Clean up resources
            socket.close();
            scanner.close();
            System.out.println("Disconnected from the chat server.");
            
        } catch (UnknownHostException e) {
            System.err.println("Error: Server not found!");
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }
    
    /**
     * Thread to handle incoming messages from the server
     */
    private static class MessageReceiver implements Runnable {
        private BufferedReader in;
        
        public MessageReceiver(BufferedReader in) {
            this.in = in;
        }
        
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("SUBMIT_USERNAME")) {
                        // Server is requesting username, handled in main thread
                        continue;
                    } else if (message.startsWith("MESSAGE ")) {
                        // Display the chat message
                        System.out.println(message.substring(8));
                        System.out.print("> "); // Re-print the prompt
                    }
                }
            } catch (IOException e) {
                System.err.println("Error receiving message: " + e.getMessage());
            }
        }
    }
}