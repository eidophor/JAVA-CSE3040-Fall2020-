package homework11;

class PalindromeChecker {
	private static String str;
	private static String reversed;
	private static String letter;
	
	
	//checking method
	public static void palCheck(String str) {
		reversed = "";
		letter = "";
		
        for (int i = 0 ; i < str.length(); i++) {
			letter = str.substring(i, i+1);
            reversed = letter + reversed; 
        }
        
        System.out.println(reversed);
        
        if (str.equals(reversed)) {
        	System.out.println(str + " is a palindrome.");
        }
        else {
        	System.out.println(str + " is not a palindrome.");
        }
       
	}
	
	//static method check
	public static void check(String string) {
		str = string;
		System.out.println(str);
		palCheck(str);
	}
	
	public static void check(int num) {
		str = Integer.toString(num);
		System.out.println(str);
		palCheck(str);
	}
}

public class Problem11 {
	public static void main(String args[]) {
		PalindromeChecker.check("abcde");
		PalindromeChecker.check("abcba");
		PalindromeChecker.check(1234);
		PalindromeChecker.check(12321);
	}
}
