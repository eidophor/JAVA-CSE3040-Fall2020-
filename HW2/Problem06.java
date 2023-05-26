package homework06;

interface IntSequence {
	boolean hasNext();
	int next();
}


class FibonacciSequence implements IntSequence {
	
	private int num1 =0 , num2 =0, sum = 0, j = 0;
	
	public boolean hasNext() {
		if (sum < 4182) {
			return true;
		}
		else return false;
	}
	
	public int next() { 
		
		sum = num1 + num2;
		
		if (j==1) {
			sum = 1;
		}
		
		num1 = num2;
		num2 = sum;

		j++;
		
		return sum;
	}
	
}

public class Problem06 {
	public static void main(String[] args) {
		IntSequence seq = new FibonacciSequence(); 
		for(int i=0; i<20; i++) { 
			if(seq.hasNext() == false) break; 
			System.out.print(seq.next() + " "); 
		} 
		System.out.println(" ");
	}

}
