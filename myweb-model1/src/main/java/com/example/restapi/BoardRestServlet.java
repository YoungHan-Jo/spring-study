package com.example.restapi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.domain.AttachVO;
import com.example.domain.BoardVO;
import com.example.domain.Criteria;
import com.example.repository.AttachDAO;
import com.example.repository.BoardDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.coobird.thumbnailator.Thumbnailator;

// urlPatterns가 기본값. 생략가능
// loadOnStartup <- 객체를 미리 준비 하도록. 뒤에 숫자는 우선순위, 1순위
// 사용할 때 객체 생성을 자동으로 하긴하는데 오래걸릴수도 있기때문에 미리 생성함
@WebServlet(urlPatterns = { "/api/boards/*" }, loadOnStartup = 1)
public class BoardRestServlet extends HttpServlet {

	private static final String BASE_URI = "/api/boards";

	private BoardDAO boardDAO = BoardDAO.getInstance();

	private AttachDAO attachDAO = AttachDAO.getInstance();

	private Gson gson;

	public BoardRestServlet() {
		GsonBuilder builder = new GsonBuilder();
		gson = builder.create();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// GET 조회
		// "/api/boards/{bno}" -> 상세 글 보기
		// "/api/boards/pages/{page}" -> 페이지 번호에 해당하는 글 목록 가져오기

		String requestURI = request.getRequestURI();
		String str = requestURI.substring(BASE_URI.length() + 1);

		String strJson = "";

		if (str.startsWith("pages")) { // 페이지 번호에 해당하는 글 목록

			int beginIndex = str.indexOf("/")+1;
			String strPage = str.substring(beginIndex);
			int page = Integer.parseInt(strPage);
			
			
			// 글목록 가져오기 조건객체 준비
			Criteria cri = new Criteria(); // 기본값 1페이지 10개
			cri.setPageNum(page); // 요청 페이지번호 설정
			
			List<BoardVO> boardList = boardDAO.getBoards(cri);
			
			strJson = gson.toJson(boardList);
			
		} else { // 글 번호에 해당하는 상세글 보기

			int bno = Integer.parseInt(str);
			

			BoardVO boardVO = boardDAO.getBoardByNum(bno);
			List<AttachVO> attachList = attachDAO.getAttachesByBno(bno);

			Map<String, Object> map = new HashMap<>();
			map.put("board", boardVO);
			map.put("attachList", attachList);

			strJson = gson.toJson(map);
		}

		sendResponse(response, strJson);

	} // doGet

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// POST 등록
		// "/api/boards/new" -> 새 주글 등록
		// "/api/boards/reply" -> 새 답글 등록

		String requestURI = request.getRequestURI();
		String type = requestURI.substring(BASE_URI.length() + 1);

		if (type.equals("new")) {
			writeNewBoard(request, response);
		} else if (type.equals("reply")) {
			//writeReplyBoard(request, response); // 새로운 답글쓰기
		}

	} // doPost

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// PUT 수정
		// "/api/boards/{bno}" + 수정 내용 -> 글정 글 수정하기

		String requestURI = request.getRequestURI();
		String bno = requestURI.substring(BASE_URI.length() + 1);
		int num = Integer.parseInt(bno);

		String uploadFolder = "C:/jyh/upload"; // 업로드 기준 경로

		File uploadPath = new File(uploadFolder, getFolder()); // "C:/jyh/upload/2021/08/03"

		System.out.println("uploadPath : " + uploadPath.getPath());

		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		// MultipartRequest 인자값
		// 1.request
		// 2.업로드할 물리적 경로 "C:/jyh/upload" <- \ 두번 쓰던가 or / 한번으로 가능
		// 3.업로드 최대크기 바이트단위로 제한 // 1024Byte * 102Byte = 1MB
		// 4. request의 텍스트 데이터, 파일명 인코딩 "utf-8"
		// 5. 파일명 변경 정책. 파일명 중복시 이름변경 규칙 가진 객체를 전달

		// 파일 업로드하기
		MultipartRequest multi = new MultipartRequest(request, uploadPath.getPath(), 1024 * 1024 * 50, "utf-8",
				new DefaultFileRenamePolicy());
		// === 파일 업로드 완료===

		// AttachDAO 객체 준비
		AttachDAO attachDAO = AttachDAO.getInstance();

		// BoardDAO 객체준비
		BoardDAO boardDAO = BoardDAO.getInstance();

		// ==================신규 첨부파일 정보를 테이블에 insert 하기 =================

		// input type="file" 태그의 name 속성들을 가져오기
		Enumeration<String> enu = multi.getFileNames(); // Iterator, Enumeration 반복자 객체

		while (enu.hasMoreElements()) { // 파일이 있으면
			String fname = enu.nextElement(); // file0 file1 file2 file3 등 하나씩 가져옴

			// 저장된 파일명 가져오기
			String filename = multi.getFilesystemName(fname); // fname이 file0 일때
			System.out.println("getFilesystemName : " + filename);

			// 원본 파일명 가져오기
			String original = multi.getOriginalFileName(fname);
			System.out.println("getOriginalFileName : " + original);

			if (filename == null) { // 업로드 할 파일정보가 없으면
				continue; // 그 다음 반복으로 건너뛰기
				// 파일추가 4개 하고 2개만 업로드 했을때 비어있는 2곳도 처리하는 방법
			}

			// AttachVO 객체 준비
			AttachVO attachVO = new AttachVO();

			attachVO.setFilename(filename);
			attachVO.setUploadpath(getFolder());
			attachVO.setBno(num);

			UUID uuid = UUID.randomUUID();
			attachVO.setUuid(uuid.toString()); // 기본키 uuid 저장

			File file = new File(uploadPath, filename); // 년월일 경로에 실제 파일명의 파일객체

			boolean isImage = checkImageType(file); // 이미지 파일 여부 확인

			attachVO.setFiletype((isImage == true) ? "I" : "O");

			// 이미지 파일이면 썸네일 이미지 생성하기
			if (isImage == true) {
				File outFile = new File(uploadPath, "s_" + filename); // 출력할 썸네일 파일정보
				// (읽을 파일, 출력할 썸네일 파일, 넓이,높이)
				Thumbnailator.createThumbnail(file, outFile, 100, 100); // 썸네일 생성
			}

			// 첨부파일 attach 테이블에 attachVO를 insert 하기
			attachDAO.addAttach(attachVO);

		} // while

		// ==============신규 첨부파일 정보를 테이블에 insert 하기 완료 =================

		// ==============삭제할 첨부파일 정보를 삭제하기 =====================
		String[] delFilesUuid = multi.getParameterValues("delfile");

		if (delFilesUuid != null) {
			for (String uuid : delFilesUuid) {

				// 첨부파일 uuid에 해당하는 첨부파일 VO객체 가져오기
				AttachVO attach = attachDAO.getAttachByUuid(uuid);

				String path = uploadFolder + "/" + attach.getUploadpath() + "/" + attach.getFilename();
				File deleteFile = new File(path);

				if (deleteFile.exists()) { // 삭제할 파일이 존재하면
					deleteFile.delete(); // 파일 삭제하기
				} // if

				if (attach.getFiletype().equals("I")) { // 이미지 파일이면 썸네일 파일도 지움
					String thumbnailPath = uploadFolder + "/" + attach.getUploadpath() + "/s_" + attach.getFilename();
					File thumbnailFile = new File(thumbnailPath);
					if (thumbnailFile.exists()) { // 삭제할 파일이 존재하면
						thumbnailFile.delete(); // 파일 삭제하기
					}
				} // if

				// DB에서 uuid에 해당하는 첨부파일정보 삭제하기
				attachDAO.deleteAttachesByUuid(uuid);

			} // for
		}

		// =====================삭제할 첨부파일 정보를 삭제하기 완료 ========================

		// ===================== board 테이블 게시글 수정하기 ========================
		// 수정에 사용할 게시글 VO 객체 준비
		BoardVO boardVO = new BoardVO();

		// 파라미터값 가져와서 VO에 저장
		boardVO.setNum(num);
		boardVO.setSubject(multi.getParameter("subject"));
		boardVO.setContent(multi.getParameter("content"));
		boardVO.setIpaddr(request.getRemoteAddr());

		// DB에 게시글 수정하기
		boardDAO.updateBoard(boardVO);

		// ===================== board 테이블 게시글 수정하기 완료 ========================
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("result", "success");
		// 자바객체 -> JSON 문자열로 변환 (직렬화)
		String strJson = gson.toJson(map);
		// 클라이언트 쪽으로 출력하기
		sendResponse(response, strJson);


	} // doPut

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// DELETE 삭제
		// "/api/boards/{bno}" -> 글정 글 삭제하기

		String requestURI = request.getRequestURI();
		String bno = requestURI.substring(BASE_URI.length() + 1);
		int num = Integer.parseInt(bno);

		// 게시글 번호에 첨부된 첨부파일 정보 리스트 가져오기
		List<AttachVO> attachList = attachDAO.getAttachesByBno(num);

		// 업로드 기준 경로
		String uploadFolder = "C:/jyh/upload";

		// 첨부 파일 삭제하기
		for (AttachVO attach : attachList) {
			String path = uploadFolder + "/" + attach.getUploadpath() + "/" + attach.getFilename();
			File deleteFile = new File(path);

			if (deleteFile.exists()) { // 삭제할 파일이 존재하면
				deleteFile.delete(); // 파일 삭제하기
			} // if

			if (attach.getFiletype().equals("I")) { // 이미지 파일이면 썸네일 파일도 지움
				String thumbnailPath = uploadFolder + "/" + attach.getUploadpath() + "/s_" + attach.getFilename();
				File thumbnailFile = new File(thumbnailPath);
				if (thumbnailFile.exists()) { // 삭제할 파일이 존재하면
					thumbnailFile.delete(); // 파일 삭제하기
				}
			} // if
		} // for

		// DB첨부파일 정보 삭제하기
		attachDAO.deleteAttachesByBno(num);
		// DB 게시글 정보 삭제하기
		boardDAO.deleteBoardByNum(num);

		Map<String, Object> map = new HashMap<>();
		map.put("result", "success");
		// 자바객체 -> JSON 문자열로 변환 (직렬화)
		String strJson = gson.toJson(map);
		// 클라이언트 쪽으로 출력하기
		sendResponse(response, strJson);

	} // doDelete

	private void sendResponse(HttpServletResponse response, String json) throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
	} // sendResponse

	// 새로운 주글 쓰기
	private void writeNewBoard(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String uploadFolder = "C:/jyh/upload"; // 업로드 기준 경로

		File uploadPath = new File(uploadFolder, getFolder()); // "C:/jyh/upload/2021/08/03"

		System.out.println("uploadPath : " + uploadPath.getPath());

		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		// 파일 업로드하기
		MultipartRequest multi = new MultipartRequest(request, uploadPath.getPath(), 1024 * 1024 * 50, "utf-8",
				new DefaultFileRenamePolicy());
		// 파일 업로드 완료

		// AttachDAO 객체 준비
		AttachDAO attachDAO = AttachDAO.getInstance();

		// BoardDAO 객체준비
		BoardDAO boardDAO = BoardDAO.getInstance();

		// insert할 새 게시글 번호 가져오기
		int num = boardDAO.getNextnum();

		// input type="file" 태그의 name 속성들을 가져오기
		Enumeration<String> enu = multi.getFileNames(); // Iterator, Enumeration 반복자 객체

		while (enu.hasMoreElements()) { // 파일이 있으면

			String fname = enu.nextElement(); // file0 file1 file2 file3 등 하나씩 가져옴

			// 저장된 파일명 가져오기
			String filename = multi.getFilesystemName(fname); // fname이 file0 일때
			System.out.println("getFilesystemName : " + filename);

			// 원본 파일명 가져오기
			String original = multi.getOriginalFileName(fname);
			System.out.println("getOriginalFileName : " + original);

			if (filename == null) { // 업로드 할 파일정보가 없으면
				continue; // 그 다음 반복으로 건너뛰기
				// 파일추가 4개 하고 2개만 업로드 했을때 비어있는 2곳도 처리하는 방법
			}

			// AttachVO 객체 준비
			AttachVO attachVO = new AttachVO();

			attachVO.setFilename(filename);
			attachVO.setUploadpath(getFolder());
			attachVO.setBno(num);

			UUID uuid = UUID.randomUUID();
			attachVO.setUuid(uuid.toString()); // 기본키 uuid 저장

			File file = new File(uploadPath, filename); // 년월일 경로에 실제 파일명의 파일객체

			boolean isImage = checkImageType(file); // 이미지 파일 여부 확인

			attachVO.setFiletype((isImage == true) ? "I" : "O");

			// 이미지 파일이면 썸네일 이미지 생성하기
			if (isImage == true) {
				File outFile = new File(uploadPath, "s_" + filename); // 출력할 썸네일 파일정보
				// (읽을 파일, 출력할 썸네일 파일, 넓이,높이)
				Thumbnailator.createThumbnail(file, outFile, 100, 100); // 썸네일 생성
			}

			attachDAO.addAttach(attachVO);

		} // while

		// BoardVO 객체 준비
		BoardVO boardVO = new BoardVO();

		// 파라미터값 가져와서 VO에 저장. MultipartRequest 로 부터 가져옴
		boardVO.setMid(multi.getParameter("id"));
		boardVO.setSubject(multi.getParameter("subject"));
		boardVO.setContent(multi.getParameter("content"));

		// 글 번호 설정
		boardVO.setNum(num);

		// ipaddr regDate readcount
		boardVO.setIpaddr(request.getRemoteAddr()); // ip 주소 String으로 가져오기
		boardVO.setRegDate(new Timestamp(System.currentTimeMillis()));
		boardVO.setReadcount(0);

		// 주글에서 re_ref re_lev re_seq 설정하기
		boardVO.setReRef(num); // 주글일때는 글 번호와 글 그룹 번호는 동일함
		boardVO.setReLev(0); // 들여쓰기 레벨. 주글은 0레벨
		boardVO.setReSeq(0); // 그룹내 순번, 주글은 그룹안에서 순번 0번(오름차순 시 첫번째)

		// 주글 등록하기
		boardDAO.addBoard(boardVO);

		// =================================================================================

		BoardVO dbBoard = boardDAO.getBoardByNum(num);
		List<AttachVO> attachList = attachDAO.getAttachesByBno(num);

		Map<String, Object> map = new HashMap<>();
		map.put("result", "success");
		map.put("board", dbBoard); // JSON에서 {}
		map.put("attachList", attachList); // JSON 에서 []

		String strJson = gson.toJson(map);

		sendResponse(response, strJson);

	} // writeNewBoard

	// 새로운 답글 쓰기
	private void writeReplyBoard(HttpServletRequest request, HttpServletResponse response) {

	}

	// 년/월/일 폴더생성
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// '/'로 해도 자바가 알아서 디렉토리 구분자로 인식함

		Date date = new Date();

		String str = sdf.format(date);

		// str = str.replace("-", File.separator); // 폴더 분할 형식으로 // 위에 yyyy/MM/dd 로
		// 수정했으므로 주석처리

		return str;
	}

	// 이미지 파일인지 확인
	private boolean checkImageType(File file) {

		boolean isImage = false;

		try {
			String contentType = Files.probeContentType(file.toPath());

			System.out.println("contentType : " + contentType); // image/jpg

			isImage = contentType.startsWith("image"); // ("image")로 시작하는지

		} catch (IOException e) {
			e.printStackTrace();
		}

		return isImage;
	}

}
