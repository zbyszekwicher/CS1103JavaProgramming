package com.ecommerce;

import java.util.ArrayList;

import com.ecommerce.orders.Order;

/**@author Zbyszek
 * 
 */
public class Customer {
	private int customerID;
	private static int customerCount = 0;
	private String name;
	private ArrayList<Product> shoppingCart = new ArrayList<>();
	
	public Customer(String name) {
		this.customerID = customerCount;
		customerCount++;
		this.name = name;
	}
	
	public int getID() {
		return customerID;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Product> getShoppingCart() {
		return shoppingCart;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void addToCart(Product p) {
		this.shoppingCart.add(p);
	} 
	
	public boolean removeFromCart(Product p) {
		return shoppingCart.remove(p);
	}
	
	public double calculateTotalCost() {
		double totalCost = 0;
		for (Product p : shoppingCart) {
			totalCost += p.getPrice();
		}
		return totalCost;
	}
	
	public Order placeOrder() {
		return new Order(this, shoppingCart);
	}
}
