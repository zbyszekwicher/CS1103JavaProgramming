import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class ChatServer2 {
	static private final int PORT = 2004;
	static private TreeMap<Integer, ConnectionThread> connections = new TreeMap<>();
	static private ArrayList<String> messages = new ArrayList<>();
	static private Scanner stdin = new Scanner(System.in);
	static private ListenerThread listenerThread;

	/** Main method starts a thread that listens for new connections.
	*	than it waits until the user wants to shut the server down. 
	*/
	public static void main(String[] args) {
		listenerThread = new ListenerThread();
		listenerThread.start();
		System.out.println("Server started. Type quit to exit.");
		
		while ( ! stdin.nextLine().equals("quit")) {
			System.out.println("unknown command");
		}
		ServerShut();
	}
	
	/**This method is synchronized, because other threads might also want to call it.
	 * It interrupts all the threads to close them,
	 * and closes all the connections.
	 */
	private static synchronized void ServerShut() {
		for (ConnectionThread c : connections.values()) {
			try {
				c.user.connection.close();
				c.writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			c.interrupt();
		}
		listenerThread.interrupt();
		System.out.println("Server is shut");
	}
	
	/**This method is also synchronized, because many instances of ConnectionThread
	 * might want to call it simultaneously.
	 * The purpose of this method is to send the given String to all the connected users.
	 * @param message
	 */
	private static synchronized void broadcastMessage(String message) {
		for (ConnectionThread c : connections.values()) {
			c.writer.println(message);
			c.writer.flush();
		}
	}
	
	/**This inner class is a Thread. It also represents a connected user.
	 * All the ConectionThread instances are stored in a connections map.
	 * This thread is responsible for getting the message from the user,
	 * and than calling the broadcastMessage() to send it to everyone.
	 * It assumes that always the first String that it gets is the user name.
	 */
	private static class ConnectionThread extends Thread {
		private User user;
		private int id;
		private PrintWriter writer;
		public ConnectionThread(User user, int id) throws IOException {
			this.user = user;
			this.id = id;
			writer = new PrintWriter(user.connection.getOutputStream());
		}
		
		@Override
		public void run() {
			try (BufferedReader incoming = new BufferedReader(
					new InputStreamReader(user.connection.getInputStream()) ) ) {				
				// The first incoming String is always a userName.
				user.userName = incoming.readLine();
				
				// Send all the messages that were already sent before the user connected.
				for (String m : messages) {
					writer.println(m);
				}
				writer.flush();
				broadcastMessage(user.userName + " joined the chat.");
				
				// Now listening for incoming messages.
				String message;
				while ( !(message = incoming.readLine()).equals("quit")
						&& !this.isInterrupted()) {
					broadcastMessage(user.userName + ": " + message);
					messages.add(user.userName + ": " + message);
				}
				incoming.close();
				writer.close();
				user.connection.close();
			} catch (IOException e) {
				System.out.println("Error occurred, closing connection with " + user.userName);
	            System.out.println(e.toString());
			}
			
			// Closing the resources
			synchronized (connections) {
				connections.remove(id);
			}
			writer.close();
		}
	}
	
	/**This thread listens for new users that want to connect.
	 * Once there is a new connection, it initializes and starts
	 * new ConnectionThread.
	 */
	private static class ListenerThread extends Thread {
		ServerSocket serverListener;
		@Override
		public void run() {
			int id = 0;
			Socket con;
			BufferedReader incoming = null;
			ConnectionThread cThread;
			try {
				serverListener = new ServerSocket(PORT);
			} catch (IOException e) {
	            System.out.println("Error occurred while initializing server listener.");
	            System.out.println(e.toString());
	    		ServerShut();
	    		return;
			}
			while (true) {				
				try {
					if (this.isInterrupted()) {
						if (incoming != null)
							incoming.close();
						serverListener.close();
						return;
					}
					con = serverListener.accept();
					User u = new User(con);
					cThread = new ConnectionThread(u, id);
					cThread.start();
					synchronized (connections) {
						connections.put(id, cThread);
					}
					id++;
				} catch (Exception e) {
		            System.out.println("Error occurred while opening connection.");
		            System.out.println(e.toString());
				}
			}
		}
	}
	
	/**This inner class stores information about the connected user.
	 * Also it has the reference to the actual connection Socket.
	 */
	private static class User {
		Socket connection;
		String userName;
		public User(Socket connection) {
			this.connection = connection;
			this.userName = "";
		}
	}
}
