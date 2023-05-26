package homework03;

import java.util.Scanner;

public class Problem3 {

	public static void main(String[] args) {
		
		
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a text: ");
		String str = in.nextLine();
		String inLet ="";
		while (true) {
			System.out.print("Enter a letter: ");
			inLet = in.nextLine();
			
			if (inLet.length() > 1) {
				System.out.println("You must enter a single letter.");
				continue;
			}
			else if (inLet.equals("")) {
				System.out.println("You must enter a single letter.");
				continue;
			}
			else break;
		}
		
		
		String temp [] = str.split("");
		
		int count = 0;
		
		for (int i = 0 ; i < str.length() ; i++) {
			
			if (temp[i].equals(inLet)) {
				count++;
			}
			
		}
		
		System.out.println("There are " + count + " " + inLet +"'s in the text.");
		
		in.close();
	}

}
