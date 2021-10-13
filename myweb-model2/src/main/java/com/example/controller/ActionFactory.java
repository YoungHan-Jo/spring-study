package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.controller.home.HomeAction;
import com.example.controller.member.MemberJoinAction;
import com.example.controller.member.MemberJoinProAction;
import com.example.controller.member.MemberLoginAction;

// 메인컨트롤러는 수정하지 않아도 되도록. 액션을 관리하는 클래스 따로 만듦
public class ActionFactory {
	
	// 싱글톤 처리
	private static ActionFactory instance;
	
	public static ActionFactory getInstance() {
		if(instance == null) {
			instance = new ActionFactory();
		}
		return instance;
	}
	
	// 해시맵으로 관리 
	private Map<String,Action> actionMap = new HashMap<>();
	
	private ActionFactory() {
		// 명령어와 해당 명령어를 처리하는 컨트롤러 오브젝트(Action)를 등록
		actionMap.put("index.do", new HomeAction());
		
		// member 관련 컨트롤러를 해당 명령어와 함께 등록
		actionMap.put("memberJoin.do", new MemberJoinAction());
		actionMap.put("memberJoinPro.do", new MemberJoinProAction());
		actionMap.put("memberLogin.do", new MemberLoginAction());
		
		// board 관련 컨트롤러를 해당 명령어와 함게 등록
		
	}// 기본 생성자

	//명령어를 입력 받으면 Action을 반환함.
	public Action getAction(String command) {
		Action action = null;

		action = actionMap.get(command);

		return action;
	} // getAction

}
