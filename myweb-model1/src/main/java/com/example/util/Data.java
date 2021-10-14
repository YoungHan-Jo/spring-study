package com.example.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

public class Data {

	public static final List<Session> SESSION_LIST = new ArrayList<>();
	// 접속한 클라이언트 세션들 리스트에 추가
	
	//추가 되어있는 세션(클라이언트)들에게 String(or JSON) 보내기
	public static void broadcast(String message) {
		for(Session session : SESSION_LIST) {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// for
	}
}
