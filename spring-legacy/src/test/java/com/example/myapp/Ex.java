package com.example.myapp;

// 클래스 간의 두 가지 관계
// 1.[상속관계] - A는 B이다. A is a B. (A는 B를 상속받는다.)
// 2.[포함관계] - A는 B를 가진다. A has a B.

public class Ex {

	public static void main(String[] args) {
		
		//객체 간의 의존성 관계를 조립해주는 역할을 함.
		
		Engine engine = new HyundaiEngine();
		
		Car car = new Car(engine);
		
		car.drive();
		
		System.out.println("==========================");
		
		car.setEngine(new ToyotaEngine());
		car.drive();
		
	}

}
