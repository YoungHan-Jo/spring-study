package com.example.controller;

import java.util.Date;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.MemberVO;
import com.example.mapper.MemberMapper;


//예전에는 implements Controller 로 한 클래스당 1개의 기능만 했기때문에 이제는 사용하지 않음.
// @Controller 애노테이션을 사용함.
// @Component 계열 애노테이션이므로 스프링 빈이 됨.
// 스프링의 프론트컨트롤러인 DispatcherServlet 객체가 사용할 컨트롤러가 됨.

@Controller
@RequestMapping("/member/*") // //member/에 관한 요청만 받을 수 있음.
public class MemberController {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@GetMapping("/join") // GET 요청 @RequestMapping "/member/"에  + "join"이 됨 // / 있어도 알아서 없애줌
	public void joinForm() {
		System.out.println("join 호출됨...");
		
		//return "member/join";
		
		// 컨트롤러 메소드의 리턴타입이 void일 경우는
		// 요청 URL주소("/member/join")를 실행할 JSP 경로명("/member/join")으로 사용함.
		// URL 요청 경로명과 JSP 경로명이 같을 경우 사용할 수 있다.
		// 단순한 구조일 때 혹은 회사에 따라 사용할 경우도 있음.
	}
	
	@PostMapping("/join") // POST 요청
	public String join(MemberVO memberVO) { // 요청받은 값을 전부 setter로 입력한 채로 memberVO객체가 생성됨
		// 스프링의 프론트 컨트롤러(DispatcherServlet)는
		// 호출하는 컨트롤러의 매개변수 타입(MemberVO)이 VO에 해당하면(getter/setter가 존재하면)
		// VO객체를 생성후 사용자 요청 파라미터 값을 자동으로 채워진 채로 나옴
		
		//회원가입 날짜 설정
		memberVO.setRegDate(new Date());
		
		// 비밀번호를 jbcrypt 라이브러리 사용해서 암호화하여 저장하기
		String passwd = memberVO.getPasswd();
		String pwHash = BCrypt.hashpw(passwd, BCrypt.gensalt());
		memberVO.setPasswd(pwHash); // 암호화된 비밀번호 문자열로 수정하기
		
		// 생년월일 '-' 제거
		String birthday = memberVO.getBirthday();
		birthday = birthday.replace("-", "");
		memberVO.setBirthday(birthday);
		
		System.out.println(memberVO);
		
		memberMapper.insert(memberVO);
		
		// forward로 하면 url이 그대로라서 입력 데이터가 남아있어서 서버터짐
		return "redirect:/member/login"; // 리다이렉트 요청경로를 리턴
		
		
	}
	
	@GetMapping("/login")
	public void login() {
		System.out.println("login 호출됨...");
	}

}
