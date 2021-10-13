package com.example.controller;

import lombok.Data;

@Data
public class ActionForward {

	// 롬복 getter 불리언은 get** 이 아니고 is**로 사용함
	
	private boolean isRedirect = false; // false 면 forward, true면 redirect 방식 이동
	private String path = null; // 리다이렉트일 경우 path는 *.do 형식의 경로
								// 포워드 일 경우 path는 .jsp 형식의 경로
}
