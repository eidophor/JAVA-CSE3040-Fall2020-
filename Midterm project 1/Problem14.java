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
		double max = -1; //먼저 max값을 -로 잡고 만약 item의 값이 max값보다 크면 max값을 교체
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
			min = list.get(0).price(); // min값을 맨 앞에 가격으로 잡고 다음가격이 더 작으면 교체
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
	
	// 가격과 같은방식
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
	
	// 전체 가격을 size로 나누기
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
				String line = br.readLine(); // 한줄씩 처리하자
				if (line == null)
					break; 
				String arr[] = line.split(" "); // 공백기준으로 잘라서 문자열 배열, 텍스트에 엔터는 없다는 가정 
				f.add(new Fruit(arr[0], Double.parseDouble(arr[1]))); 
				// 각줄에 두번쨰 열은 가격인데 String형 이므로 double형으로 변환
			}
			br.close();
			return true;
		} catch (FileNotFoundException e) { //익셉션 상하위 순서고려해서 한번에
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
