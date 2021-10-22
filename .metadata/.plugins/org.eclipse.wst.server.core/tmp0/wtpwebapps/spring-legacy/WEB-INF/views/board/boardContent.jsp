<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- functions태그는 String, Array, Collection 객체에 관한 fn 태그 선언 -->

<!DOCTYPE html>
<html lang="ko">

<head>
<jsp:include page="/WEB-INF/views/include/head.jsp" />
<style>
time.comment-date {
	font-size: 13px;
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

							<h5>게시판 상세보기</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<table class="striped" id="boardList">
								<tr>
									<th class="center-align">제목</th>
									<td colspan="5">${ board.subject }</td>
								</tr>
								<tr>
									<th class="center-align">작성자</th>
									<td>${ board.mid }</td>
									<th class="center-align">작성일</th>
									<td><fmt:formatDate value="${ board.regDate }"
											pattern="yyyy.MM.dd HH:mm:ss" /></td>
									<th class="center-align">조회수</th>
									<td>${ board.readcount }</td>
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
									${ board.content }
									</pre></td>
								</tr>
								<tr>
									<th class="center-align">첨부파일</th>
									<!-- test="${ attachCount gt 0 }" -->
									<td colspan="5"><c:choose>

											<c:when test="${ fn:length(attachList) gt 0 }">
												<c:forEach var="attach" items="${ attachList }">

													<c:if test="${ attach.filetype eq 'O' }">
														<!-- 일반파일 -->
														<!-- 다운로드할 일반파일 경로 변수 만들기 pageScope로 저장 -->
														<c:set var="fileCallPath"
															value="${ attach.uploadpath }/${ attach.uuid }_${ attach.filename }" />
														<li><a href="/download?fileName=${ fileCallPath }">
																<i class="material-icons">file_present</i> ${ attach.filename }
														</a></li>
													</c:if>
													<c:if test="${ attach.filetype eq 'I' }">
														<!-- 이미지파일 -->
														<c:set var="fileCallPath"
															value="${ attach.uploadpath }/s_${ attach.uuid }_${ attach.filename }" />
														<c:set var="fileCallPathOrigin"
															value="${ attach.uploadpath }/${ attach.uuid }_${ attach.filename }" />
														<li><a
															href="/download?fileName=${ fileCallPathOrigin }"> <img
																src="/display?fileName=${ fileCallPath }" style="width: 50px">
														</a></li>
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>
												첨부파일 없음
											</c:otherwise>
										</c:choose></td>
								</tr>
							</table>

							<div class="section">
								<div class="row">
									<div class="col s12 right-align">

										<!-- 로그인 했을 때 -->
										<c:if test="${ not empty sessionScope.id }">
											<c:if test="${ sessionScope.id eq board.mid }">
												<a class="btn waves-effect waves-light"
													href="/board/modify?num=${ board.num }&pageNum=${ pageNum }">
													<i class="material-icons left">edit</i>글수정
												</a>
												<a class="btn waves-effect waves-light"
													onclick="remove(event)"> <i class="material-icons left">delete</i>글삭제
												</a>
											</c:if>

											<a class="btn waves-effect waves-light"
												href="/board/reply?reRef=${ board.reRef }&reLev=${ board.reLev }&reSeq=${ board.reSeq }&pageNum=${ pageNum }">
												<i class="material-icons left">reply</i>답글
											</a>
										</c:if>

										<a class="btn waves-effect waves-light"
											href="/board/list?pageNum=${ pageNum }"> <i
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
	<jsp:include page="/WEB-INF/views/include/bottom.jsp" />
	<!-- end of footer area -->

	<script>
		//글 삭제 버튼을 클릭하면 호출되는 함수
		function remove(event) {
			event.preventDefault();

			var isRemove = confirm('게시글을 삭제하시겠습니까?');
			if (isRemove == true) {
				/* 스크립트 영역에서도 el언어 사용가능, el언어는 jsp 읽을 때 제일 처음으로 동작함 */
				location.href = '/board/remove?num=${ board.num }&pageNum=${ pageNum }';
			}
		}
	</script>


</body>

</html>