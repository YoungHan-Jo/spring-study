<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
<!-- String타입을 Date 타입으로 파싱 -->
	<!-- var 는 pageScope에 키-값 으로 저장됨 -->
<fmt:parseDate value="${ member.birthday }" pattern="yyyyMMdd" var="dateBirthday"/>
<!-- 패턴을 변화 -->
<fmt:formatDate value="${dateBirthday}" pattern="yyyy-MM-dd" var="strBirthday" />

<%

/* String birthday = memberVO.getBirthday(); // '20020101' -> '2002-01-01'

// String -> Date 객체 변환
SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

Date date = sdf.parse(birthday);

//Date 객체 - > String
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

String strBirthday = sdf2.format(date); */

%>	
	
<!DOCTYPE html>
<html lang="ko">

<head>
<jsp:include page="/WEB-INF/views/include/head.jsp" />
</head>

<body>

	<!-- Navbar area -->
	<jsp:include page="/WEB-INF/views/include/top.jsp" />
	<!-- end of Navbar area -->

	<!-- Page Layout here -->
	<div class="row container">

		<!-- left menu area -->
		<jsp:include page="/WEB-INF/views/include/left.jsp" />
		<!-- end of left menu area -->

		<div class="col s12 m8 l9">
			<!-- Teal page content  -->
			<div class="section">

				<!-- card panel -->
				<div class="card-panel">
					<div class="row">
						<div class="col s12" style="padding: 0 5%;">

							<h5>내정보 수정</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<form action="/member/modify" method="POST" id="frm">
								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">account_box</i> <input
											id="id" type="text" name='id' value="${ member.id }" readonly> <label
											for="id">아이디</label><!--${ requestScope.member.id } requestScope 생략가능 -->
									</div>
								</div>

								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">lock</i> <input id="passwd"
											type="password" name="passwd" class="validate"> <label
											for="passwd">비밀번호</label> <span class="helper-text"
											data-error="wrong" data-success="right">Helper text</span>
									</div>
								</div>

								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">person</i> <input id="name"
											type="text" name="name" value="${ member.name }" class="validate"> <label
											for="name">이름</label>
									</div>
								</div>

								<div class="row">
									<div class="input-field">
										<i class="material-icons prefix">event</i> <input type="date"
											id="birthday" name="birthday" value="${ strBirthday }"> <label for="birthday">생년월일</label>
									</div>
								</div>

								<div class="row">
									<div class="input-field">
										<i class="material-icons prefix">wc</i> <select name="gender">
											<!-- EL언어 3항연산자등 사용 가능, 노란줄 무시 -->
											<option value="" disabled ${ empty member.gender ? 'selected' : '' }>성별을 선택하세요.</option>
											<option value="M" ${ member.gender eq 'M' ? 'selected' : '' } >남자</option>
											<option value="F" ${ member.gender eq 'F' ? 'selected' : '' } >여자</option>
											<option value="N" ${ member.gender eq 'N' ? 'selected' : '' } >선택 안함</option>
										</select> <label>성별</label>
									</div>
								</div>

								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">mail</i> <input id="email"
											type="email" name="email" value="${ member.email }"  class="validate" > <label
											for="email">본인 확인 이메일</label>
									</div>
								</div>

								<p class="row center">
									알림 이메일 수신 : &nbsp;&nbsp; <label> <input
										name="recvEmail" value="Y" type="radio" ${ member.recvEmail eq 'Y' ? 'checked' : '' } /> <span>예</span>
									</label> &nbsp;&nbsp; <label> <input name="recvEmail" value="N"
										type="radio" ${ member.recvEmail eq 'N' ? 'checked' : '' } /> <span>아니오</span>
									</label>
								</p>

								<br>
								<div class="row center">
									<button class="btn waves-effect waves-light" type="submit">
										수정 <i class="material-icons right">create</i>
									</button>
									&nbsp;&nbsp;
									<button class="btn waves-effect waves-light" type="reset">
										초기화 <i class="material-icons right">clear</i>
									</button>
								</div>
							</form>

						</div>
					</div>
				</div>
				<!-- end of card panel -->

			</div>
		</div>

	</div>

	<!-- footer area -->
	<jsp:include page="/WEB-INF/views/include/bottom.jsp" />
	<!-- end of footer area -->


	<!--  Scripts-->
	<jsp:include page="/WEB-INF/views/include/commonJs.jsp" />
	<script>
	
	</script>
</body>

</html>