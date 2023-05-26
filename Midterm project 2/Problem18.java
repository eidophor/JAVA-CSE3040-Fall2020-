package homework18;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class FruitBox implements Map { // 맵을 상속받는다
	private Map<String, Double>  arrMap = new LinkedHashMap<String, Double>();

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
	//toString 메소드 오버라이딩
	public String toString() {
		
		Map<String, Double>  arrMap2 = new LinkedHashMap<String, Double>();  // 넣은순서대로 들어가는 LinkedHashMap을 선언
		
		List<String> keySetList = new ArrayList<>(arrMap.keySet()); // 정렬용 리스트
        Collections.sort(keySetList, new Comparator<String>() { 
            public int compare(String o1, String o2) {  // 리스트에서 키값들을 가져온다
            		// 키값의 value를 비교하여 앞에 값이 크면 1 작은값이면 -1 같으면 0
            		if(arrMap.get(o1)>arrMap.get(o2)) {
            			return 1;
            		}
            		else if(arrMap.get(o1)<arrMap.get(o2)) {
            			return -1;
            		}
            		else // 같을경우 key를 비교하여 앞에 값이 크면 1을 반환시켜 오름차순으로 한다
            		{
            	    	if(o1.compareTo(o2)>0) {
            	    		return 1;
                    	}
            	    	else if(o1.compareTo(o2)<0) {
            	    		return -1;
            	    	}
            	    	else {
            	    		return 0;
            	    	}
            		}
            }
        });

        for (String i : keySetList) arrMap2.put(i, arrMap.get(i)); 
		
		String str = "";  
		List<Entry<String, Double>> list_entries = new ArrayList<Entry<String, Double>>(arrMap2.entrySet());  

	
		for(Entry<String, Double> entry : list_entries) { // 값을 하나씩 꺼내서
			str = str + entry.getKey() +" "+  entry.getValue() +"\n"; // 문자열에 과일이름과 값을 더하고 줄바꿈
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
			Map<String, Double>  arrMap = new FruitBox();

			BufferedReader br = new BufferedReader(new FileReader(path)); 
			while (true) {
				String line = br.readLine();
				if (line == null)
					break; 
				String arr[] = line.split(" ");
				arrMap.put(arr[0], Double.parseDouble(arr[1])); 
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

public class Problem18 { 
	public static void main(String args[]) { 
		Map<String, Double> map = MapManager.readData("input.txt"); 
		if(map == null) {
			System.out.println("Input file not found."); 
			return;
			}
		System.out.println(map); 
	}
}