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
			HashMap<String, Integer> duplicateCount = new HashMap<String, Integer>(); // 이 상황에서는 count를 세려면 HashMap을 쓰는게 좋은것 같다
			ArrayList<Item> itemDummy = new ArrayList<Item>(); // 예비용 Item 리스트
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				String arr[] = line.split(" "); // 공백기준으로, 엔터가 없다는 가정

				for (int i = 0; i < arr.length; i++) // 자른 문자열 배열을 하나하나 소문자로 만들어서 다시 넣어준다
				{
					arr[i] = arr[i].toLowerCase();
				}

				for (int i = 0; i < arr.length; i++) // item 객체를 만들어 예비용 리스트에 넣는다
				{
					Item item = new Item(arr[i]);
					itemDummy.add(item);
				}
			}

			for (int i = 0; i < itemDummy.size(); i++) { // ArrayList 만큼 반복
				if (duplicateCount.containsKey(itemDummy.get(i).getItem())) { // HashMap 내부에 이미 key 값이 존재하는지 확인
					duplicateCount.put(itemDummy.get(i).getItem(),
					duplicateCount.get(itemDummy.get(i).getItem()) + 1);// key가 이미 있다면 value에 +1

				} else { // key값이 존재하지 않으면 생성 후 초기화
					duplicateCount.put(itemDummy.get(i).getItem(), 1);
				}
			}

			for (Item it : itemDummy) { // 더미에서 하나씩 item 꺼내오기

				for (String key : duplicateCount.keySet()) { // duplicateCount에는 아이템이 중복없이 값이 들어가있으며 count는 제대로 값에 맞춰져서 세어져있다
					
					int value = duplicateCount.get(key); 
					Item item = new Item(key, value); //item 객체 생성
					boolean check = false;
					if (it.getItem().equals(key)) // 더미에 item과 key가 같을떄
					{
						for (Item it2 : itemList) { // itemList에서 하나씩 item 꺼내오기
							
							if(it2.getItem().equals(it.getItem())) // 만약에 itemList에 더미에 있는 값이 들어가있으면 true로 바꿔준다
							{
								check = true;
								break;
							}
							
						}
						if(!check) // 만약 안들어갔을경우에 값을 넣어준다
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
