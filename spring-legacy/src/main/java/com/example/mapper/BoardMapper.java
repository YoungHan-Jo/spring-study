package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.example.domain.BoardVO;
import com.example.domain.Criteria;

public interface BoardMapper {

	@Delete("DELETE FROM board")
	void deleteAll();

	// 외부 xml로 사용하면 애노테이션을 사용하면 안됨. 중복 금지
	// @Delete("DELETE FROM board WHERE num = #{num}")
	void deleteBoardByNum(int num);

	// @Select("SELECT COUNT(*) AS cnt FROM board")
	int getCountAll();

	int getNextnum();

	void addBoard(BoardVO boardVO);

	List<BoardVO> getBoardsAll();

	BoardVO getBoardByNum(int num);

	// 조회수 1증가
	void updateReadcount(int num);

	void updateBoard(BoardVO boardVO);

	// 답글쓰기에서 답글을 다는 대상글과 같은 글 그룹 내에서
	// 답글을 다는 대상들의 그룹내 순번보다 큰 클들의 순번을 1씩 증가
	// mapper 메소드 매개변수 특이사항
	// : 매개변수가 2개 이상일 경우,
	//  각 매개변수마다 SQL문에서 사용할 이름을 지정해야함
	// VO객체를 제외한 나머지 매개변수는 이름을 지정해주는게 원칙이지만
	// (매개변수가 1개만 있으면 생략가능)
	void updateReSeqPlusOne(
			@Param("reRef") int reRef,
			@Param("reSeq") int reSeq);
	
	//검색어가 적용된 글 개수
	int getCountBySearch(Criteria cri);
	
	//페이징으로 글 가져오기
	List<BoardVO> getBoardsWithPaging(Criteria cri);
	
	// join 쿼리로 게시글,첨부파일 가져오기
	BoardVO getBoardAndAttaches(int num);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
