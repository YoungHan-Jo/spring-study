<%@page import="com.example.domain.AttachVO"%>
<%@page import="java.util.List"%>
<%@page import="com.example.repository.AttachDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.example.domain.BoardVO"%>
<%@page import="com.example.repository.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
String id = (String) session.getAttribute("id");
%>
<%
// 상세보기 글 번호 파라미터값 가져오기
//location.href='/board/boardContent.jsp?num=<%=boardVO.getNum()
int num = Integer.parseInt(request.getParameter("num"));

String pageNum = request.getParameter("pageNum");

//DAO객체 준비
BoardDAO boardDAO = BoardDAO.getInstance();
AttachDAO attachDAO = AttachDAO.getInstance();

//조회수 1증가
boardDAO.updateReadcount(num);

// 상세보기 할 글 한개 가져오기
BoardVO boardVO = boardDAO.getBoardByNum(num);

// 글 작성 날짜형식
SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

// 첨부파일 정보리스트 가져오기
List<AttachVO> attachList = attachDAO.getAttachesByBno(num);


%>
<!DOCTYPE html>
<html lang="ko">

<head>
<jsp:include page="/include/head.jsp" />
<style>
time.comment-date {
	font-size: 13px;
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

							<h5>게시판 상세보기</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<table class="striped" id="boardList">
								<tr>
									<th class="center-align">제목</th>
									<td colspan="5"><%=boardVO.getSubject()%></td>
								</tr>
								<tr>
									<th class="center-align">작성자</th>
									<td><%=boardVO.getMid()%></td>
									<th class="center-align">작성일</th>
									<td><%=sdf.format(boardVO.getRegDate())%></td>
									<th class="center-align">조회수</th>
									<td><%=boardVO.getReadcount()%></td>
								</tr>
								<tr>
									<th class="center-align">추천</th>
									<td class="blue-text">264</td>
									<th class="center-align">비추천</th>
									<td class="red-text">7</td>
									<th class="center-align">댓글</th>
									<td>70</td>
								</tr>
								<tr>
									<th class="center-align">내용</th>
									<td colspan="5"><pre>
									<%=boardVO.getContent()%>
									</pre></td>
								</tr>
								<tr>
									<th class="center-align">첨부파일</th>
									<td colspan="5">
										<%
										if(attachList.size() > 0){ // 첨부 파일이 있으면
											%>
											<ul>
											<%
											for(AttachVO attach : attachList){
												if(attach.getFiletype().equals("O")){ // 일반 파일
													//파일 이름 경로
													String fileCallPath = attach.getUploadpath() + "/" + attach.getFilename();
													%>
													<li>
														<a href="/board/download.jsp?fileName=<%=fileCallPath %>">
															<i class="material-icons">file_present</i>
															<%=attach.getFilename() %>
														</a>
													</li>
													<%	
												}else if(attach.getFiletype().equals("I")) { // 이미지파일
					                   				// 썸네일 이미지 경로
					                   				String fileCallPath = attach.getUploadpath() + "/s_" + attach.getFilename();
					                   				// 원본 이미지 경로
					                   				String fileCallPathOrigin = attach.getUploadpath() + "/" + attach.getFilename();
					                   				%>
					                       			<li>
					                       				<a href="/board/download.jsp?fileName=<%=fileCallPathOrigin %>">
					                       					<img src="/board/display.jsp?fileName=<%=fileCallPath %>">
					                       				</a>
					                       			</li>
					                       			<%
												}
	
											}
											%>
											</ul>
											<%

										}else{ // 첨부파일이 없으면
											%>
											첨부파일 없음
											<%
										}
		
										%>
									
									</td>
								</tr>
							</table>


							<div class="section">
								<div class="row">
									<div class="col s12 right-align">

										<%
										if (id != null) {
										
											if(id.equals(boardVO.getMid())){ //로그인
												%>
												<a class="btn waves-effect waves-light" href="/board/boardModify.jsp?num=<%=boardVO.getNum() %>&pageNum=<%=pageNum %>"> <i
												class="material-icons left">edit</i>글수정
												</a> 
												<a class="btn waves-effect waves-light" onclick="remove(event)"> <i
													class="material-icons left">delete</i>글삭제
												</a>
												<%
											}
											%>
											<a class="btn waves-effect waves-light" 
											href="/board/replyWrite.jsp?reRef=<%=boardVO.getReRef() %>&reLev=<%=boardVO.getReLev()%>&reSeq=<%=boardVO.getReSeq()%>&pageNum=<%=pageNum %>"> 
											<i class="material-icons left">reply</i>답글
											</a>

										<%
										}
										%>

										<a class="btn waves-effect waves-light"
											href="/board/boardList.jsp?pageNum=<%=pageNum%>"> <i
											class="material-icons left">list</i>글목록
										</a>
									</div>
								</div>
							</div>


							<!-- comment area -->
							<div id="comment" class="section">
								<div class="row" style="margin-left: 0px;">
									<div class="col s12">
										<i class="material-icons" style="font-size: 18px;">forum</i> <span
											style="font-size: 18px;">댓글</span>
									</div>
								</div>

								<ul class="collection">
									<li class="collection-item avatar"><img
										src="/resources/images/yuna.jpg" class="circle"> <span
										class="title"><b>홍길동</b> (aaa)</span>
										<p>
											First Line <br> Second Line
										</p> <span class="secondary-content"> <time
												class="grey-text comment-date">2021-07-23 15:07:24</time> <span
											class="grey-text text-lighten-1">|</span> <a href="#!">삭제</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">수정</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">답글</a>
									</span></li>
									<li class="collection-item avatar" style="margin-left: 40px;">
										<img src="/resources/images/yuna.jpg" class="circle"> <span
										class="title"><b>홍길동</b> (aaa)</span>
										<p>
											First Line <br> Second Line
										</p> <span class="secondary-content"> <time
												class="grey-text comment-date">2021-07-23 15:07:24</time> <span
											class="grey-text text-lighten-1">|</span> <a href="#!">삭제</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">수정</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">답글</a>
									</span>
									</li>
									<li class="collection-item avatar" style="margin-left: 80px;">
										<img src="/resources/images/yuna.jpg" class="circle"> <span
										class="title"><b>홍길동</b> (aaa)</span>
										<p>
											First Line <br> Second Line
										</p> <span class="secondary-content"> <time
												class="grey-text comment-date">2021-07-23 15:07:24</time> <span
											class="grey-text text-lighten-1">|</span> <a href="#!">삭제</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">수정</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">답글</a>
									</span>
									</li>
									<li class="collection-item avatar"><img
										src="/resources/images/yuna.jpg" class="circle"> <span
										class="title"><b>홍길동</b> (aaa)</span>
										<p>
											First Line <br> Second Line
										</p> <span class="secondary-content"> <time
												class="grey-text comment-date">2021-07-23 15:07:24</time> <span
											class="grey-text text-lighten-1">|</span> <a href="#!">삭제</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">수정</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">답글</a>
									</span></li>



									<li class="collection-item avatar" style="margin-left: 40px;">
										<img src="../resources/images/yuna.jpg" class="circle">
										<span class="title"><b>홍길동</b> (aaa)</span>
										<p>
											First Line <br> Second Line
										</p> <span class="secondary-content"> <time
												class="grey-text comment-date">2021-07-23 15:07:24</time> <span
											class="grey-text text-lighten-1">|</span> <a href="#!">삭제</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">수정</a>
											<span class="grey-text text-lighten-1">|</span> <a href="#!">답글</a>
									</span>
									</li>
									<!-- modify comment -->
									<li class="collection-item" style="margin-left: 40px;">
										<div class="row" style="margin-left: 0px;">
											<div class="col s12">
												<span style="font-size: 15px;">댓글 수정</span>
											</div>
											<div class="col s12">
												<form action="" method="POST">
													<div class="row">
														<div class="col s12 m9">
															<textarea class="materialize-textarea">First Line Second Line</textarea>
														</div>
														<div class="col s12 m3">
															<button type="submit"
																class="btn-large waves-effect waves-light">수정</button>
														</div>
													</div>
												</form>
											</div>
										</div>
									</li>
									<!-- end of modify comment -->
									<!-- write reply comment -->
									<li class="collection-item" style="margin-left: 80px;">
										<div class="row" style="margin-left: 0px;">
											<div class="col s12">
												<span style="font-size: 15px;">답댓글 작성</span>
											</div>
											<div class="col s12">
												<form action="" method="POST">
													<div class="row">
														<div class="col s12 m9">
															<textarea class="materialize-textarea"></textarea>
														</div>
														<div class="col s12 m3">
															<button type="submit"
																class="btn-large waves-effect waves-light">작성</button>
														</div>
													</div>
												</form>
											</div>
										</div>
									</li>
									<!-- end of write reply comment -->
								</ul>

								<div class="divider" style="margin: 30px 0;"></div>

								<!-- write new comment -->
								<div class="row" style="margin-left: 0px;">
									<div class="col s12">
										<span style="font-size: 18px;">새댓글 작성</span>
									</div>
									<div class="col s12">
										<form action="" method="POST">
											<div class="row">
												<div class="col s12 m9">
													<textarea class="materialize-textarea"></textarea>
												</div>
												<div class="col s12 m3">
													<button type="submit"
														class="btn-large waves-effect waves-light">작성</button>
												</div>
											</div>
										</form>
									</div>
								</div>
								<!-- end of write new comment -->

							</div>
							<!-- end of comment area -->

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
	
	<script>
	//글 삭제 버튼을 클릭하면 호출되는 함수
	function remove(event){
		event.preventDefault();
		
		var isRemove = confirm('게시글을 삭제하시겠습니까?');
		if(isRemove == true){
			location.href = '/board/boardRemove.jsp?num=<%=boardVO.getNum() %>&pageNum=<%=pageNum %>';
		}
	}
	
	</script>


</body>

</html>