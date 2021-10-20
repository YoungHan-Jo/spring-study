package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.domain.PageDTO;
import com.example.service.AttachService;
import com.example.service.BoardService;

import net.coobird.thumbnailator.Thumbnailator;

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

//		//상세보기 할 글 한개 가져오기
//		BoardVO boardVO = boardService.getBoardByNum(num);
//		// 첨부파일 정보리스트 가져오기
//		List<AttachVO> attachList = attachService.getAttachesByBno(num);

		// join 쿼리문으로 게시판 글 + 첨부파일 리스트 정보 한번에 가져오기
		BoardVO boardVO = boardService.getBoardAndAttaches(num);

		model.addAttribute("board", boardVO);
		model.addAttribute("attachList", boardVO.getAttachList());
		// jsp에서 태그로 할 수는 있지만
		// controller에서 미리 구해서 값 넘기는게 편함
		model.addAttribute("attachCount", boardVO.getAttachList().size());

		// controller에서 사용하지 않고 화면으로 그냥 넘기기만 하는 값
		// 매개변수에서 @ModelAttribute("키값") 으로 처리해서 구분하기%%%%%%%%
		// model.addAttribute("pageNum", pageNum);

		return "board/boardContent";
	} // content

	// 새로운 주글 쓰기 폼 화면 요청
	@GetMapping("/write")
	public String writeForm(@ModelAttribute("pageNum") String pageNum, HttpSession session) {

		// 사용자 로그인 확인
		// 주변부 로직(톰캣의 필터 -> 스프링의 인터셉터)
//		if(session.getAttribute("id") == null) {
//			return "redirect:/member/login";
//		}

		return "board/boardWrite";
	}// write

	// "년/월/일' 형식의 폴더명을 리턴하는 메소드
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		String str = sdf.format(new Date());

		return str;
	}// getFolder

	private boolean checkImageType(File file) throws IOException {
		boolean isImage = false;

		String contentType = Files.probeContentType(file.toPath()); // "image/jpg" "image/png" 등으로 리턴함.
		System.out.println("contentType : " + contentType);

		isImage = contentType.startsWith("image"); // image로 시작할 때 true로 리턴

		return isImage;
	} // checkImageType

	// 첨부파일 업로드(썸네일 이미지 생성) 후 attachList 리턴하는 메소드
	private List<AttachVO> uploadFilesAndGetAttachList(List<MultipartFile> files, int bno)
			throws IllegalStateException, IOException {

		List<AttachVO> attachList = new ArrayList<AttachVO>();

		// 업로드 처리로 생성해야 할 파일 정보가 없으면 메소드 종료
		if (files == null || files.size() == 0) {

			System.out.println("첨부파일 없음...");

			return attachList;
		}

		System.out.println("첨부파일 개수: " + files.size());

		// 업로드 기준경로
		String uploadFolder = "C:/jyh/upload";

		File uploadPath = new File(uploadFolder, getFolder()); // "C:/jyh/upload/2021/10/19"
		System.out.println("uploadPath : " + uploadPath.getPath());

		if (uploadPath.exists() == false) { // 파일경로가 존재하지 않으면
			uploadPath.mkdirs(); // s붙여야 하위폴더까지 전부 만듦
		}

		for (MultipartFile multipartFile : files) {
			if (multipartFile.isEmpty()) { // 첨부파일 내용 비어있을 때
				continue;
			}

			String originalFilename = multipartFile.getOriginalFilename();

			System.out.println("originalFilename : " + originalFilename);

			// 파일명 중복을 피하기 위해
			// uuid를 앞에 붙인 파일명을 사용함.
			UUID uuid = UUID.randomUUID();
			String uploadFilename = uuid.toString() + "_" + originalFilename;

			File file = new File(uploadPath, uploadFilename); // 생성할 파일경로 파일명 정보

			// 파일1개 업로드(파일 생성) 완료
			multipartFile.transferTo(file); // 파일 생성하기
			// =======================================================================

			// 현재 업로드한 파일이 이미지 파일이면 썸네일 이미지를 추가로 생성하기
			boolean isImage = checkImageType(file); // 이미지 파일여부 boolean으로 확인

			if (isImage == true) {
				File outFile = new File(uploadPath, "s_" + uploadFilename);

				// createThumbnail(원본이미지, 썸네일 이미지, 가로, 세로)
				Thumbnailator.createThumbnail(file, outFile, 100, 100); // 썸네일 이미지 파일 생성
			}

			// =========================================================================

			// insert할 주글 AttachVO 객체준비 및 데이터 저장
			AttachVO attachVO = new AttachVO();
			attachVO.setUuid(uuid.toString());
			attachVO.setUploadpath(getFolder());
			attachVO.setFilename(originalFilename);
			attachVO.setFiletype(isImage ? "I" : "O");
			attachVO.setBno(bno);

			attachList.add(attachVO);
		} // for

		return attachList;
	} // uploadFilesAndGetAttachList

	// 첨부파일 업로드와 함께 주글쓰기 처리
	@PostMapping("/write")
	public String write(List<MultipartFile> files, BoardVO boardVO, HttpServletRequest request, RedirectAttributes rttr)
			throws IllegalStateException, IOException {
		// 스프링 웹에서는 클라이언트로부터 넘어오는 file타입의 input태그 요소의 갯수만큼
		// MultipartFile 타입의 객체로 전달받게 됨.
		// 파일이 진짜 있든없든 file타입 input태그는 전부 다 포함 됨.
		// MultipartFile[]배열로 받는 방법과
		// List<MultipartFile> 리스트로 받는 방법이 있음

		// insert 할 새 글번호 가져오기
		int num = boardService.getNextnum();

		List<AttachVO> attachList = uploadFilesAndGetAttachList(files, num);

		// insert 할 BoardVO 객체의 데이터 설정(나머지 정보는 매개변수로 spring이 자동으로 입력)
		boardVO.setNum(num);
		boardVO.setReadcount(0);
		boardVO.setRegDate(new Date());
		boardVO.setIpaddr(request.getRemoteAddr());
		boardVO.setReRef(num); // 주 글일경우 현재글 그룹번호와 동일
		boardVO.setReLev(0); // 주글은 들여쓰기 레벨 0
		boardVO.setReSeq(0); // 주글일 경우 글그룹 순번 0;
		boardVO.setAttachList(attachList);

		// 주글 한개(boardVO)와 첨부파일 여러개(attachList)를 한개의 트랜잭션으로 처리하기
		boardService.addBoardAndAttaches(boardVO);

		// =========================================================================

		// 리다이렉트 시 서버에 쿼리스트링으로 전달할 데이터를
		// RedirectAttributes 타입의 객체에 저장하면,
		// 스프링이 자동으로 쿼리스트링으로 변환해서 처리해줌
		// 매개변수 받아서 입력

		rttr.addAttribute("num", boardVO.getNum());
		rttr.addAttribute("pageNum", 1);

		// 대부분 POST형식은 뒤로가기하면 사용자 입력값이 중복 입력 될 수도 있어서
		// 리다이렉트로 주소를 갱신하기
		return "redirect:/board/content";
	} // write

	// 첨부파일 삭제하는 메소드
	private void deleteAttachFiles(List<AttachVO> attachList) {
		// 삭제할 파일정보가 없으면 메소드 종료
		if (attachList == null || attachList.size() == 0) {
			System.out.println("삭제할 첨부파일 정보가 없습니다...");
			return;
		}

		String basePath = "C:/jyh/upload";

		for (AttachVO attachVO : attachList) {
			String uploadpath = basePath + "/" + attachVO.getUploadpath();
			String filename = attachVO.getUuid() + "_" + attachVO.getFilename();

			File file = new File(uploadpath, filename);

			if (file.exists()) { // 해당경로에 첨부파일이 존재하면
				file.delete();
			}

			// 첨부파일이 이미지일 경우 썸네일 이미지도 삭제
			if (attachVO.getFiletype().equals("I")) {
				File thumbnailFile = new File(uploadpath, "s_" + filename);
				if(thumbnailFile.exists()) {
					thumbnailFile.delete();
				}
			}

		} // for

	} // deleteAttachFiles

	@GetMapping("/remove")
	public String remove(int num, String pageNum) {

		// ========= 첨부파일 삭제 ==========
		List<AttachVO> attachList = attachService.getAttachesByBno(num);

		deleteAttachFiles(attachList);// 첨부파일(썸네일 포함) 삭제하기

		System.out.println("========= 첨부파일 삭제 완료 ========");

		// attach, board 테이블 내용 삭제하기- 트랜잭션 단위로 처리
		boardService.deleteBoardAndAttaches(num);

		// 글 목록으로 리다이렉트로 이동
		return "redirect:/board/list?pageNum=" + pageNum;

	}// remove

}
