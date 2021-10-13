<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
span.delete-oldfile, span.delete-addfile {
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

	<h1>글 수정</h1>
	<hr>
	<!-- multipart -> 파일을 전송 할 수 있도록 -->
	<form id="frm" enctype="multipart/form-data">
		<table border=1>
			<tr>
				<th>글번호</th>
				<td><input type="number" name="num" id="bno"></td>
			</tr>
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
					<div id="oldFileBox"></div>
					<div id="newFileBox"></div>

				</td>
			</tr>
		</table>
		<br>
		<button type="button" id="btnModify">글수정</button>
	</form>

	<script src="/resources/js/jquery-3.6.0.js"></script>
	<script src="/resources/js/jquery.serializeObject.min.js"></script>

	<script>

	
	var fileIndex = 0;
	var fileCount = 0;
	
	
	$('#bno').on('change',function(){ // 값의 변화가 있을 때
		
		fileCount = 0;
		$('div#newFileBox').empty();
		$('div#oldFileBox').empty();
		
		var bno = $(this).val();
		console.log(bno);
		
		$.ajax({
			url: '/api/boards/' + bno,
			method: 'GET',
			success: function(data){
				console.log(data);
				console.log(typeof data);
				
				showData(data);
				
			}// success
		});
	});
	
	function showData(obj){
		
		// 게시글 정보 보이기
		var board = obj.board;
		
		$('input#id').val(board.mid);
		$('input#subject').val(board.subject);
		$('textarea#content').val(board.content);
		
		//첨부파일 정보 보이기
		var attachList = obj.attachList;
		
		fileCount = attachList.length;
		
		var str = '';
		
		for(var attach of attachList){
			str += `
				<input type="hidden" name="oldfile" value="\${attach.uuid}">
				<div>
					\${attach.filename}
					<span class="delete-oldfile">X</span>
				</div>
			`;
		}// for
		
		$('div#oldFileBox').html(str);
		
	} // showData
	
	$('#btnAddFile').on('click', function () {
		if (fileCount >= 5) {
			alert('첨부파일은 최대 5개 까지만 첨부할 수 있습니다.');
			return;
		}
		
		var str = `
			<div>
				<input type="file" name="file\${fileIndex}">
				<span class="delete-addfile">X</span>
			</div>
		`;
		
		$('#newFileBox').append(str);
		
		fileIndex++;
		fileCount++;
	});
	
	//동적 이벤트 연결 ( 이벤트 위임 방식)
	$('#newFileBox').on('click', 'span.delete-addfile', function () {
		//$(this).closest('div').remove();
		$(this).parent().remove();
		
		fileCount--;
	});
	
$('div#oldFileBox').on('click', 'span.delete-oldfile', function () {
		
		$(this).parent().prev().prop('name', 'delfile'); // hidden input 요소의 name속성 변경
		
		$(this).parent().remove();
		
		fileCount--;
	});
	
	
	// 글수정 버튼 클릭했을 때
	$('#btnModify').on('click',function(){
		
		var form = $('form#frm')[0];
		//console.log(form);
		//console.log(typeof form);
		
		var formData = new FormData(form); // 쿼리String 타입으로 변경
		console.log(formData);
		console.log(typeof formData);
		
		var bno = $('input#bno').val(); // parseInt("2") Number("2") - > 2
		
		$.ajax({
			url: '/api/boards/'+bno,
			//enctype: 'multipart/form-data', 본문 <form>에 입력했기때문에 여기서는 생략가능
			method: 'PUT',
			data: formData,
			processData: false, // 파일전송시 false 설정
			contentType: false, // 파일전송시 false 설정
			success: function (data) {
				console.log(data);
				
				if(data.result == 'success'){
					alert('글 수정 성공');
				}
				
				$('form#frm')[0].reset(); // 폼 내용 리셋
				$('div#newFileBox').empty();
				$('div#oldFileBox').empty();
			} // success
			
		});
		
	});
	

</script>
</body>
</html>