import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 9876;
    private static HashSet<PrintWriter> writers = new HashSet<>();
    
    public static void main(String[] args) {
        System.out.println("Chat Server is running...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                
                // Create a new thread for each client
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Error in the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Handler for client connections
     */
    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                // Set up input and output streams
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                // Request username until a valid one is received
                while (true) {
                    out.println("SUBMIT_USERNAME");
                    username = in.readLine();
                    
                    if (username == null || username.isEmpty()) {
                        out.println("MESSAGE Username cannot be empty.");
                    } else {
                        // Add the client's writer to the set of all writers
                        synchronized (writers) {
                            writers.add(out);
                            break;
                        }
                    }
                }
                
                // Notify everyone about the new user
                broadcast("SERVER: " + username + " has joined the chat!");
                
                // Process messages from this client
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.toLowerCase().equals("/quit")) {
                        break;
                    }
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                // The client is leaving the chat
                if (username != null) {
                    broadcast("SERVER: " + username + " has left the chat.");
                }
                
                // Remove the client from the set of writers
                if (out != null) {
                    synchronized (writers) {
                        writers.remove(out);
                    }
                }
                
                // Close the socket
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
            }
        }
        
        /**
         * Sends a message to all clients
         */
        private void broadcast(String message) {
            synchronized (writers) {
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + message);
                }
            }
            System.out.println(message); // Display message in server console too
        }
    }
}