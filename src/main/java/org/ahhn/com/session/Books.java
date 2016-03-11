package org.ahhn.com.session;

import java.sql.Blob;
import java.util.Date;

/**
 * Created by XJX on 2016/3/6.
 */
public class Books {
	private String isbn;
	private String bookName;
	private int price;
	private Date date;
	private String namePrice;//派生属性

	//大文本
	private String content;

	//二进制数据
	private Blob image;

	public Books() {
	}

	public Books(String bookName, int price) {
		this.bookName = bookName;
		this.price = price;
	}

	public Books(String isbn, String bookName, int price, Date date) {
		this.isbn = isbn;
		this.bookName = bookName;
		this.price = price;
		this.date = date;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNamePrice() {
		return namePrice;
	}

	public void setNamePrice(String namePrice) {
		this.namePrice = namePrice;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
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
