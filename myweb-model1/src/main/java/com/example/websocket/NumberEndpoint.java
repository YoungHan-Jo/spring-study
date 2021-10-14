package com.example.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.example.util.Data;

// 각 클라이언트가 서버에 연결될때마다
// @ServerEndpoint 애너테이션이 붙은 클래스 객체가 매번 별도의 스레드 내에서 생성되어 실행되는 구조임.
// 연결이 될 때 마다 new EchoServer 되어 객체가 생성됨. 병렬처리로 모든 클라이언트를 동시에 실시간으로 연결하기 위해
@ServerEndpoint(value = "/number") // 동일한 주소가 있으면 충돌해서 안됨
public class NumberEndpoint {

	@OnOpen
	public void handleOpen(Session session) throws IOException {
		System.out.println("@OnOpen : 클라이언트" + session.getId() + " 가 서버에 연결됨...");

		// 공유 리스트 컬렉션에 Session 객체를 추가하기
		Data.SESSION_LIST.add(session);
		
		// 1~20 까지 0.5초 간격으로 숫자를 전송하기. 전송이 끝나면 서버가 연결 종료함.

		for (int i = 1; i <= 20; ++i) {

			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 현재 연결된 클라이언트로 문자열 전송하기
			session.getBasicRemote().sendText(String.valueOf(i));
		} // for
		
		session.close();// 서버쪽에서 클라이언트와 연결 끊기
	}

	@OnMessage
	public void handleMessage(Session session, String message) throws IOException {
		System.out.println("@OnMessage : 클라이언트" + session.getId() + "로 부터 메시지를 받음...");
	}

	@OnClose
	public void handleClose(Session session, CloseReason closeReason) throws IOException {
		System.out.println("@OnClose : 클라이언트 " + session.getId() + " 와 " + closeReason + "이유로 인해 연결이 끊어짐...");
	}

	@OnError
	public void handleError(Session session, Throwable throwable) { // 오류,예외 둘다 발생할 수 있으므로 둘다 포함한 상위 클래스
		System.out.println("@OnError : 현재 클라이언트" + session.getId() + "와 연결중에 에러가 발생됨...");

		System.out.println(throwable.getMessage());
		throwable.printStackTrace();
	}
}
