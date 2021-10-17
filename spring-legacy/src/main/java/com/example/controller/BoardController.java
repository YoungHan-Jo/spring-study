package com.example.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.domain.PageDTO;
import com.example.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private BoardService boardService;

	// 기본생성자 자동 @autowired 생략가능
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping("/list")
	public String list(Criteria cri, Model model) {
		System.out.println("list() 호출됨...");

		// board 테이블에서 검색, 페이징 적용한 글 리스트
		List<BoardVO> boardList = boardService.getBoardsByCri(cri);

		// 검색유형, 검색어에 따른 글 개수 가져오기
		int totalCount = boardService.getCountBySearch(cri);
		
		// 페이지블록 정보 객체준비. 필요한 정보를 생성자로 전달.
		PageDTO pageDTO = new PageDTO(cri, totalCount);
		
		// 뷰에서 사용할 데이터를 Model 객체에 저장, 
		// 스프링(DispatcherServlet)이 requestScope로 옮겨줌.
		model.addAttribute("boardList", boardList);
		model.addAttribute("pageMaker", pageDTO);

		return "board/boardList";
	} // list

}
