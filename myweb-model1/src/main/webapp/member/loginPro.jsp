<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@page import="com.example.domain.MemberVO"%>
<%@page import="com.example.repository.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// loginForm id passwd 파라미터 가져오기
String id = request.getParameter("id");
String passwd = request.getParameter("passwd");
String rememberMe = request.getParameter("rememberMe");

// MemberDAO 객체 준비하기
MemberDAO memberDAO = MemberDAO.getInstance();

MemberVO memberVO = memberDAO.getMemberById(id);

if (memberVO != null) { // 아이디 일치
	// if (memberVO.getPasswd().equals(passwd)) 

	// BCrypt.checkpw(입력한passwd, db의passwd ) 비교해서 true/false 리턴
	if (BCrypt.checkpw(passwd, memberVO.getPasswd())) { // passwd 일치
		// 로그인 인증 처리
		// 사용자마다 유지되는 세션객체에 기억할 데이터를 저장. // 나중에 폐기됨.
		session.setAttribute("id", id); // 사용자 정보 키+객체 hashmap 형태로 저장

		// 사용자가 로그인 상태유지를 체크했으면
		if (rememberMe != null) {
			//쿠키 생성
			Cookie cookie = new Cookie("loginId", id);
		
			//쿠키 유효기간 늘리기/ 기본값 0 이라서 바로없어짐
			cookie.setMaxAge(60 * 60 * 24 * 7);
		
			//쿠키를 사용할 경로 설정
			cookie.setPath("/"); // root 로 설정 프로젝트 모든 경로에서 쿠키 받도록 설정
		
			//클라이언트로 보낼때 쿠키를 response 응답객체에 추가해주기
			response.addCookie(cookie);
		}

		// index.jsp 메인페이지로 이동
		//자바방식
		response.sendRedirect("/index.jsp"); // 리다이렉트 정보 : 서버가 시킨대로 재요청하는 주소.
		// index는 톰캣 기본화면으로 설정해뒀기때문에 '/'만 적어도 됨.
%>

<!-- 리다이렉트 자바스크립트 방식 -->
<!-- <script>
	alert('로그인 성공');
	location.href = 'index.jsp';
</script> -->
<%
} else { // passwd 불일치
%><script>
	alert('비밀번호가 일치하지 않습니다.');
	history.back();
</script>
<%
}
} else { // 아이디 불일치
%><script>
	alert('존재하지 않는 아이디입니다.');
	/* location.href='loginForm.jsp'; // 로그인 폼 페이지로 새로 요청하기*/
	history.back(); /* 페이지 뒤로가기, 앞에 window. 은 생략가능. */
</script>
<%
}
%>