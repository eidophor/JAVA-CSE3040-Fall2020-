package homework04;

import java.util.Scanner;

public class Problem4 {

	public static void main(String[] args) {
		
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a text: ");
		String text = in.nextLine();
		String inStr ="";
		
		while (true) {
			System.out.print("Enter a string: ");
			inStr = in.nextLine();
			
			if (inStr.equals("")) {
				System.out.println("You must enter a string.");
				continue;
			}
			else break;
		}
		
		int count = 0;
		
		for (int i = 0 ; i < (text.length()-(inStr.length()-1)) ; i++) {
			
			String temp = text.substring(i,i+inStr.length());
			
			if (temp.equals(inStr)) {
				count++;
			}
			
		}
		
		System.out.println("There are " + count + " instances of " + "\"" + inStr +"\".");
		
		in.close();
	}

}