package homework09;

class Point {
	private int dim;
	private double[] loc;
	
	Point(double[] d) {
		dim = 0;
		loc = d;
		for(double n : d) {
			dim++;
		}
	}
	public int getDim() {
		return dim;
	}
	public double[] getLoc() {
		return loc;
	}
}


class EuclideanDistance {
	static double getDist(Point p1, Point p2) {
		//예외 처리하기 -1
		if(p1.getDim() != p2.getDim()) {
			return -1;
		}
		else {
			double sum = 0;
			for(int i=0; i<p1.getDim(); i++) {
				sum += (p1.getLoc()[i] - p2.getLoc()[i]) * (p1.getLoc()[i] - p2.getLoc()[i]);
			}
			return Math.sqrt(sum);
		}
	}
}

class ManhattanDistance {
	static double getDist(Point p1, Point p2) {
		//예외 처리하기 -1
		if(p1.getDim() != p2.getDim()) {
			return -1;
		}
		else {
			double sum = 0;
			for(int i=0; i<p1.getDim(); i++) {
				sum += Math.abs(p1.getLoc()[i] - p2.getLoc()[i]);
			}
			return sum;
		}
	}
}

public class Problem09 { 
	public static void main(String[] args) { 
		Point p1 = new Point(new double[] {1.0, 2.0, 3.0}); 
		Point p2 = new Point(new double[] {4.0, 5.0, 6.0}); 
		System.out.println("Euclidean Distance: " + EuclideanDistance.getDist(p1, p2)); 
		System.out.println("Manhattan Distance: " + ManhattanDistance.getDist(p1, p2)); 
		Point p3 = new Point(new double[] {1.0, 2.0, 3.0}); 
		Point p4 = new Point(new double[] {4.0, 5.0}); 
		System.out.println("Euclidean Distance: " + EuclideanDistance.getDist(p3, p4)); 
		System.out.println("Manhattan Distance: " + ManhattanDistance.getDist(p3, p4)); 
	} 
}

