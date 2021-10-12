package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Component애노테이션 계열. 스프링 빈
public class HomeController {

	// 메서드명은 실질적으로 사용하지는 않고 주석처럼 알기쉽게 길게 쓸 때도 있음. 밸류값에 뭐가 들어오냐에 따라서 리턴함.
	// 애노테이션으로 메서드 단위로 관리함
	// @GetMapping("/")
	@GetMapping(value = { "/", "/index" })
	public String home() {
		System.out.println("home() 호출됨..."); // 잘 돌아가는지 확인

		return "index"; // return "redirect:index"; 로 리다이렉트 or 포워드 구분
		// servlet-context.xml 에서 prefix suffix로 됨
	}

} // HomeController
