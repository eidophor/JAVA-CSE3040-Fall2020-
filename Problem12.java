package homework12;

class SubsequenceChecker {
	private static String str1, str2, sub;
	private static int count = 0;
	private static boolean tf;
	
	
	public static void subCheck() {
		int indexArg[] = new int[str2.length()];
		
		for (int i = 0 ; i < str2.length() ; i++) {
			
			if (sub.indexOf(str2.charAt(i)) == -1) {
				System.out.println(str2 + " is not a subsequence of " + str1);
				tf = false;
				break;
			}
			
			indexArg[i] = sub.indexOf(str2.charAt(i)) + count;
			
			//count에는 문자열을 잘라내어 초기 인덱스가 다시 0으로 되므로 1을 더 더해준다.
			count += sub.indexOf(str2.charAt(i)) + 1;
			
			sub = sub.substring(sub.indexOf(str2.charAt(i)) + 1, sub.length());	
			tf = true;
		}
		
		if (tf == true) {
		
		System.out.println(str2 + " is a subsequence of " + str1);
		
		for (int j = 0 ; j < str2.length() ; j++) {
			System.out.print(indexArg[j] + " ");
		}
		System.out.println("");
		}
}
	
	public static void check(String statement, String test) {
		str1 = statement;
		str2 = test;
		sub = str1;
		subCheck();
	}
}

public class Problem12 {

	public static void main(String[] args) {
		SubsequenceChecker.check("supercalifragilisticexpialidocious", "pads");
		SubsequenceChecker.check("supercalifragilisticexpialidocious", "padsx");
	}
}
