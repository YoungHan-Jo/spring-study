<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

.img-box {
	height: 200px;
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

							<h5>갤러리 게시판</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<c:choose>
								<c:when test="${ pageMaker.totalCount gt 0 }">
									<!-- foreach문 var: 하나하나의 이름, items: 리스트 -->
									<!-- var는 pageScope에 저장됨 -->
									<c:forEach var="attach" items="${ attachList }">
										<div class="col s6 m4 l3 img-box">
											<c:set var="fileCallPath"
												value="${ attach.uploadpath }/s_${ attach.uuid }_${ attach.filename }" />
											<a href='/board/content?num=${ attach.bno }&pageNum=1'><img src="/display?fileName=${ fileCallPath }"
												style="width: 80%;" /></a>
										</div>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td class="center-align" colspan="5">갤러리 사진이 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
							<br>

							<div class="divider" style="margin: 30px 0;"></div>

						</div>
					</div>
				</div>
				<ul class="pagination center">
					<!-- 이전 버튼 -->
					<!-- isPrev 이라서 그냥 get이랑 똑같은 방식으로 생략 하면 됨 -->
					<!-- 자체가 if 문이라서 eq true 생략가능 -->
					<!-- 링크 뒤의 #board는 id가 board인 태그위치로 이동 -->
					<c:if test="${ pageMaker.prev eq true }">
						<li class="waves-effect"><a
							href="/gallery/list?pageNum=${ pageMaker.startPage - 1 }#board"><i
								class="material-icons">chevron_left</i></a></li>
					</c:if>

					<!-- 페이지 블록 내 최대 5개 페이지 출력 -->
					<!-- 기본 for문 begin:시작값, end:끝값, step:증감 -->
					<!-- var 는 pageScope에 저장됨 -->
					<c:forEach var="i" begin="${ pageMaker.startPage }"
						end="${ pageMaker.endPage }" step="1">
						<li
							class="waves-effect ${ pageMaker.cri.pageNum eq i ? 'active' : '' }"><a
							href="/gallery/list?pageNum=${ pageScope.i }#board">${ i }</a></li>
					</c:forEach>

					<!-- 다음 버튼 -->
					<c:if test="${ pageMaker.next eq true }">
						<li class="waves-effect"><a
							href="/gallery/list?pageNum=${ pageMaker.endPage + 1 }#board"><i
								class="material-icons">chevron_right</i></a></li>
					</c:if>
				</ul>


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

			location.href = '/board/list?' + query + '#board';
		})
	</script>
</body>

</html>