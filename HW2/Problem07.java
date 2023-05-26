package homework07;

import java.util.Scanner;

interface IntSequenceStr {
	boolean hasNext();
	int next();
}

class BinarySequenceStr implements IntSequenceStr {
	int p; 
	int[] binary; 
	int now;
	
	// Operator에 넣지 않으면 무한루프 돈다!! 당연한 것 
	public BinarySequenceStr(int num) {
		p = 0;
		now =0;
		int n = num;
		
		if(num == 0) {
			binary = new int[1];
			binary[0] = 0;
			p = 1;
		}
		else {
			while(Math.pow(2, p) <= num) {
				p++;
			}
			
			binary = new int[p];
			for(int i=p-1; i>=0; i--) {
				binary[i] = n % 2;
				n /= 2;
			}
		}
		
	}
	
	public boolean hasNext() {
		if (now < p) {
			return true;
		}
		else {
			return false;
		}
	}
		
	
	public int next() {
		
		return binary[now++];
       }
}
		

public class Problem07 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a positive integer: ");
		String str = in.nextLine();
		int num = Integer.parseInt(str);
		in.close();
		System.out.println("Integer: " + num);
		IntSequenceStr seq = new BinarySequenceStr(num);
		System.out.print("Binary number: ");
		while(seq.hasNext()) System.out.print(seq.next());
		System.out.println(" ");
	}
}
