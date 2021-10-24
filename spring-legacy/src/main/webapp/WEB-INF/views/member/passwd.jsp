<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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

							<h5>비밀번호 변경</h5>
							<div class="divider" style="margin: 30px 0;"></div>

							<form action="/member/passwd" method="POST" id="frm">

								<div class="row">
									<div class="input-field col s12">
										<i class="material-icons prefix">password</i> <input
											id="passwd" type="password" name="passwd" class="validate">
										<label for="passwd">현재 비밀번호</label>
									</div>
								</div>

								<div class="input-field col s12">
									<i class="material-icons prefix">lock</i> <input id="newPasswd"
										type="password" name="newPasswd" class="validate"
										data-length="10"> <label for="newPasswd">새 비밀번호</label> <span
										class="helper-text" data-error="비밀번호는 10글자까지만 가능합니다."
										data-success="OK!">Helper text</span>
								</div>

								<div class="input-field col s12">
									<i class="material-icons prefix">check</i> <input id="newPasswd2"
										type="password" data-length="10"> <label for="newPasswd2">새
										비밀번호 재확인</label> <span class="helper-text" data-error=""
										data-success=""></span>
								</div>

								<br>
								<div class="row center">
									<button class="btn waves-effect waves-light" type="submit">
										비밀번호 변경 <i class="material-icons right">create</i>
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
		// 입력 문자 개수 보이기 기능 활성화
	    $('input#newPasswd, input#newPasswd2').characterCounter();

		$('#newPasswd2').on('focusout',function(){
			const newPasswd = $('#newPasswd').val();
			const newPasswd2 = $(this).val();
			
			if(newPasswd == newPasswd2){
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
			
			var result = confirm("비밀번호를 변경하시겠습니까");
			
			if(result == false){
				history.back();
			}
			
	
		})	
	</script>
</body>

</html>