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
	//comparable ����ؼ� ���ı��� �����ֱ�
	public int compareTo(Element o) {
		if (this.price > o.getPrice()) return 1;
		if (this.price < o.getPrice()) return -1;
		return this.item.compareTo(o.getItem());
	}
}


class ElementReader {
	private static ArrayList<Element> list2 = new ArrayList<>();
	//������ ������ �κ������� ������
	public static ArrayList<Element> readElements(String fileAdr) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileAdr));
			while (true) {
				String line = br.readLine(); // ���پ� ó������
				if (line == null)
					break; 
				String arr[] = line.split(" "); // ����������� �߶� ���ڿ� �迭, �ؽ�Ʈ�� ���ʹ� ���ٴ� ���� 
				list2.add(new Element(arr[0], Double.parseDouble(arr[1]))); 
				// ���ٿ� �ι�° ���� �����ε� String�� �̹Ƿ� double������ ��ȯ
			}
			br.close();
			return list2;
		} catch (FileNotFoundException e) { //�ͼ��� ������ ��������ؼ� �ѹ���
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
