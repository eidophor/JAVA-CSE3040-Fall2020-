package homework20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class BookInfo implements Comparable<BookInfo>{

	private String title, author, price;
	private int idx;
	
	public BookInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public BookInfo(String title, String author, int idx, String price) {
		super();
		this.title = title;
		this.author = author;
		this.idx = idx;
		this.price = price;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getPrice() {
		return price;
	}
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public int compareTo(BookInfo arg0) {
		// TODO Auto-generated method stub
		if (this.idx > arg0.getIdx()) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public String toString() {
		return "#" + idx + " " + title + ", " + author + ", " + price;
	}
}

class BookReader {
	//readBooksJsoup method
	public static ArrayList<BookInfo> readBooksJsoup(String address){
		
		ArrayList<BookInfo> list = new ArrayList<BookInfo>();
		Document doc = null;
		
		try {
			doc = Jsoup.connect(address).get();
			
			int bookIdx = 1;
			Elements row = doc.getElementsByClass("topX-row");
			
			String title = "", author = "", price = "";
			for(int i = 0; i < row.size(); i++) {
				
				Elements e_title = row.eq(i).select(".product-info-title").select("a");
				title = e_title.eq(0).text();
				
				Elements e_author = row.eq(i).select(".product-shelf-author").select("a");
				author = e_author.eq(0).text();
				
				Elements e_price = row.eq(i).select(".current").select("a");
				price = e_price.eq(0).text();
				
				BookInfo bi = new BookInfo(title, author, bookIdx, price);
				list.add(bi);
				bookIdx++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}

public class Problem20 {
	public static void main(String[] args) {
		ArrayList<BookInfo> books;
		books = BookReader.readBooksJsoup("https://www.barnesandnoble.com/b/books/_/N-1fZ29Z8q8");
		Collections.sort(books);
		for(int i=0; i<books.size(); i++) {
			BookInfo book = books.get(i);
			System.out.println(book);
		}
	}
}
