package homework08;

class Shape {
	//private variables로 선언조건 -> 메소드로 값을 호출하는 방법으로 만들자
	private double a;
	private double b;
	private double area;
	
	//변수 입력받는 기본작업 및 끌어다 사용할 메소드
	void setA(double d) {
		a = d;
	}
	
	double getA() {
		return a;
	}
	
	void setB(double d) {
		b = d;
	}
	
	double getB() {
		return b;
	}
	
	void setArea(double d) {
		area = d;
	}
	
	double getArea() {
		return area;
	}
}

class Circle extends Shape{
	
	Circle(double d) {
		setA(d);
		calArea();
	}
			
	void calArea() {
		setArea(Math.PI * getA() * getA());
	}
}
		
class Square extends Shape{
	Square(double d) {
		setA(d);
		calArea();
	}
			
	void calArea() {
		setArea(getA() * getA());
	}
}
		
class Rectangle extends Shape{
	Rectangle(double d1, double d2) {
		setA(d1);
		setB(d2);
		calArea();
	}
			
	void calArea() {
		setArea(getA() * getB());
	}
}

public class Problem08 {
	
	public static double sumArea(Shape[] arr) {
		double sum = 0;
		
		for(Shape s : arr) {
			sum += s.getArea();
		}
		return sum;
	}
	
	public static void main(String[] args) { 
		Shape[] arr = { new Circle(5.0), new Square(4.0), new Rectangle(3.0, 4.0), new Square(5.0)}; 
		System.out.println("Total area of the shapes is: " + sumArea(arr)); 
	} 
}

