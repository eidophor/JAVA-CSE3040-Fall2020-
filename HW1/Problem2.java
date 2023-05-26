package homework02;

import java.util.Scanner;

public class Problem2 {

	public static void main(String[] args) {
		
		
		int randInt, gsInt;

		
		Scanner in = new Scanner(System.in);
		
		randInt = (int)(100 * Math.random()) + 1;  
        int t = 1, min = 1, max = 100;
        
       while (true) {
        	
        	System.out.print("[" + t + "] Guess a number (" + min + "-" + max + "): ");
        	gsInt = in.nextInt();
        	
        	if (gsInt < min || gsInt > max ) {
        		System.out.println("Not in range!");
        		continue;
        	}
        	else if (gsInt == randInt) {
        		System.out.println("Correct! Number of guesses: " + t);
        		break;
        	}
        	else if (gsInt < randInt) {
        		System.out.println("Too small!");
        		t++;
        		min = gsInt + 1;
        		continue;
        	}
        	else if (gsInt > randInt) {
        		System.out.println("Too large!");
        		t++;
        		max = gsInt - 1;
        		continue;
        	}
  
        	
        }
       	in.close();
               
	}

}
