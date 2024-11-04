import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	static private final int PORT = 2004;
	static private Scanner stdin = new Scanner(System.in);
	static private Socket connection;
	private static PrintWriter outgoingPrinter;

	/** Main method sets up the connection, starts the thread that listens for incoming messages,
	 * and then waits for user to type messages. Once the user enters "quit", the connection is closing.
	 */
	public static void main(String[] args) {
		System.out.println("CHAT CLIENT");
		// Get server address
		System.out.print("Enter computer name or IP address: ");
        String hostName = stdin.nextLine();
        
        // Get user nickname
        System.out.print("Enter the user name: ");
        String userName = stdin.nextLine();
        
        // Create a connection to the server
        try {
        	connection = new Socket( hostName, PORT );
        	outgoingPrinter = new PrintWriter( connection.getOutputStream() );
        	sendMessage(userName);
        	System.out.println("Connected successfuly to the server");
		} catch (Exception e) {
			System.out.println("Error occurred while opening connection.");
            System.out.println(e.toString());
            return;
		}
        
        // Start a listening thread
        GetMessagesThread gmt = new GetMessagesThread();
        gmt.start();
        
        System.out.println("Type messages you want to send or quit to exit.\n");
        String input = "";
        while ( ! input.equals("quit")) {
			input = stdin.nextLine();
			if ( ! input.isEmpty()) {
				sendMessage(input);
			}
		}
        sendMessage(userName + " left the chat.");
        sendMessage("quit");
		
        // close the connection
        gmt.interrupt();
		try {
			gmt.join();
			connection.close();
			outgoingPrinter.close();
		} catch (IOException | InterruptedException e) {
			System.out.println("Error occured while closing the connection");
            System.out.println(e.toString());
		}
	}
	
	private static void sendMessage(String message) {
		outgoingPrinter.println(message);
		outgoingPrinter.flush();
	}
	
	/** This thread listens for incoming messages, and displays them in the console.
	 */
	private static class GetMessagesThread extends Thread {
		@Override
		public void run() {
			try (BufferedReader incoming = new BufferedReader( 
					new InputStreamReader(connection.getInputStream()) ) ) {
				String incomingMessage;
				while ( !this.isInterrupted()
						&& (incomingMessage = incoming.readLine()) != null ){
		            System.out.println(" > " + incomingMessage);
				}
			} catch (Exception e) {
				System.out.println("Error occurred while receiving message.");
	            System.out.println(e.toString());
			}
		
		}
	}
}
