package com.example.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.domain.PageDTO;
import com.example.service.AttachService;
import com.example.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private BoardService boardService;
	private AttachService attachService;

	// DI(스프링 자동주입) 생성자 하나만 있으면 @Autowired 생략가능
	public BoardController(BoardService boardService, AttachService attachService) {
		super();
		this.boardService = boardService;
		this.attachService = attachService;
	}

	@GetMapping("/list")
	public String list(Criteria cri, Model model) {
		// 매개변수 객체의 setter에 해당하는 정보를 spring이 자동으로 만들어 줌
		// 1. 멤버변수 기본값으로 셋팅. 0, 빈문자열
		// 2. 기본 생성자를 실행 해 그 위에 덮어씌움

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

	@GetMapping("/content")
	public String content(int num, @ModelAttribute("pageNum") String pageNum, Model model) {
		
		// 조회수 1 증가시키기
		boardService.updateReadcount(num);
		
		//상세보기 할 글 한개 가져오기
		BoardVO boardVO = boardService.getBoardByNum(num);
		
		// 첨부파일 정보리스트 가져오기
		List<AttachVO> attachList = attachService.getAttachesByBno(num);
		
		model.addAttribute("board", boardVO);
		model.addAttribute("attachList", attachList);
		// jsp에서 태그로 할 수는 있지만 
		// controller에서 미리 구해서 값 넘기는게 편함
		model.addAttribute("attachCount", attachList.size());
		
		// controller에서 사용하지 않고 화면으로 그냥 넘기기만 하는 값
		// 매개변수에서 @ModelAttribute("키값") 으로 처리해서 구분하기%%%%%%%%
		// model.addAttribute("pageNum", pageNum);
		

		return "board/boardContent";
	} // content

}
