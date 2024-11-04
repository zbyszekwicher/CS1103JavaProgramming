
public class LibraryItem {
	private String title;
    private String author;
    private String ID;

    public LibraryItem(String title, String author, String ID) {
        this.title = title;
        this.author = author;
        this.ID = ID;
    }

    public String getTitle() {
    	return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getID() {
        return ID;
    }

    public String getDetails() {
        return ID + " Title: " + title + ", Author: " + author;
    }
}
