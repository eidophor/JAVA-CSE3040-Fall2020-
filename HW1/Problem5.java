package homework05;

import java.util.Scanner;

public class Problem5 {

	public static void main(String[] args) {
		int inScore;
		int[] data = new int[5];
		int[] place = new int[5];
		Scanner in = new Scanner(System.in);
		System.out.println("Enter exam scores of each student.");
		
		
		
		for ( int i=0 ; i<5 ; i++) {
			System.out.print("Score of student "+ (i+1) +": ");
			inScore = in.nextInt();
			data [i]= inScore;
			}
		// 1과 2,3,4,5 비교 -> 2와 3,4,5비교 -> 3과 4,5비교 -> 4와 5비교	
		// 총 10번 유효한 비교 -> rank 0부터 4까지 생성(1+2+3+4=10)
		for ( int i=0 ; i<5 ; i++)  {
			for (int j=0; j<5; j++) {
				if (data[i] < data[j]) {
					place[i]++;
				}
				else continue;
			}
			
		}
		//place=0이 1등 place=1이 2등
		for ( int i = 0 ; i<5 ; i++) {
			if (place[i] == 0) {
				System.out.println("The 1st place is student "+ (i+1) + " with " + data[i] + " points.");
			}
		}
		for ( int i = 0; i<5 ; i++) {	
			if (place[i] == 1) {
				System.out.println("The 2nd place is student "+ (i+1) + " with " + data[i] + " points.");
			}
		}
		
		
		in.close();
		}
		
		

	}


