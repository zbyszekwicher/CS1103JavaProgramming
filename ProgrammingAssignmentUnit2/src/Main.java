import com.ecommerce.*;
import com.ecommerce.orders.Order;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
/**@author Zbyszek
 * 
 */
public class Main {
	private static Scanner stdin = new Scanner(System.in);
	private static ArrayList<Product> products = new ArrayList<>();
	private static ArrayList<Customer> customers= new ArrayList<>();
	private static ArrayList<Order> orders= new ArrayList<>();
	
	public static void main(String[] args) {
		while (true) {
			System.out.println("\nECOMMERCE SYSTEM MENU");
			System.out.println("1 -> create a new Product object");
			System.out.println("2 -> create a new Customer object");
			System.out.println("3 -> browse products and add them to the shopping cart");
			System.out.println("4 -> manage shopping cart and place orders");
			System.out.println("5 -> display customer information");
			System.out.println("6 -> display product information");
			System.out.println("7 -> display order information");
			System.out.println("0 -> exit program");
			String input = stdin.nextLine();
			manageInput(input);
		}
	}
	
	private static void manageInput(String input) {
		switch (input) {
		case "0" -> System.exit(0);
		case "1" -> newProduct();
		case "2" -> newCustomer();
		case "3" -> browseProducts();
		case "4" -> placeOrders();
		case "5" -> displayCustomerInfo();
		case "6" -> displayProductInfo();
		case "7" -> displayOrderInfo();
		default -> {
			System.out.println("enter a correct number");
			input = stdin.nextLine();
			manageInput(input);
			}
		}
	}
	
	private static void newProduct() {
		System.out.println("ADDING NEW PRODUCT");
		System.out.println("\nEnter the product's name");
		String name = stdin.nextLine();
		System.out.println("Enter the product's price");
		double price;
		try {
			price = stdin.nextDouble();
		} catch (InputMismatchException e) {
			System.out.println("Invalid price format, try again\n");
			return;
		}
		products.add(new Product(name, price));
		System.out.println(String.format("%s, %s$ added successfuly", name, price));
	}
	
	private static void newCustomer() {
		System.out.println("ADDING NEW CUSTOMER");
		System.out.println("\nEnter the customer's name");
		String name = stdin.nextLine();
		customers.add(new Customer(name));
		System.out.println(String.format("Customer %s added successfuly", name));
	}
	
	private static void browseProducts() {
		System.out.println("ADDING PRODUCT TO THE SHOPPING CART");
		if (displayCustomerInfo() == 0) {
			System.out.print("Enter customer's name: ");
			String input = stdin.nextLine();
			Customer customer = customers.get(0);
			for (Customer c : customers) {
				if (input.equals(c.getName())) {
					customer = c;
					break;
				}
			}
			if (!customer.getName().equals(input)) {
				System.out.print("There is no such customer found as");
				System.out.println(input);
			}
				
			//Now we can browse products (the customer is set)
			if (displayProductInfo() == 0) {
				System.out.print("Enter name of the product you want to add to the shopping cart: ");
				input = stdin.nextLine();
				Product product = products.get(0);
				for (Product p : products) {
					if (input.equals(p.getName())) {
						product = p;
						break;
					}
				}
				if (!product.getName().equals(input)) {
					System.out.print("There is no such product found as");
					System.out.println(input);
					return;
				}
				
				customer.addToCart(product);
				System.out.println(String.format("%s has been successfuly added to the shopping cart of %s", product.getName(), customer.getName()));
			}
		}
	}

	private static int displayCustomerInfo() {
		System.out.println("CUSTOMERS");
		if (customers.isEmpty()) {
			System.out.println("There are no customers yet\n");
			return 1;
		}
		for (Customer c : customers) {
			System.out.println(c.getName());
			if (!c.getShoppingCart().isEmpty()) {
				System.out.println("Shopping cart:");
				for (Product p : c.getShoppingCart()) {
					System.out.print("\t");
					p.printInfo();
				}
			}
		}
		return 0;
	}
	
	private static int displayProductInfo() {
		System.out.println("PRODUCTS");
		if (products.isEmpty()) {
			System.out.println("There are no products yet\n");
			return 1;
		}
		for (Product p : products) {
			p.printInfo();
		}
		return 0;
	}

	private static int displayOrderInfo() {
		System.out.println("ORDERS");
		if (orders.isEmpty()) {
			System.out.println("There are no orders yet\n");
			return 1;
		}
		for (Order o : orders) {
			o.printOrderSummary();
			System.out.println();
		}
		return 0;
	}
	
	private static void placeOrders() {
		System.out.println("PLACING AN ORDER");
		if (displayCustomerInfo() == 0) {
			System.out.print("Enter customer's name: ");
			String input = stdin.nextLine();
			Customer customer = customers.get(0);
			for (Customer c : customers) {
				if (input.equals(c.getName())) {
					customer = c;
					break;
				}
			}
			if (!customer.getName().equals(input)) {
				System.out.print("There is no such customer found as");
				System.out.println(input);
			}
			
			orders.add(customer.placeOrder());
			System.out.println("order has been placed");
		}
	}
}
