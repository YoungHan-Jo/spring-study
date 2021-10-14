package com.example.repository;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.example.domain.BoardVO;

public class BoardDAOTests {

	private BoardDAO boardDAO;

	@Before
	public void setUp() {
		boardDAO = BoardDAO.getInstance();

		boardDAO.deleteAll();

	}

	@Test
	public void testAddBoard() {

		Random random = new Random();

		// 주글 1000개 insert하기
		for (int i = 0; i < 1000; ++i) {
			BoardVO boardVO = new BoardVO();

			int num = boardDAO.getNextnum();

			boardVO.setNum(num);
			boardVO.setMid("user1");
			boardVO.setSubject("글제목" + i);
			boardVO.setContent("글내용\n다음줄" + i);
			boardVO.setReadcount(random.nextInt(100));
			boardVO.setRegDate(new Timestamp(System.currentTimeMillis()));
			boardVO.setIpaddr("127.0.0.1");
			boardVO.setReRef(num); // 주글일때는 글번호가 글그룹 번호와 동일
			boardVO.setReLev(0); // 주글은 들여쓰기 레벨0
			boardVO.setReSeq(0); // 주글은 글그룹 안에서 순번 0 (오름차순 시 먼저 보이기)

			boardDAO.addBoard(boardVO);

		} // for

		assertEquals(1000, boardDAO.getCountAll());
	}

} // end of tests
