package com.example.myapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // 스프링이 관리하는 객체(스프링 빈) // 스프링은 빈과 빈 끼리만 서로 의존관계를 주입해준다.
public class Car {

	// 다른 클래스에 변화가 있어도 Car 클래스는 변화가 없도록 결합도 낮추기

	// 메소드(생성자)의 매개변수를 통해 외부로부터 필요한 의존객체를 주입받으면
	// 객체 간의 결합도를 낮출 수 있음 -> 유지보수가 용이한 구조가 됨.
	// 스스로 생성하면 안되고, 외부에서 받아서 쓰기만 하도록

	// Engine 인터페이스로 상속시키기
	
	// 멤버변수에도 Autowired 가능은 함
	// 하지만 스프링이 없이는 외부에서 주입할 방법이 없다.
	private Engine engine;

	// 생성자는 Autowired 생략 가능, 기본생성자x -> 매개변수 받는 생성자 정의 하나만 있어야함o
	public Car(Engine engine) {
		this.engine = engine;
	}

	@Autowired // 매개변수 타입으로 스프링 빈(Component)중에서 자동으로 찾아옴
	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public void drive() {
		engine.start();
		System.out.println("자동차가 움직입니다.");
	}

}
