<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@page import="com.example.domain.MemberVO"%>
<%@page import="com.example.repository.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
String passwd = request.getParameter("passwd");

MemberDAO memberDAO = MemberDAO.getInstance();

String id = (String) session.getAttribute("id");

MemberVO memberVO = memberDAO.getMemberById(id);

//암호화된 아이디 비교

if (BCrypt.checkpw(passwd, memberVO.getPasswd())) {
	memberDAO.deleteById(id);

	//세션 비우기
	session.invalidate();// 초기화. 모든 세션 삭제

	//쿠키값 비우기
	Cookie[] cookies = request.getCookies();//쿠키값 가져오기

	// 쿠키가 있으면 필터때문에 다시 세션이 생기기 때문에 쿠키도 삭제해줘야함
	// 쿠키는 직접적으로 삭제할 수 없음.
	//특정 쿠키 삭제하기(브라우저가 삭제하도록 유효기간 0초로 설정한 쿠키 보내서 덮어씌우기)
	if (cookies != null) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("loginId")) {
				cookie.setMaxAge(0); // 쿠키를 직접적으로 삭제하는것은 불가 -> 삭제되도록 유도하기
				cookie.setPath("/"); // 같은 경로로 설정해서 덮어씌우기위해
				response.addCookie(cookie); // 응답객체에 추가하기
			}
		}
	}
%>
<script>
	alert('회원탈퇴 하였습니다.');
	location.href = '/';
</script>
<%
} else {
%>
<script>
	alert('비밀번호가 틀림');
	history.back();
</script>
<%
}
%>



