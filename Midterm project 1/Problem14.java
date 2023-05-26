package homework14;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


class Fruit {

	private String item;
	private double price;
	
	public Fruit(String item, double d) {
		this.item = item;
		this.price = d;
	}
	
	public double price() {
		return price;
	}
	
	public String item() {
		return item;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
}


class FruitBox<T extends Fruit> {
    private ArrayList<T> list = new ArrayList<T>();

	public void add(T fruit) {
		list.add(fruit);
		System.out.println(fruit.item() +" "+fruit.price());
	}
	
	public int getNumItems() {
		return list.size();
	}
	
	public double getMaxPrice() {
		double max = -1; //���� max���� -�� ��� ���� item�� ���� max������ ũ�� max���� ��ü
		if(list.size()>0) {
			for(T b : list) {
				if(b.price()>max) {
					max = b.price();
				}
			}
		}
		return max;
	}
	
	public double getMinPrice() {
		double min = 0; 
		if(list.size()>0)
		{
			min = list.get(0).price(); // min���� �� �տ� �������� ��� ���������� �� ������ ��ü
			for(T b : list)
			{
				if(b.price()<min)
				{
					min = b.price();
				}
			}
		}
		return min;
	}
	
	// ���ݰ� �������
	public String getMaxItem() {
		double max = -1;
		String item = "";
		if(list.size()>0) {
			for(T b : list) {
				if(b.price()>max) {
					max = b.price();
					item = b.item();
				}
			}
		}
		return item;
	}
	
	public String getMinItem() {
		double min = 0;
		String item = "";
		
		if(list.size()>0) {
			min = list.get(0).price();
			
			for(T b : list) {
				if(b.price()<min) {
					min = b.price();
					item = b.item();
				}
			}
		}
		return item;
}
	
	// ��ü ������ size�� ������
	public double getAvgPrice() {
		double sum = 0;
		
		for(T b : list) {
			sum = sum+b.price();
		}
		return sum/list.size();
	}
	
}


class ItemReader {

	public static boolean fileToBox(String fileAdr, FruitBox<Fruit> f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileAdr));
			while (true) {
				String line = br.readLine(); // ���پ� ó������
				if (line == null)
					break; 
				String arr[] = line.split(" "); // ����������� �߶� ���ڿ� �迭, �ؽ�Ʈ�� ���ʹ� ���ٴ� ���� 
				f.add(new Fruit(arr[0], Double.parseDouble(arr[1]))); 
				// ���ٿ� �ι��� ���� �����ε� String�� �̹Ƿ� double������ ��ȯ
			}
			br.close();
			return true;
		} catch (FileNotFoundException e) { //�ͼ��� ������ ��������ؼ� �ѹ���
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}


public class Problem14 {
		public static void main(String[] args) {
			FruitBox<Fruit> box = new FruitBox<>();
			boolean rv = ItemReader.fileToBox("input_prob14.txt", box);
			if (rv == false) return;
			box.add(new Fruit("orange", 9.99));
			System.out.println("----------------");
			System.out.println("    Summary");
			System.out.println("----------------");
			System.out.println("number of items: " + box.getNumItems());
			System.out.println("most expensive item: " + box.getMaxItem() + " (" + box.getMaxPrice() + ")");
			System.out.println("cheapest item: " + box.getMinItem() + " (" + box.getMinPrice() + ")");
			System.out.printf("average price of items: %.2f", box.getAvgPrice());
		}
}
