<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
span.file-delete {
	background-color: yellow;
	color: red;
	cursor: pointer;
	border: 1px solid black;
	border-radius: 5px;
	padding: 2px;
	margin-left: 10px;
	display: inline-block;
}
</style>
</head>
<body>

	<h1>새 글 등록</h1>
	<hr>
	<!-- multipart -> 파일을 전송 할 수 있도록 -->
	<form id="frm" enctype="multipart/form-data">
		<table border=1>
			<tr>
				<th>작성자 아이디</th>
				<td><input type="text" name="id" id="id"></td>
			</tr>
			<tr>
				<th>글제목</th>
				<td><input type="text" name="subject" id="subject"></td>
			</tr>
			<tr>
				<th>글 내용</th>
				<td><textarea rows="13" cols="40" name="content" id="content"></textarea>
				</td>
			</tr>
			<tr>
				<th>파일</th>
				<td>
					<button type="button" id="btnAddFile">첨부파일 추가</button>
					<div id="fileBox">
						<div>
							<input type="file" name="file0"><span class="file-delete">❌</span>
						</div>
					</div>
					<div id="uploadResult">
						<ul>

						</ul>
					</div>
				</td>
			</tr>
		</table>
		<br>
		<button type="button" id="btnWrite">글쓰기</button>
	</form>





	<script src="/resources/js/jquery-3.6.0.js"></script>
	<script src="/resources/js/jquery.serializeObject.min.js"></script>
	<script>

	
	var fileIndex = 1;
	var fileCount = 1;
	
	$('#btnAddFile').on('click',function(){
		
		if(fileCount >= 5){
			alert('파일은 5개까지만 첨부할수있습니다.');
			return;
		}
		
		var str = `
			<div>
			<input type="file" name="file\${fileIndex}"><span
				class="file-delete">❌</span>
			</div>
			`;
			
		$('div#fileBox').append(str)
		
		fileIndex++;
		fileCount++;
	});
	
	//동적 이벤트 연결 ( 이벤트 위임 방식)
	$('#fileBox').on('click','span.file-delete', function(){
		//$(this).closest('div').remove();
		$(this).parent().remove();
		
		fileCount--;
		
	});
	
	var cloneObj = $('div#fileBox').clone(); // 하위항목까지 완전히 복사시키기/ deepCopy
	
	// 글쓰기 버튼 클릭했을 때
	$('#btnWrite').on('click',function(){
		
		var form = $('form#frm')[0];
		console.log(form);
		console.log(typeof form);
		
		var formData = new FormData(form); // 쿼리String 타입으로 넘겨줌
		console.log(formData);
		console.log(typeof formData);
		
		$.ajax({
			url: '/api/boards/new',
			//enctype: 'multipart/form-data', 본문 <form>에 입력했기때문에 여기서는 생략가능
			method: 'POST',
			data: formData,
			processData: false, // 파일전송시 false 설정
			contentType: false, // 파일전송시 false 설정
			success: function (data) {
				console.log(data);
				
				if(data.result == 'success'){
					alert('글쓰기 성공');
				}
				
				$('form#frm')[0].reset();
				$('div#fileBox').html(cloneObj.html()); // 파일첨부 부분 초기화 시키기
				
				showUploadedFile(data.attachList);
			} // success
			
		});
		
	});
	
	function showUploadedFile(attachList){
		
		var str = '';
		
		for(var attach of attachList){
			str += `
				<li>\${attach.filename}</li>
			`;
		}//for
		
		//$('div#uploadResult > ul').html(str); // 기존 내용 지우고 다시 쓰기
		$('div#uploadResult > ul').append(str); // 기존에 있던거에 추가
	}
	

</script>
</body>
</html>