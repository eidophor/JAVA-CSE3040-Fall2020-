package homework16;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


class Element implements Comparable<Element>{

	private String item;
	private double price;
	
	public Element(String item, double d) {
		this.item = item;
		this.price = d;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toString() {
		return item + " " + price;
	}
	//comparable 사용해서 정렬기준 정해주기
	public int compareTo(Element o) {
		if (this.price > o.getPrice()) return 1;
		if (this.price < o.getPrice()) return -1;
		return this.item.compareTo(o.getItem());
	}
}


class ElementReader {
	private static ArrayList<Element> list2 = new ArrayList<>();
	//지난번 과제꺼 부분적으로 따오기
	public static ArrayList<Element> readElements(String fileAdr) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileAdr));
			while (true) {
				String line = br.readLine(); // 한줄씩 처리하자
				if (line == null)
					break; 
				String arr[] = line.split(" "); // 공백기준으로 잘라서 문자열 배열, 텍스트에 엔터는 없다는 가정 
				list2.add(new Element(arr[0], Double.parseDouble(arr[1]))); 
				// 각줄에 두번째 열은 가격인데 String형 이므로 double형으로 변환
			}
			br.close();
			return list2;
		} catch (FileNotFoundException e) { //익셉션 상하위 순서고려해서 한번에
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}

public class Problem16 {
	public static void main(String[] args) {
		ArrayList<Element> list = ElementReader.readElements("input.txt"); 
		if(list == null) { 
			System.out.println("Input file not found.");
			return;
		}
		Collections.sort(list); 
		Iterator<Element> it = list.iterator(); 
		while(it.hasNext()) System.out.println(it.next());
	}
}
