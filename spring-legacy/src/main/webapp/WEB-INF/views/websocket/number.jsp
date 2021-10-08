<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>WebSocket - Receive Number Example</h1>
<hr>

<button type="button" id="btnConnect">Connect</button>
<button type="button" id="btnDisconnect">Disconnect</button>
<br><br>
<textarea rows="10" cols="50" id="ta" readonly></textarea>

<script>
// 웹소켓 객체를 저장할 변수 선언
var webSocket;

var taEchoArea = document.getElementById('ta'); // 메세지 출력 영역

// #btnConnect 버튼 클릭했을 때
document.getElementById('btnConnect').addEventListener('click',function(){
	console.log('Connect 버튼 클릭됨...');
	
	console.log('location.host : ' + location.host); //localhost:8090

	// 연결할 url 주소 문자열
	var url = 'ws://'+location.host + "/number";

	console.log('url주소 :' +url);

	//소켓서버에 최초 연결하기. 연결되면 웹소켓(WebSocket) 객체가 준비됨
	webSocket = new WebSocket(url);

	// 웹소켓 객체에 이벤트 동작 4개 연결하기 (open, close, error, message)
	webSocket.onopen = function(event){
		ta.value += '서버와 연결됨...\n';
	};
	webSocket.onclose = function(event){
		ta.value += '서버와 연결이 끊어짐...\n';
	};
	webSocket.onerror = function(event){
		ta.value += '서버와 연결 중 에러가 발생됨...\n';
	};
	webSocket.onmessage = function(event){
		ta.value += '서버로부터 받은 메세지 : ' + event.data + '\n';
		
		ta.scrollTop = ta.scrollHeight;
		// scrollTop = 0 일때 스크롤 제일위로 붙어 있음
		// scrollHeight만큼 위에서 떨어뜨리기
	};
});


//#btnDisconnect 버튼 클릭했을때
document.getElementById('btnDisconnect').addEventListener('click',function (){
	
	// 웹소켓 객체를 닫아서 서버와 연결 끊기
	webSocket.close();
	
});

</script>

</body>
</html>