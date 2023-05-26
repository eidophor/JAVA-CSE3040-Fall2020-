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

class FruitBox implements Map { // ���� ��ӹ޴´�
	private Map<String, Double> arrMap = new TreeMap<String, Double>(); // TreeMap�� Ű���������� ����

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
		String str = "";  // ���ڿ� ����
		List<Entry<String, Double>> listEntries = new ArrayList<Entry<String, Double>>(arrMap.entrySet());  // ����Ʈ��

		for(Entry<String, Double> entry : listEntries) { // ���� �ϳ��� ����
			str = str + entry.getKey() +" "+ entry.getValue() + "\n"; // ���ڿ��� �����̸��� ���� �����ְ� �ٹٲ�
		}
        return str;
	}
	@Override
	public Object put(Object key, Object value) { // �� �ֱ�
		arrMap.put((String)key, (Double)value); 
		return null;
	}
}

class MapManager {

	public static Map<String, Double> readData(String path) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Double> arrMap = new FruitBox();
			BufferedReader br = new BufferedReader(new FileReader(path)); // �����о����
			while (true) {
				String line = br.readLine(); 
				if (line == null)
					break; 
				String arr[] = line.split(" "); // ����������� �߶� ���ڿ� �迭 �����
		
				arrMap.put(arr[0], Double.parseDouble(arr[1])); // ���ٿ� ù��° ���� �����̸� �ι��� ���� �����̴� ������ String�� �̹Ƿ�
																		// double������ ��ȯ���ش�
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