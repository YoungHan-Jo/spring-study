package com.example.repository;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.domain.MemberVO;

public class MemberDAOTests {

	private MemberDAO memberDAO;

	private MemberVO memberVO1;
	private MemberVO memberVO2;

	@BeforeClass
	public static void beforeTest() {
		System.out.println("테스트 시작");
	}

	@Before
	public void setUp() {
		memberDAO = MemberDAO.getInstance();

		memberDAO.deleteAll();

		memberVO1 = new MemberVO();
		memberVO1.setId("aaa");
		memberVO1.setPasswd("1234");
		memberVO1.setName("홍길동");
		memberVO1.setBirthday("19910101");
		memberVO1.setGender("M");
		memberVO1.setEmail("aaa@aaa.com");
		memberVO1.setRecvEmail("Y");
		memberVO1.setRegDate(new Timestamp(System.currentTimeMillis()));

		memberVO2 = new MemberVO();
		memberVO2.setId("bbb");
		memberVO2.setPasswd("1234");
		memberVO2.setName("성춘향");
		memberVO2.setBirthday("19950505");
		memberVO2.setGender("F");
		memberVO2.setEmail("bbb@bbb.com");
		memberVO2.setRecvEmail("N");
		memberVO2.setRegDate(new Timestamp(System.currentTimeMillis()));
	}

	@Test
	public void testConnection() { // 접속 테스트

		String URL = "jdbc:mysql://localhost:3306/jspdb?useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul";
		String USER = "jspid";
		String PASSWD = "jsppass";

		Connection con = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, USER, PASSWD);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertNotNull(con);
	} // test getConnection

	@Test
	public void testDeleteAll() {
		memberDAO.insert(memberVO1);
		memberDAO.insert(memberVO2);

		memberDAO.deleteAll();

		int count = memberDAO.getCountAll();

		assertEquals(0, count);
	}

	@Test
	public void testInsert() {
		memberDAO.insert(memberVO1);
		memberDAO.insert(memberVO2);

		int count = memberDAO.getCountAll();

		assertEquals(2, count);
	}

	@After
	public void tearDown() {
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("테스트 종료");
	}
}
