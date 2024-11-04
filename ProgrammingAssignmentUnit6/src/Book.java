
public class Book extends LibraryItem {
	public Book(String title, String author, String ID) {
		super(title, author, ID);
	}
	
	@Override
	public String getDetails() {
        return "[book] " + super.getDetails();
    }
}
