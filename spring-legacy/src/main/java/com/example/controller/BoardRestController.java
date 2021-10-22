package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.service.AttachService;
import com.example.service.BoardService;

import net.coobird.thumbnailator.Thumbnailator;

@RestController
@RequestMapping("/api/*")
public class BoardRestController {

	private BoardService boardService;
	private AttachService attachService;

	public BoardRestController(BoardService boardService, AttachService attachService) {
		super();
		this.boardService = boardService;
		this.attachService = attachService;
	}

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

	
	// AJAX로 첨부파일을 업로드 하게 된다면,
	// 대용량 파일이더라도 사용자가 기다리지 않아도 됨.
	// 첨부파일 업로드와 함께 주글쓰기 처리
	@PostMapping(value = "/boards/*", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public Map<String,Object> write(List<MultipartFile> files, BoardVO boardVO, HttpServletRequest request,
			RedirectAttributes rttr) throws IllegalStateException, IOException {
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
		
		BoardVO dbBoardVO = boardService.getBoardAndAttaches(num);

		Map<String,Object> map = new HashMap<>();
		map.put("result", "success");
		map.put("board", dbBoardVO);
		
		
		return map;
	} // write

}
