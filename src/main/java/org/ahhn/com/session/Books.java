package org.ahhn.com.session;

/**
 * Created by XJX on 2016/3/6.
 */
public class Books {
	private String isbn;
	private String bookName;
	private int price;

	public Books(String bookName, int price) {
		this.bookName = bookName;
		this.price = price;
	}

	public Books() {

	}

	public Books(String isbn, String bookName, int price) {
		this.isbn = isbn;
		this.bookName = bookName;
		this.price = price;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Books{" +
				"isbn='" + isbn + '\'' +
				", bookName='" + bookName + '\'' +
				", price=" + price +
				'}';
	}
}
