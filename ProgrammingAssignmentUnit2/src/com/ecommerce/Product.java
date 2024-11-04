package com.ecommerce;

/**@author Zbyszek
 * 
 */
public class Product {
	private int productID;
	private static int productsCount = 0;
	private String name;
	private double price;
	
	public Product(String name, double price) {
		this.productID = productsCount;
		productsCount++;
		this.name = name;
		this.price = price;
	}
	
	public int getID() {
		return productID;
	}
	
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setName(String s) {
		this.name = s;
	}
	
	public void setPrice(double d) {
		this.price = d;
	}
	
	public void printInfo() {
		System.out.println(String.format("%s,\t%s$", name, price));
	}
}
