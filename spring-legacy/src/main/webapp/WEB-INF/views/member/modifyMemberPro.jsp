<%@page import="org.mindrot.jbcrypt.BCrypt"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.example.domain.MemberVO"%>
<%@page import="com.example.repository.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 액션태그 이용해서 폼 파라미터값 가져와서 설정하기 -->

<jsp:useBean id="memberVO" class="com.example.domain.MemberVO"></jsp:useBean>

<jsp:setProperty property="*" name="memberVO" />

<%
memberVO.setRegDate(new Timestamp(System.currentTimeMillis()));

MemberDAO memberDAO = MemberDAO.getInstance();

// DB 테이블에서 id에 해당하는 행 가져오기
MemberVO dbMemberVO = memberDAO.getMemberById(memberVO.getId());
%>

<%
String birthday = memberVO.getBirthday();
birthday = birthday.replace("-", "");
memberVO.setBirthday(birthday);
%>


<%
// 비밀번호 일치하면 회원정보 수정하기
if (BCrypt.checkpw(memberVO.getPasswd(), dbMemberVO.getPasswd()) == true) {

	memberDAO.updateById(memberVO);
%>
<script>
	alert('회원정보 수정 완료');
	location.href = '/index.jsp';
</script>
<%
} else {
%>
<script>
	alert('비밀번호 틀림');
	history.back(); // 뒤로가기
</script>
<%
}
%>