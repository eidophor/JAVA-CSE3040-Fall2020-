package homework17;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class FruitBox implements Map { // 맵을 상속받는다
	private Map<String, Double> arrMap = new TreeMap<String, Double>(); // TreeMap은 키값기준으로 정렬

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}
	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Set keySet() {
		return null;
	}
	@Override
	public void putAll(Map m) {
		// TODO Auto-generated method stub
	}
	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {
		String str = "";  // 문자열 선언
		List<Entry<String, Double>> listEntries = new ArrayList<Entry<String, Double>>(arrMap.entrySet());  // 리스트로

		for(Entry<String, Double> entry : listEntries) { // 값을 하나씩 꺼내
			str = str + entry.getKey() +" "+ entry.getValue() + "\n"; // 문자열에 과일이름과 값을 더해주고 줄바꿈
		}
        return str;
	}
	@Override
	public Object put(Object key, Object value) { // 값 넣기
		arrMap.put((String)key, (Double)value); 
		return null;
	}
}

class MapManager {

	public static Map<String, Double> readData(String path) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Double> arrMap = new FruitBox();
			BufferedReader br = new BufferedReader(new FileReader(path)); // 파일읽어오기
			while (true) {
				String line = br.readLine(); 
				if (line == null)
					break; 
				String arr[] = line.split(" "); // 공백기준으로 잘라서 문자열 배열 만들기
		
				arrMap.put(arr[0], Double.parseDouble(arr[1])); // 각줄에 첫번째 열은 과일이름 두번쨰 열은 가격이다 가격은 String형 이므로
																		// double형으로 변환해준다
			}
			br.close();
			return arrMap;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}

public class Problem17 {
	public static void main(String[] args) {
		Map<String, Double> map = MapManager.readData("input.txt"); 
		if(map == null) { 
			System.out.println("Input file not found.");
			return;
		}
		System.out.println(map);
	}
}