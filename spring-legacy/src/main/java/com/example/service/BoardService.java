package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.mapper.AttachMapper;
import com.example.mapper.BoardMapper;

//Service패키지는 트랜잭션을 위한 패키지.
// 꼭 모든 mapper로 다 나눠야하는건 아님.
// 같은 싸이클 안에서 돌아가는 여러 mapper가 들어 올 수 있음

@Service // @Component 계열 애노테이션 + 트랜잭션 처리 가능
@Transactional // 클래스에 선언하면 이 클래스에 있는 모든 메소드가 트랜잭션으로 동작함
public class BoardService {

	// @Autowired //멤버객체로 @Autowired로 하면 간단하지만 스프링에서만 사용가능해서 안되는게 많음
	private BoardMapper boardMapper;
	private AttachMapper attachMapper;

	// 생성자로 의존객체를 받도록 선언하면 @Autowired 애노테이션 생략가능
	public BoardService(BoardMapper boardMapper, AttachMapper attachMapper) {
		this.boardMapper = boardMapper;
		this.attachMapper = attachMapper;
	}

	// 페이징,검색어 적용하여 글 리스트 가져오기
	public List<BoardVO> getBoardsByCri(Criteria cri) {

		// 1페이지 0행부터
		// 2페이지 10행부터 시작
		// 3페이지 20행부터 시작
		// 4페이지 30행부터 시작
		int startRow = (cri.getPageNum() - 1) * cri.getAmount();

		cri.setStartRow(startRow);

		List<BoardVO> boardList = boardMapper.getBoardsWithPaging(cri);
		return boardList;
	} // getBoardsByCri

	// 페이징, 검색어 적용해여 글 개수 가져오기
	public int getCountBySearch(Criteria cri) {
		int count = boardMapper.getCountBySearch(cri);
		return count;
	} // getCountBySearch

	public // 조회수 1증가
	void updateReadcount(int num) {
		boardMapper.updateReadcount(num);
	}

	public BoardVO getBoardByNum(int num) {
		return boardMapper.getBoardByNum(num);
	}

	public BoardVO getBoardAndAttaches(int num) {
		// join없이 하는 방법
//		BoardVO boardVO = boardMapper.getBoardByNum(num);
//		List<AttachVO> attachList = attachMapper.getAttachesByBno(num);
//		boardVO.setAttachList(attachList);
//		return boardVO; 

		// join 쿼리로 데이터 가져오기
		BoardVO boardVO = boardMapper.getBoardAndAttaches(num);

		return boardVO;
	}

	public int getNextnum() {
		return boardMapper.getNextnum();
	}
	
	
	// 주글 한개(boardVO)와 첨부파일 여러개(attachList) 트랜잭션으로 처리하기
	@Transactional
	public void addBoardAndAttaches(BoardVO boardVO) {
		// attach 테이블의 bno컬럼이 외래키로서
		// board 테이블의 num 컬럼을 참조하므로
		// board 레코드가 먼저 insert된 이후에 attach 레코드가 insert 가능함.
		
		boardMapper.addBoard(boardVO);
		
		List<AttachVO> attachList = boardVO.getAttachList();
		
		if (attachList.size() > 0) {
			attachMapper.addAttaches(attachList);
			// addAttaches로 한번에 처리되도록 DAO를 만들었기때문에 for문 돌릴 필요 없음.
			// 더 효율적인 방법
		}
	}
	
	@Transactional
	public void deleteBoardAndAttaches(int bno) {
		// 외래키 관계에 의해, 참조하는 테이블 부터 삭제
		
		attachMapper.deleteAttachesByBno(bno);
		boardMapper.deleteBoardByNum(bno);
	}
	
	
	
	
	
	
	
	
	
} // end of class
