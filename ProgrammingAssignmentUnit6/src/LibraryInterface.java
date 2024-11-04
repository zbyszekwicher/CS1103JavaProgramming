import java.util.Scanner;

public class LibraryInterface {
	private static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) {
		Catalog<Book> bookCatalog = new Catalog<>();
		while (true) {
			System.out.println("\nLIBRARY INTERFACE");
			System.out.println("1 -> view the current catalog");
			System.out.println("2 -> add new item");
			System.out.println("3 -> remove an item");
			System.out.println("0 -> quit");
			String input = stdin.nextLine();
			manageInput(input, bookCatalog);
		}
	}
	
	private static <T extends LibraryItem> void manageInput(String input, Catalog<T> c) {
		switch (input) {
		case "0" -> System.exit(0);
		case "1" -> viewCatalog(c);
		case "2" -> addItem(c);
		case "3" -> removeItem(c);
		default -> {
			System.out.println("enter a correct number");
			input = stdin.nextLine();
			manageInput(input, c);
			}
		}
	}

	private static <T extends LibraryItem> void viewCatalog(Catalog<T> c) {
		System.out.println("\nCatalog view");
		c.viewCatalog();
	}
	
	private static <T extends LibraryItem> void addItem(Catalog<T> c) {
		System.out.println("\nAdding new item");
		System.out.print("Title: ");
		String title = stdin.nextLine();
		System.out.print("Author: ");
		String author = stdin.nextLine();
		System.out.print("ID: ");
		String ID = stdin.nextLine();
		
		LibraryItem item = new Book(title, author, ID);
		c.add((T) item);	
	}

	private static <T extends LibraryItem> void removeItem(Catalog<T> c) {
		System.out.println("\nRemoving an item");
		System.out.print("Give item ID: ");
		String ID = stdin.nextLine();
		c.remove(c.getItem(ID));
	}
}
