package homework01;

import java.util.Scanner;

public class Problem1 {

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);
		
		while (true) {
		
		System.out.print("ASCII code teller. Enter a letter : ");

		String str = in.nextLine();
		
		if (str.equals("")) {
			System.out.println("You must input a single uppercase or lowercase alphabet.");
			break;
		}
		
		int ascCode;
		char inLetter = str.charAt(0);
		ascCode = (int) inLetter;
			
		
		if (str.length() > 1) {
			System.out.println("You must input a single uppercase or lowercase alphabet.");
			break;
		}
		else if (ascCode < 65 || ascCode > 122) {
			System.out.println("You must input a single uppercase or lowercase alphabet.");
			break;
		}
		else if (ascCode > 90 && ascCode < 97) {
			System.out.println("You must input a single uppercase or lowercase alphabet.");
			break;
		}
		else {
			System.out.println("The ASCII code of " + str + " is " + ascCode + ".");
			break;
			
		}
	
	    }
	
		
		in.close();
	
		
		
		
	}

}
