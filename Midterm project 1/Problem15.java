package homework15;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


class Item {
	private String item; 
	private int count = 1; 
	
	public Item(String item) {
		this.item = item;
		this.count = 1;
	}
	
	public Item(String item, int count) {
		this.item = item;
		this.count = count;
	}
	
	public String getItem() {
		return item;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String toString() {
		return item +" "+count;
	}
}

class MyFileReader {

	public static boolean readDataFromFile(String path, ArrayList<Item> itemList) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			HashMap<String, Integer> duplicateCount = new HashMap<String, Integer>(); // �� ��Ȳ������ count�� ������ HashMap�� ���°� ������ ����
			ArrayList<Item> itemDummy = new ArrayList<Item>(); // ����� Item ����Ʈ
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				String arr[] = line.split(" "); // �����������, ���Ͱ� ���ٴ� ����

				for (int i = 0; i < arr.length; i++) // �ڸ� ���ڿ� �迭�� �ϳ��ϳ� �ҹ��ڷ� ���� �ٽ� �־��ش�
				{
					arr[i] = arr[i].toLowerCase();
				}

				for (int i = 0; i < arr.length; i++) // item ��ü�� ����� ����� ����Ʈ�� �ִ´�
				{
					Item item = new Item(arr[i]);
					itemDummy.add(item);
				}
			}

			for (int i = 0; i < itemDummy.size(); i++) { // ArrayList ��ŭ �ݺ�
				if (duplicateCount.containsKey(itemDummy.get(i).getItem())) { // HashMap ���ο� �̹� key ���� �����ϴ��� Ȯ��
					duplicateCount.put(itemDummy.get(i).getItem(),
					duplicateCount.get(itemDummy.get(i).getItem()) + 1);// key�� �̹� �ִٸ� value�� +1

				} else { // key���� �������� ������ ���� �� �ʱ�ȭ
					duplicateCount.put(itemDummy.get(i).getItem(), 1);
				}
			}

			for (Item it : itemDummy) { // ���̿��� �ϳ��� item ��������

				for (String key : duplicateCount.keySet()) { // duplicateCount���� �������� �ߺ����� ���� �������� count�� ����� ���� �������� �������ִ�
					
					int value = duplicateCount.get(key); 
					Item item = new Item(key, value); //item ��ü ����
					boolean check = false;
					if (it.getItem().equals(key)) // ���̿� item�� key�� ������
					{
						for (Item it2 : itemList) { // itemList���� �ϳ��� item ��������
							
							if(it2.getItem().equals(it.getItem())) // ���࿡ itemList�� ���̿� �ִ� ���� �������� true�� �ٲ��ش�
							{
								check = true;
								break;
							}
							
						}
						if(!check) // ���� �ȵ�����쿡 ���� �־��ش�
						itemList.add(item); 
					}
				}
			}
			br.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
}


public class Problem15 {
	public static void main(String[] args) {
		ArrayList<Item> list = new ArrayList<>();
		boolean rv = MyFileReader.readDataFromFile("input_prob15.txt", list);
		if(rv == false) {
			System.out.println("Input file not found.");
			return;
		}
		for(Item it: list) System.out.println(it);
	}
}
