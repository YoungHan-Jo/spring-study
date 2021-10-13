package com.example.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.MemberVO;
import com.example.service.MemberService;
import com.example.util.JScript;

//예전에는 implements Controller 로 한 클래스당 1개의 기능만 했기때문에 이제는 사용하지 않음.
// @Controller 애노테이션을 사용함.
// @Component 계열 애노테이션이므로 스프링 빈이 됨.
// 스프링의 프론트컨트롤러인 DispatcherServlet 객체가 사용할 컨트롤러가 됨.

@Controller
@RequestMapping("/member/*") // //member/에 관한 요청만 받을 수 있음.
public class MemberController {

	// @Autowired
	private MemberService memberService;

	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping("/join") // GET 요청 @RequestMapping "/member/"에 + "join"이 됨 // / 있어도 알아서 없애줌
	public void joinForm() {
		System.out.println("join 호출됨...");

		// return "member/join";

		// 컨트롤러 메소드의 리턴타입이 void일 경우는
		// 요청 URL주소("/member/join")를 실행할 JSP 경로명("/member/join")으로 사용함.
		// URL 요청 경로명과 JSP 경로명이 같을 경우 사용할 수 있다.
		// 단순한 구조일 때 혹은 회사에 따라 사용할 경우도 있음.
	}

	@PostMapping("/join") // POST 요청
	public ResponseEntity<String> join(MemberVO memberVO) { // 요청받은 값을 전부 setter로 입력한 채로 memberVO객체가 생성됨
		// 스프링의 프론트 컨트롤러(DispatcherServlet)는
		// 호출하는 컨트롤러의 매개변수 타입(MemberVO)이 VO에 해당하면(getter/setter가 존재하면)
		// VO객체를 생성후 사용자 요청 파라미터 값을 자동으로 채워진 채로 나옴

		// 회원가입 날짜 설정
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

		memberService.register(memberVO);

		// forward로 하면 url이 그대로라서 입력 데이터가 남아있어서 서버터짐

		// 리다이렉트 방법1.
		// return "redirect:/member/login"; // 리다이렉트 요청경로를 리턴

		// 리다이렉트 방법2.
		// 스프링에서는 너무 많은 권한을 가진 request, response 객체에 대해서
		// 직접적인 접근을 최소화하길 권장함. (방대하기 때문에 다른 개발자에게 영향을 미침)
		// MVC2에서는 response로 직접 처리했지만 스프링에서는 스프링에게 정보만 제공해서 간접적으로 처리

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=UTF-8");

		String str = JScript.href("회원가입 완료", "/member/login");

		return new ResponseEntity<String>(str, headers, HttpStatus.OK);

	} // join

	@GetMapping("/login")
	public void loginForm() {
		System.out.println("login 호출됨...");
	}

	@PostMapping("login") // POST 요청 /member/login
	public ResponseEntity<String> login(String id, String passwd,
			@RequestParam(required = false, defaultValue = "false") boolean rememberMe, HttpSession session,
			HttpServletResponse response) {
		// @RequestParam 필수값이 아니다. 값이 없어도 메소드 실행가능
		// "true" 값이 String 이라도 Boolean.parseBoolean("true"); 로 자동 형변환
		// 숫자로 하면 숫자로도 자동으로 바뀜
		// 컨트롤러 메소드의 매개변수 형식이 기본자료형(String형 포함)에 해당하면
		// 해당 매개변수 이름으로 사용자 입력한 파라미터 값(name속성 값)을 자동으로 찾아줌

		MemberVO memberVO = memberService.getMemberById(id);

		boolean isPasswdSame = false;
		String message = "";

		if (memberVO != null) { // id 일치
			isPasswdSame = BCrypt.checkpw(passwd, memberVO.getPasswd());

			if (isPasswdSame == false) { // 비밀번호 일치하지 않음
				message = "비밀번호가 일치하지 않습니다.";
			}
		} else { // id 존재하지않음
			message = "존재하지 않는 아이디 입니다.";
		}

		// 로그인 실패인 경우 (없는 아이디거나 비밀번호가 틀렸을때)
		if (memberVO == null || !isPasswdSame) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "text/html; charset=UTF-8");

			String str = JScript.back(message);
			return new ResponseEntity<String>(str, headers, HttpStatus.OK);
		}

		// 로그인 성공인 경우
		// 로그인 인증처리
		// 사용자 당 유지되는 세션객체에 기억할 데이터를 저장
		// 매개변수에 HttpSession session 넣으면 자동으로 찾아줌
		session.setAttribute("id", id);

		// 로그인 상태유지가 체크
		if (rememberMe) {
			Cookie cookie = new Cookie("loginId", id);
			cookie.setMaxAge(60 * 60 * 24 * 7); // 쿠키 수명
			cookie.setPath("/"); // 쿠키 적용 경로 설정
			response.addCookie(cookie); // 응답객체에 쿠키 추가
		}

		// 리다이렉트 방식 1. String 리턴
		// return "redirect:/";로 보통 하지만 리턴값이 ResponseEntity 타입이기 때문에 안됨

		// 리다이렉트 방식 2. ResponseEntity로 리턴
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/"); // redirect 경로를 "/"로 지정

		// 리다이렉트일 경우는 응답코드로 HttpStatus.FOUND 를 지정해야함에 주의 !%%%%%%%%%%
		return new ResponseEntity<String>(headers, HttpStatus.FOUND);

	} // login

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		// 세션 비우기
		session.invalidate();

		Cookie[] cookies = request.getCookies();
		
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("loginId")) {
					cookie.setMaxAge(0); // 쿠키를 직접적으로 삭제하는것은 불가 -> 삭제되도록 유도하기
					cookie.setPath("/"); // 같은 경로로 설정해서 덮어씌우기위해
					response.addCookie(cookie); // 응답객체에 추가하기
				}
			}
		}

		// 로그인 화면으로 리다이렉트 이동
		return "redirect:/member/login";
	} // logout

}
