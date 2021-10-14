<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
<jsp:include page="/include/head.jsp" />
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
			<!-- Teal page content  -->
			<div class="section">

				<!-- card panel -->
				<div class="card-panel">
					<div class="row">
						<div class="col s12" style="padding: 0 5%;">

							<h5>회원가입</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<form action="/member/joinPro.jsp" method="POST" id="frm"
								name="frm">
								<div class="row">
									<div class="input-field col s12 m9">
										<i class="material-icons prefix">account_box</i> <input
											id="id" type="text" name='id' data-length="20"> <label
											for="id">아이디</label> <span class="helper-text"></span>
									</div>

									<div class="col s12 m3">
										<button type="button"
											class="waves-effect waves-light btn-small" id="btnIdDupChk">ID중복확인</button>
									</div>


								</div>

								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">lock</i> <input id="passwd"
											type="password" name="passwd" class="validate"
											data-length="10"> <label for="passwd">비밀번호</label> <span
											class="helper-text" data-error="비밀번호는 10글자까지만 가능합니다."
											data-success="OK!">Helper text</span>
									</div>

									<div class="input-field col s12">
										<i class="material-icons prefix">check</i> <input id="passwd2"
											type="password" data-length="10"> <label
											for="passwd2">비밀번호 재확인</label> <span class="helper-text"
											data-error="" data-success=""></span>
									</div>
								</div>

								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">person</i> <input id="name"
											type="text" name="name" class="validate"> <label
											for="name">이름</label>
									</div>
								</div>

								<div class="row">
									<div class="input-field">
										<i class="material-icons prefix">event</i> <input type="date"
											id="birthday" name="birthday"> <label for="birthday">생년월일</label>
									</div>
								</div>

								<div class="row">
									<div class="input-field">
										<i class="material-icons prefix">wc</i> <select name="gender">
											<option value="" disabled selected>성별을 선택하세요.</option>
											<option value="M">남자</option>
											<option value="F">여자</option>
											<option value="N">선택 안함</option>
										</select> <label>성별</label>
									</div>
								</div>

								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">mail</i> <input id="email"
											type="email" name="email" class="validate"> <label
											for="email">본인 확인 이메일</label>
									</div>
								</div>

								<p class="row center">
									알림 이메일 수신 : &nbsp;&nbsp; <label> <input
										name="recvEmail" value="Y" type="radio" checked /> <span>예</span>
									</label> &nbsp;&nbsp; <label> <input name="recvEmail" value="N"
										type="radio" /> <span>아니오</span>
									</label>
								</p>

								<br>
								<div class="row center">
									<button class="btn waves-effect waves-light" type="submit">
										회원가입 <i class="material-icons right">create</i>
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
	<jsp:include page="/include/bottom.jsp" />
	<!-- end of footer area -->


	<!--  Scripts-->
	<jsp:include page="/include/commonJs.jsp" />
	<script>
		// 입력 문자 개수 보이기 기능 활성화
	    $('input#id, input#passwd, input#passwd2').characterCounter();
	    
		//아이디 중복확인 버튼
		$('#btnIdDupChk').on('click',function(){
			const id = $('#id').val();
			
			//id가 공백이면 '아이디입력하세요' 포커스 주기
			if(id == ''){
				alert('아이디를 입력하세요.');
				$('#id').focus();
				return;
			}
			
			// id 중복확인 자식창 열기
			open('/member/joinIdDupChk.jsp?id='+id,'idDupChk','width=500, height=400');
			//open(주소, 창 이름, 부가설정)
			
		})
		
		$('input#id').on('keyup',function(){
			var id = $(this).val();
			if (id.length == 0){
				return;
			}

			var $inputId = $(this);
			var $span = $inputId.next().next();
			
			$.ajax({
				url : '/api/members/' + id,
				method : 'GET',
				success : function(data){
					console.log(data);
					console.log(typeof data);
										
					if(data.count == 0){ // 사용가능한 아이디
						$span.html('사용가능한 아이디 입니다.').css('color','green');
					}else{ // data.count == 1 // 아이디 중복
						$span.html('사용중인 아이디 입니다.').css('color','red');
					}
				}// success
			});
		})
		
		
		
		$('#passwd2').on('focusout',function(){
			const passwd = $('#passwd').val();
			const passwd2 = $(this).val();
			
			if(passwd == passwd2){
				var $span = $(this).closest('div.input-field').find('span.helper-text');
				$span.html('비밀번호 일치함').css('color','green');
				$(this).removeClass('invalid').addClass('valid');
			}else{
				var $span = $(this).closest('div.input-field').find('span.helper-text');
				$span.html('비밀번호 일치하지하지 않음').css('color','red');
				$(this).removeClass('valid').addClass('invalid');
			}
			
		})
	    
	
	
		$('form#frm').on('submit',function(event){
			
			var id = $('#id').val();
			if(id.length < 3 || id.length >13){
				event.preventDefault();
				
				alert('아이디는 3글자 이상 13글자 이하만 가능합니다.');
				
				$('#id').select();
				
				return;
			}
			
		})	
	</script>
</body>

</html>