<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@page import="com.example.repository.MemberDAO"%>
<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%-- post 요청 한글처리는 필터로 처리함 --%>

<jsp:useBean id="memberVO" class="com.example.domain.MemberVO" />

<!-- 사용자입력값 가져오기 -->
<jsp:setProperty property="*" name="memberVO" />

<%
memberVO.setRegDate(new Timestamp(System.currentTimeMillis()));
%>

<%
// 비밀번호를 jbcrypt 라이브러리 사용해서 암호화하여 저장하기
String passwd = memberVO.getPasswd();
String pwHash = BCrypt.hashpw(passwd, BCrypt.gensalt());
memberVO.setPasswd(pwHash); // 암호화된 비밀번호 문자열로 수정하기

%>

<%
String birthday = memberVO.getBirthday();
birthday = birthday.replace("-", "");
memberVO.setBirthday(birthday);
%>
<%
MemberDAO memberDAO = MemberDAO.getInstance();
%>
<%
memberDAO.insert(memberVO);
%>

<script>
	alert('회원가입 성공');
	location.href = '/member/login.jsp';
</script>