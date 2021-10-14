<%@page import="com.example.domain.PageDTO"%>
<%@page import="com.example.domain.Criteria"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.example.domain.BoardVO"%>
<%@page import="java.util.List"%>
<%@page import="com.example.repository.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
String id = (String) session.getAttribute("id");
%>

<%
// 글목록 가져오기 조건객체 준비
Criteria cri = new Criteria(); // 기본값 1페이지 10개

// 요청 페이지번호 가져오기
String strPageNum = request.getParameter("pageNum");
if (strPageNum != null) { // 요청페이지번호 있으면 cri에 값 설정
	cri.setPageNum(Integer.parseInt(strPageNum));
}

//요청 글 개수 가져오기
String strAmount = request.getParameter("amount");
if (strAmount != null) {
	cri.setAmount(Integer.parseInt(strAmount));
}

//요청 검색유형 가져오기
String type = request.getParameter("type"); // null or ""
if(type != null && type.length()>0){
	cri.setType(type);
}

//요청 검색유형 가져오기
String keyword = request.getParameter("keyword"); // null or ""
if(type != null && type.length()>0){
	cri.setKeyword(keyword);
}
//=================요청 파라미터 값 가져와서 Criteriai에 저장 완료=================

//DAO 객체준비
BoardDAO boardDAO = BoardDAO.getInstance();

// board 테이블에서 전체글 리스트로 가져오기
List<BoardVO> list = boardDAO.getBoards(cri);

// 검색어 있으면 검색 결과 or 전체 글 개수 가져오기
int totalCount = boardDAO.getCountBySearch(cri);

// 페이지블록 정보 객체준비. 필요한 정보를 생성자로 전달.
PageDTO pageDTO = new PageDTO(cri, totalCount);
%>

<!DOCTYPE html>
<html lang="ko">

<head>
<jsp:include page="/include/head.jsp" />
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
	<jsp:include page="/include/top.jsp" />
	<!-- end of Navbar area -->


	<!-- Page Layout here -->
	<div class="row container">

		<!-- left menu area -->
		<jsp:include page="/include/left.jsp" />
		<!-- end of left menu area -->

		<div class="col s12 m8 l9">
			<!-- page content  -->
			<div class="section">

				<div class="card-panel">
					<div class="row">
						<div class="col s12" style="padding: 0 2%;">

							<h5>일반 게시판</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<%
							if (id != null) {
							%>
							<div class="row">
								<a
									href="/board/boardWrite.jsp?pageNum=<%=pageDTO.getCri().getPageNum()%>"
									class="btn waves-effect waves-light right"> <i
									class="material-icons left">create</i>새글쓰기
								</a>
							</div>
							<%
							}
							%>

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

									<%
									if (pageDTO.getTotalCount() > 0) {

										SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

										for (BoardVO boardVO : list) {
											String strRegDate = sdf.format(boardVO.getRegDate());
									%>
									<tr
										onclick="location.href='/board/boardContent.jsp?num=<%=boardVO.getNum()%>&pageNum=<%=cri.getPageNum()%>'">
										<td class="center-align"><%=boardVO.getNum()%></td>
										<td>
										<%
										if (boardVO.getReLev() > 0){ // 답글이면
											%>
											<span class="reply-level" style="width: <%=boardVO.getReLev()*15 %>px"></span>
											<i class="material-icons">subdirectory_arrow_right</i>
											<%	
										}
										%>
											
											<%=boardVO.getSubject()%>
										</td>
										<td class="center-align"><%=boardVO.getMid()%></td>
										<td class="center-align"><%=strRegDate%></td>
										<td class="center-align"><%=boardVO.getReadcount()%></td>
									</tr>
									<%
									}

									} else { // 글이 없을 때
									%>
									<tr>
										<td class="center-align" colspan="5">게시글이 없습니다.</td>
									</tr>
									<%
									}
									%>
								</tbody>
							</table>

							<br>
							<ul class="pagination center">
								<%
								// 이전 버튼
								//불리언은 is변수명 가능
								if (pageDTO.isPrev()) {
								%>
								<li class="waves-effect"><a
									href="/board/boardList.jsp?pageNum=<%=pageDTO.getStartPage() - 1%>&type=<%=pageDTO.getCri().getType() %>&keyword=<%=pageDTO.getCri().getKeyword() %>#board"><i
										class="material-icons">chevron_left</i></a></li>
								<%
								}
								%>
								<%
								//페이지블록 내 최대 5개씩 출력
								for (int i = pageDTO.getStartPage(); i <= pageDTO.getEndPage(); ++i) {
								%>
								<li
									class="waves-effect <%=pageDTO.getCri().getPageNum() == i ? "active" : ""%>"><a
									href="/board/boardList.jsp?pageNum=<%=i%>&type=<%=pageDTO.getCri().getType() %>&keyword=<%=pageDTO.getCri().getKeyword() %>#board"><%=i%></a></li>
								<%
								}
								%>
								<%
								// 다음 버튼
								//불리언은 is변수명 가능
								if (pageDTO.isNext()) {
								%>
								<li class="waves-effect"><a
									href="/board/boardList.jsp?pageNum=<%=pageDTO.getEndPage() + 1%>&type=<%=pageDTO.getCri().getType() %>&keyword=<%=pageDTO.getCri().getKeyword() %>#board"><i
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
												<option value="subject" <%=(pageDTO.getCri().getType().equals("subject")) ? "selected" : "" %> >제목</option>
												<option value="content" <%=(pageDTO.getCri().getType().equals("content")) ? "selected" : "" %>>내용</option>
												<option value="mid" <%=(pageDTO.getCri().getType().equals("mid")) ? "selected" : "" %>>작성자</option>
											</select> <label>검색 조건</label>
										</div>
									</div>

									<div class="col s12 l4">
										<!-- AutoComplete -->
										<div class="input-field">
											<i class="material-icons prefix">search</i> <input
												type="text" id="autocomplete-input" class="autocomplete"
												name="keyword" value="<%=pageDTO.getCri().getKeyword() %>"> 
												<label for="autocomplete-input">검색어</label>
										</div>
										<!-- end of AutoComplete -->
									</div>

									<div class="col s12 l4">
										<button type="button" id="btnSearch" class="waves-effect waves-light btn-large">
										<i class="material-icons left">search</i>검색</button>
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
	<jsp:include page="/include/bottom.jsp" />
	<!-- end of footer area -->

	<!--  Scripts-->
	<jsp:include page="/include/commonJs.jsp" />
	<script>
		$('button#btnSearch').on('click',function(){
			
			// 폼 태그의 쿼리들을 문자열로 한번에 가져옴. // type=subject&keyword=답글
			var query = $('#frm').serialize();
			
			location.href = '/board/boardList.jsp?'+query+'#board';
		})
	</script>
</body>

</html>