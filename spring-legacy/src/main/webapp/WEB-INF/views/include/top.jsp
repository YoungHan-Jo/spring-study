<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%
// 프로그램 하나당 1개, 싱글톤같이 하나만 존재함. object형식이라서 다운캐스팅 해야함
/* application.setAttribute("키", "값");
String value = (String) application.getAttribute("키");

session.setAttribute("키", "값");
String value2 = (String) session.getAttribute("키");

request.setAttribute("키", "값"); // 요청을 받을때 생겨서 응답을 줄 때(화면 만들때) 자동 파기됨
String value3 = (String) request.getAttribute("키"); // 서버 내부적으로 저장했다가 꺼내고 싶을때 사용
request.getParameter("passwd"); // <- 이거랑은 전혀 다름. 외부 데이터를 가져올때 사용.

pageContext.setAttribute("키", "값");
String value4 = (String) pageContext.getAttribute("키"); */
%>

<!-- 
ㅁ EL(Expression language) : JSP에서 데이터 출력위주로 사용되는 표현언어
ex) 세션값을 꺼내서 입력해야할 부분에 ${ sessionScope.id } 로 입력. null값이면 공백으로 나옴

ㅁ 톰캣(WAS)의 영역객체 4가지(Map 컬렉션 처럼 키-값 쌍으로 데이터를 관리할 수 있는 객체) 
 application : 웹 프로그램 한 개 당 유지되는 객체. EL표현은 ${ applicationScope }
 session : 사용자 한 명 당 유지되는 객체.	EL표현은 ${ sessionScope }
 request : 사용자 요청 한 개 당 유지되는 객체.	EL표현은 ${ requestScope }
 pageContext : JSP 페이지 한 개 처리할 동안 유지됨.	EL표현은 ${ pageScope }
  
 ㅁ 영역(scope)객체의 수명주기
  applicationScope > sessionScope > requestScope > pageScope
  
 ㅁ EL언어에서 영역(scope)객체 검색순서 ${ sessionScope.id } 영역을 명시하지 않을때	${ id }
 pageScope -> requestScope -> sessionScope -> applicationScope
  
 -->

<!-- navbar -->
<nav class="nav-extended light-blue lighten-1" role="navigation">
	<div class="container right-align"
		style="font-size: 13px; height: 40px;">

		<c:choose>
			<c:when test="${ empty sessionScope.id }">
				<a href="/member/login">로그인</a>&nbsp; | &nbsp; <a
					href="/member/join">회원가입</a>
			</c:when>
			<c:otherwise>
				<span>${ sessionScope.id }님</span>
				<a href="/member/logout">로그아웃</a>
			</c:otherwise>
		</c:choose>
	</div>

	<div class="nav-wrapper container">
		<a id="logo-container" href="/index" class="brand-logo"><i
			class="material-icons">android</i>Logo</a>

		<ul class="right hide-on-med-and-down">

			<c:if test="${ not empty sessionScope.id }">
				<li><a href="#!" class="dropdown-trigger"
					data-target="dropdownMember">내정보<i class="material-icons right">arrow_drop_down</i></a></li>
			</c:if>

			<!-- 로그인 관계없이 보이는 메뉴 -->
			<li><a class="dropdown-trigger" data-target="dropdownBoard">게시판<i
					class="material-icons right">arrow_drop_down</i></a></li>
			<li><a href="#!" class="dropdown-trigger"
				data-target="dropdownChat">채팅<i class="material-icons right">arrow_drop_down</i></a></li>
		</ul>
	</div>
</nav>
<!-- end of navbar -->


<!-- navbar dropdown structure -->
<ul id="dropdownMember" class="dropdown-content">
	<!-- 내정보 서브메뉴 -->
	<li><a href="/member/passwd">비밀번호 변경</a></li>
	<li><a href="/member/modify">내정보 수정</a></li>
	<li><a href="/member/remove">회원탈퇴</a></li>
</ul>
<ul id="dropdownBoard" class="dropdown-content">
	<!-- 게시판 서브메뉴 -->
	<li><a href="/board/list">게시판</a></li>
	<li><a href="">자료실</a></li>
</ul>
<ul id="dropdownChat" class="dropdown-content">
	<!-- 채팅 서브메뉴 -->
	<li><a href="#test1">간단한 채팅</a></li>
	<li><a href="">채팅방 목록</a></li>
</ul>
<!-- end of navbar dropdown structure -->

