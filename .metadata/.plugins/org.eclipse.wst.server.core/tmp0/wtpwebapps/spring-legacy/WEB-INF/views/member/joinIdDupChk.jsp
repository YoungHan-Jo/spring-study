<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2>아이디 중복확인</h2>
<hr>
<!-- count 1: 아이디 중복, 0: 아이디 중복아님 -->
<!-- 비교 연산자 표현 : 
== "eq" equal
!= "ne" not equal 
> "gt" greater than
< "lt" less than
>= "ge" greater equal
<= "le" less equal -->
<c:choose>
	<c:when test="${ requestScope.count eq 1 }">  
		<p>아이디중복, 이미 사용중인 ID 입니다.</p>
	</c:when>
	<c:otherwise>
		<p>${ requestScope.id } 는 사용가능한 ID 입니다.</p>
	<button type="button" id="btnUseId">ID 사용</button>
	</c:otherwise>
</c:choose>

<form action="/member/joinIdDupChk" method="get" name="frm">
	<input type="text" name="id" value="${ requestScope.id }">
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