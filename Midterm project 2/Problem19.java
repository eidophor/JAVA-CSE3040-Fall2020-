package homework19;

import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

class BookInfo implements Comparable<BookInfo>{

	private String title, author,  price;
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
	//readBooks method
	public static ArrayList<BookInfo> readBooks(String address){
		
		ArrayList<BookInfo> list = new ArrayList<BookInfo>();
		
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader input = null;
		String line = "";
		
		try {
			URL url = new URL(address);
			input = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((line=input.readLine()) != null) {
				if(line.trim().length() > 0) 
					lines.add(line);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int idx = 0;
		int bookIdx = 1;
		String title = "", author = "", price = "";
		for(String s : lines) {
			if(s.contains("product-info-title")) {
				String tag = lines.get(idx + 1);
				int start = tag.indexOf(">");
				int end = tag.indexOf("</a>");
				title = tag.substring(start + 1, end);
			}
			if(s.contains("product-shelf-author")) {
				String tag = lines.get(idx);
				int start = tag.indexOf(">", 50);
				int end = tag.indexOf("</a>");
				author = tag.substring(start + 1, end);
				if(author.indexOf(",") > 0) {
					author = author.substring(0, author.indexOf(","));
				}
			}
			if(s.contains(" current link")) {
				String tag = lines.get(idx);
				int start = tag.indexOf(">", 50);
				int end = tag.indexOf("</a>");
				price = tag.substring(start + 1, end);
				
				BookInfo bi = new BookInfo(title, author, bookIdx, price);
				list.add(bi);
				bookIdx++;
			}
			idx++;
		}
		return list;
	}
}

public class Problem19 {
	public static void main(String[] args) {
		ArrayList<BookInfo> books;
		books = BookReader.readBooks("https://www.barnesandnoble.com/b/books/_/N-1fz29z8q8");
		Collections.sort(books);
		for(int i=0; i<books.size(); i++) {
			BookInfo book = books.get(i);
			System.out.println(book);
		}
	}
}
