<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<!DOCTYPE html>
<html lang="ko">

<head>
<jsp:include page="/WEB-INF/views/include/head.jsp" />
<style>
table tbody tr {
	cursor: pointer;
}

table#board span.reply-level {
	display: inline-block;
}
</style>
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
			<!-- page content  -->
			<div class="section">

				<div class="card-panel">
					<div class="row">
						<div class="col s12" style="padding: 0 2%;">

							<h5>일반 게시판 (글개수: ${ pageMaker.totalCount })</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<c:if test="${ not empty sessionScope.id }">
								<div class="row">
									<!-- pageDTO.getCri().getPageNum 랑 같음 -->
									<a href="/board/write?pageNum=${ pageMaker.cri.pageNum }"
										class="btn waves-effect waves-light right"> <i
										class="material-icons left">create</i>새글쓰기
									</a>
								</div>
							</c:if>

							<table class="highlight responsive-table" id="board">
								<thead>
									<tr>
										<th class="center-align">번호</th>
										<th class="center-align">제목</th>
										<th class="center-align">작성자</th>
										<th class="center-align">작성일</th>
										<th class="center-align">조회수</th>
									</tr>
								</thead>

								<tbody>
									<c:choose>
										<c:when test="${ pageMaker.totalCount gt 0 }">
											<!-- foreach문 var: 하나하나의 이름, items: 리스트 -->
											<!-- var는 pageScope에 저장됨 -->
											<c:forEach var="board" items="${ boardList }">
												<tr
													onclick="location.href='/board/content?num=${ board.num }&pageNum=${ pageMaker.cri.pageNum }'">
													<td class="center-align">${ board.num }</td>
													<td><c:if test="${ board.reLev gt 0 }">
															<span class="reply-level"
																style="width: ${ board.reLev * 15 }px"></span>
															<i class="material-icons">subdirectory_arrow_right</i>
														</c:if> ${ board.subject }</td>
													<td class="center-align">${ board.mid }</td>
													<!-- fmt에서 var 속성을 입력하지 않으면 그대로 출력함 -->
													<td class="center-align"><fmt:formatDate
															value="${ board.regDate }" pattern="yyyy.MM.dd" /></td>
													<td class="center-align">${ board.readcount }</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="center-align" colspan="5">게시글이 없습니다.</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>

							<br>
							<ul class="pagination center">
								<!-- 이전 버튼 -->
								<!-- isPrev 이라서 그냥 get이랑 똑같은 방식으로 생략 하면 됨 -->
								<!-- 자체가 if 문이라서 eq true 생략가능 -->
								<c:if test="${ pageMaker.prev eq true }">
									<li class="waves-effect"><a
										href="/board/list?pageNum=${ pageMaker.startPage - 1 }&type=${ pageMaker.cri.type }&keyword=${ pageMaker.cri.keyword }#board"><i
											class="material-icons">chevron_left</i></a></li>
								</c:if>

								<%
								//페이지블록 내 최대 5개씩 출력
								for (int i = pageDTO.getStartPage(); i <= pageDTO.getEndPage(); ++i) {
								%>
								<li
									class="waves-effect <%=pageDTO.getCri().getPageNum() == i ? "active" : ""%>"><a
									href="/board/boardList.jsp?pageNum=<%=i%>&type=<%=pageDTO.getCri().getType()%>&keyword=<%=pageDTO.getCri().getKeyword()%>#board"><%=i%></a></li>
								<%
								}
								%>
								<%
								// 다음 버튼
								//불리언은 is변수명 가능
								if (pageDTO.isNext()) {
								%>
								<li class="waves-effect"><a
									href="/board/boardList.jsp?pageNum=<%=pageDTO.getEndPage() + 1%>&type=<%=pageDTO.getCri().getType()%>&keyword=<%=pageDTO.getCri().getKeyword()%>#board"><i
										class="material-icons">chevron_right</i></a></li>
								<%
								}
								%>


							</ul>

							<div class="divider" style="margin: 30px 0;"></div>

							<form action="/board/boardList.jsp" method="GET" id="frm">
								<div class="row">
									<div class="col s12 l4">
										<div class="input-field">
											<i class="material-icons prefix">find_in_page</i> <select
												name="type">
												<option value="" disabled selected>--</option>
												<option value="subject"
													<%=(pageDTO.getCri().getType().equals("subject")) ? "selected" : ""%>>제목</option>
												<option value="content"
													<%=(pageDTO.getCri().getType().equals("content")) ? "selected" : ""%>>내용</option>
												<option value="mid"
													<%=(pageDTO.getCri().getType().equals("mid")) ? "selected" : ""%>>작성자</option>
											</select> <label>검색 조건</label>
										</div>
									</div>

									<div class="col s12 l4">
										<!-- AutoComplete -->
										<div class="input-field">
											<i class="material-icons prefix">search</i> <input
												type="text" id="autocomplete-input" class="autocomplete"
												name="keyword" value="<%=pageDTO.getCri().getKeyword()%>">
											<label for="autocomplete-input">검색어</label>
										</div>
										<!-- end of AutoComplete -->
									</div>

									<div class="col s12 l4">
										<button type="button" id="btnSearch"
											class="waves-effect waves-light btn-large">
											<i class="material-icons left">search</i>검색
										</button>
									</div>
								</div>
							</form>

						</div>
					</div>
				</div>
				<!-- end of card-panel -->

			</div>
		</div>

	</div>

	<!-- footer area -->
	<jsp:include page="/WEB-INF/views/include/bottom.jsp" />
	<!-- end of footer area -->

	<!--  Scripts-->
	<jsp:include page="/WEB-INF/views/include/commonJs.jsp" />
	<script>
		$('button#btnSearch').on('click', function() {

			// 폼 태그의 쿼리들을 문자열로 한번에 가져옴. // type=subject&keyword=답글
			var query = $('#frm').serialize();

			location.href = '/board/boardList.jsp?' + query + '#board';
		})
	</script>
</body>

</html>