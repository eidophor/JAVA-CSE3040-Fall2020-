package homework13;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

class Text {
	private String fileAdr;
	private char c;
	private String str;
	private int[] count; 
	private boolean a;
	
	public boolean readTextFromFile(String str) {
		int ch;
		fileAdr = str;
		StringBuffer sb = new StringBuffer();
		InputStreamReader r;
		
		try {
			r = new InputStreamReader(new FileInputStream(fileAdr));
		}
		catch (FileNotFoundException e) {
			this.a = false;
			System.out.println("Input file not found."); // catch문 서순 맞춰서 exception 처리하기
			return this.a;
		}
		
		while (true) {
			try {
				if ((ch = r.read())!= -1) {
					sb.append((char)ch);
					this.a = true;
				}
				else {
					break;
				}
			}
			catch (IOException e) {
				this.a = false;
				e.printStackTrace();
			}
		}
		
		try {
			r.close();
		}
		catch (Exception e) {
			this.a = false;
			e.printStackTrace();
		}
		this.str = new String(sb);
		return this.a; //true false 반환하기
}

	public int countChar(char c) {
		this.c = c;
		count = new int [1];
		count[0] = 0;
		str = str.toLowerCase();
		
		for (int i = 0 ; i < str.length() ; i++) {
			if (this.c == str.charAt(i)) {
				count[0]++;
			}
		}
		return count[0];
	}
}

public class Problem13 {
	public static void main(String[] args) {
		Text t = new Text();
		if(t.readTextFromFile("input_prob13.txt")) {
			for (char c = 'a' ; c <= 'z'; c++) {
				System.out.println(c + ": " + t.countChar(c));
			}
		}
	}
}
