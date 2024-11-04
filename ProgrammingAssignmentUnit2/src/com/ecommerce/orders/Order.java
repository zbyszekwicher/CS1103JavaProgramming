package com.ecommerce.orders;

import com.ecommerce.Customer;
import com.ecommerce.Product;

import java.util.ArrayList;

/**@author Zbyszek
 * 
 */
public class Order {
	private int orderID;
	private static int orderCount = 0;
	private Customer customer;
	private ArrayList<Product> products = new ArrayList<>();
	private double orderTotal;
	private String orderStatus;
	
	public Order(Customer c, ArrayList<Product> p) {
		this.orderID = orderCount;
		orderCount++;
		this.customer = c;
		this.products = p;
		if (p.isEmpty()) {
			this.orderStatus = "Empty";
		}
		else {
			this.orderStatus = "Placed";
		}
	}
	
	public void updateOrderStatus(String s) {
		orderStatus = s;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public double getOrderTotal() {
		return orderTotal;
	}
	
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	
	public void printOrderSummary() {
		System.out.println(String.format("Order ID: %s", orderID));
		System.out.println(String.format("Customer name: %s", customer.getName()));
		System.out.println(String.format("Order status: %s", orderStatus));
		System.out.println("Products:");
		double totalPrice = 0;
		for (Product p : products) {
			p.printInfo();
			totalPrice += p.getPrice();
		}
		System.out.println(String.format("TOTAL: %s", totalPrice));
	}
}
