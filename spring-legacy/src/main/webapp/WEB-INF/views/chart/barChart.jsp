<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>차트연습</h1>
<hr>

<canvas id="myChart" width="400" height="300"></canvas>



<script src="/resources/js/chart.min.js"></script>
<script>
// 차트를 그릴 캔버스 객체 가져오기
var canvas = document.getElementById('myChart');
// 캔버스 객체에 그림을 그릴 컨텍스트 객체 가져오기
var context = canvas.getContext('2d');

//차트 그리기
var myChart = new Chart(context, {
	type: 'bar',
	data: {
		labels: ['국어','영어','수학'],
		datasets: [{ // 괄호 하나당 한 사람에 대한
			label: '철수',
			data: [80,72,66],
			backgroundColor: ['red'],
			borderColor: ['black'],
			borderWidth: 2
		},
		{ // 괄호 하나당 한 사람에 대한
			label: '영희',
			data: [70,82,85],
			backgroundColor: ['blue'],
			borderColor: ['black'],
			borderWidth: 2
		}]
	},
	options: {
		responsive : false,	// 기본동작이 반응형이므로 따로 크기를 지정해주려면 false로
	}
});

</script>
</body>
</html>