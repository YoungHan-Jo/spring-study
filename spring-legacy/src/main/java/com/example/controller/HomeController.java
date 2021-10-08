package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.mapper.MemberMapper;

@Controller // Component애노테이션 계열. 스프링 빈
public class HomeController {
	
	@Autowired
	private MemberMapper memberMapper;

	// 애노테이션으로 메서드 단위로 관리함
	//@GetMapping("/")
	@GetMapping(value = {"/", "/index"})
	public String home() {
		System.out.println("home() 호출됨...");
		
		return "index"; // return "redirect:index"; 로 리다이렉트 or 포워드 구분
	}
	
	
} //HomeController
