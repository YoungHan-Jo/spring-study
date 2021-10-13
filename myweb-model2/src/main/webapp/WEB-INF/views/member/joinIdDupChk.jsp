<%@page import="com.example.repository.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%

String id = request.getParameter("id");

MemberDAO memberDAO = MemberDAO.getInstance();

int count = memberDAO.getCountById(id);

%>    
    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2>아이디 중복확인</h2>
<hr>

<%
if(count == 1){
	%>
	<p>아이디중복, 이미 사용중인 ID 입니다.</p>
	<%
} else{
	%>
	<p><%=id %> 는 사용가능한 ID 입니다.</p>
	<button type="button" id="btnUseId">ID 사용</button>
	<%
}
%>

<form action="/member/joinIdDupChk.jsp" method="get" name="frm">
	<input type="text" name="id" value="<%=id %>">
	<button type="submit">ID 중복확인</button>
	
</form>

<script src="/resources/js/jquery-3.6.0.js"></script>
<script>
	$('#btnUseId').on('click',function(){
		// 현재창(window).부모창(opener)
		window.opener.document.frm.id.value = frm.id.value;
		
		// 현재창 닫기 window.close();
		close();
	})

</script>
</body>
</html>