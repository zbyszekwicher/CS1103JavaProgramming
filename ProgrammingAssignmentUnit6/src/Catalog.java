import java.util.ArrayList;

public class Catalog<T extends LibraryItem> {
	ArrayList<T> items = new ArrayList<>();
	
	public void add(T item) {
		items.add(item);
		System.out.println(item.getTitle() + " successfuly added to the catalog");
	}
	 
	public void remove(T item) {
		if (items.remove(item)) {
			System.out.println(item.getTitle() + " successfuly removed from the catalog");
		} else {
			System.out.println("item not found");
		}
	}
	 
	public T getItem(String ID) {
	       for (T item : items) {
	           if (item.getID().equals(ID)) {
	               return item;
	           }
	       }
	       return null;
	   }
	 
	public void viewCatalog() {
		if (items.isEmpty()) {
			System.out.println("The catalog is empty");
		} else {
			for (T t : items) {
				System.out.println(t.getDetails());
			}
		}
	}
}
