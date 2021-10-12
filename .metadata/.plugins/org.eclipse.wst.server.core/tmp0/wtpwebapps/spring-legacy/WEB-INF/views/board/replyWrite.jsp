<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
String id = (String) session.getAttribute("id");
%>

<!-- reRef reLev reSeq pageNum 파라미터 가져오기 -->
<%
String reRef = request.getParameter("reRef");
String reLev = request.getParameter("reLev");
String reSeq = request.getParameter("reSeq");

String pageNum = request.getParameter("pageNum");
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

							<h5>게시판 답글쓰기</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<form action="/board/replyWritePro.jsp" method="POST" enctype="multipart/form-data">
							
								<!-- multipart/form-data 로 하면 뭉쳐져서 넘어가기 때문에
									input:hidden 속성으로 name/ value 로 쿠키 넘기듯이 파라미터 넘기기
									받을때 multi.requestParameter로 받음 -->
								<input type="hidden" name="pageNum" value="<%=pageNum %>">
								<input type="hidden" name="reRef" value="<%=reRef %>">
								<input type="hidden" name="reLev" value="<%=reLev %>">
								<input type="hidden" name="reSeq" value="<%=reSeq %>">
								
								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">account_box</i> <input
											id="id" type="text" name="id" value="<%=id%>" readonly>
										<label for="id">아이디</label>
									</div>
								</div>

								<div class="row">
									<div class="input-field">
										<i class="material-icons prefix">subtitles</i> <input
											type="text" id="title" class="validate" name="subject">
										<label for="title">제목</label>
									</div>
								</div>
								<div class="row">
									<div class="input-field">
										<i class="material-icons prefix">subject</i>
										<textarea id="textarea1" class="materialize-textarea"
											name="content"></textarea>
										<label for="textarea1">내용</label>
									</div>
								</div>

								<div class="row">
									<div class="col s12">
										<button type="button"
											class="btn-small waves-effect waves-light" id="btnAddFile">파일
											추가</button>
									</div>
								</div>


								<div class="row" id="fileBox">
									<div class="col s12">
										<input type="file" name="file0">
										<button class="waves-effect waves-light btn-small file-delete">
											<i class="material-icons">clear</i>
										</button>
									</div>
								</div>

								<br>
								<div class="row center">
									<button class="btn waves-effect waves-light" type="submit">
										답글등록 <i class="material-icons right">create</i>
									</button>
									&nbsp;&nbsp;
									<button class="btn waves-effect waves-light" type="reset">
										초기화 <i class="material-icons right">clear</i>
									</button>
									&nbsp;&nbsp;
									<button class="btn waves-effect waves-light" type="button"
										onclick="location.href = '/board/boardList.jsp'">
										글목록 <i class="material-icons right">list</i>
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
	<!-- end of footer area -


  <!--  Scripts-->
	<jsp:include page="/WEB-INF/views/include/commonJs.jsp" />
	<script>
	
	var fileIndex = 1;
	var fileCount = 1;
		
	$('#btnAddFile').on('click',function(){
		if(fileCount >= 5){
			alert('첨부파일은 최대5개까지 가능합니다.');
			return;
		}
		
		var str = `<div class="col s12">
						<input type="file" name="file\${fileIndex}"> 
						<button class="waves-effect waves-light btn-small file-delete"><i class="material-icons">clear</i></button>
					</div>`;
					
		$('#fileBox').append(str);
		
		fileIndex++;
		fileCount++;
	
	})
	
	// 동적 이벤트 연결 (이벤트 등록을 이미 존재하는 요소에게 위임하는 방식)
	$('#fileBox').on('click','button.file-delete',function(){
		$(this).closest('div').remove();
		fileCount--;
	})
	

	</script>
</body>

</html>