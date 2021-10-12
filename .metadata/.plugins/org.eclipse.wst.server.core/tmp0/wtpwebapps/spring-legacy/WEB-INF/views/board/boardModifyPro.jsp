<%@page import="com.example.domain.BoardVO"%>
<%@page import="net.coobird.thumbnailator.Thumbnailator"%>
<%@page import="java.util.UUID"%>
<%@page import="com.example.domain.AttachVO"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.example.repository.BoardDAO"%>
<%@page import="com.example.repository.AttachDAO"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@page import="java.io.IOException"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%!// 선언부 // 이 파일 자체에서만 반복해서 사용할 때
	String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		// '/'로 해도 자바가 알아서 디렉토리 구분자로 인식함

		Date date = new Date();

		String str = sdf.format(date);

		//str = str.replace("-", File.separator); // 폴더 분할 형식으로 // 위에 yyyy/MM/dd 로 수정했으므로 주석처리

		return str;
	}

	//이미지 파일인지 확인	
	boolean checkImageType(File file) {

		boolean isImage = false;

		try {
			String contentType = Files.probeContentType(file.toPath());

			System.out.println("contentType : " + contentType); // image/jpg

			isImage = contentType.startsWith("image"); // ("image")로 시작하는지

		} catch (IOException e) {
			e.printStackTrace();
		}

		return isImage;
	}%>


<%
String uploadFolder = "C:/jyh/upload"; // 업로드 기준 경로

File uploadPath = new File(uploadFolder, getFolder()); //"C:/jyh/upload/2021/08/03"

System.out.println("uploadPath : " + uploadPath.getPath());

if (uploadPath.exists() == false) {
	uploadPath.mkdirs();
}

//MultipartRequest 인자값
// 1.request
// 2.업로드할 물리적 경로 "C:/jyh/upload" <- \ 두번 쓰던가 or / 한번으로 가능
// 3.업로드 최대크기 바이트단위로 제한 //  1024Byte * 102Byte = 1MB
// 4. request의 텍스트 데이터, 파일명 인코딩 "utf-8"
// 5. 파일명 변경 정책. 파일명 중복시 이름변경 규칙 가진 객체를 전달

//파일 업로드하기
MultipartRequest multi = new MultipartRequest(request, uploadPath.getPath(), 1024 * 1024 * 50, "utf-8",
		new DefaultFileRenamePolicy());
//=== 파일 업로드 완료===

// AttachDAO 객체 준비
AttachDAO attachDAO = AttachDAO.getInstance();

//BoardDAO 객체준비
BoardDAO boardDAO = BoardDAO.getInstance();

//수정할 게시글 번호
int num = Integer.parseInt(multi.getParameter("num"));

// ==================신규 첨부파일 정보를 테이블에 insert 하기 =================

//input type="file" 태그의 name 속성들을 가져오기
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

	//AttachVO 객체 준비
	AttachVO attachVO = new AttachVO();

	attachVO.setFilename(filename);
	attachVO.setUploadpath(getFolder());
	attachVO.setBno(num);

	UUID uuid = UUID.randomUUID();
	attachVO.setUuid(uuid.toString()); // 기본키 uuid 저장

	File file = new File(uploadPath, filename); // 년월일 경로에 실제 파일명의 파일객체

	boolean isImage = checkImageType(file); // 이미지 파일 여부 확인

	attachVO.setFiletype((isImage == true) ? "I" : "O");

	//이미지 파일이면 썸네일 이미지 생성하기
	if (isImage == true) {
		File outFile = new File(uploadPath, "s_" + filename); // 출력할 썸네일 파일정보
		// (읽을 파일, 출력할 썸네일 파일, 넓이,높이)
		Thumbnailator.createThumbnail(file, outFile, 100, 100); // 썸네일 생성
	}

	// 첨부파일 attach 테이블에 attachVO를 insert 하기
	attachDAO.addAttach(attachVO);

} //while

//==============신규 첨부파일 정보를 테이블에 insert 하기 완료 =================

//==============삭제할 첨부파일 정보를 삭제하기 =====================
String[] delFilesUuid = multi.getParameterValues("delfile");

if (delFilesUuid != null) {
	for (String uuid : delFilesUuid) {

		// 첨부파일 uuid에 해당하는 첨부파일 VO객체 가져오기
		AttachVO attach = attachDAO.getAttachByUuid(uuid);

		String path = uploadFolder + "/" + attach.getUploadpath() + "/" + attach.getFilename();
		File deleteFile = new File(path);

		if (deleteFile.exists()) { // 삭제할 파일이 존재하면
		deleteFile.delete(); // 파일 삭제하기
		} //if

		if (attach.getFiletype().equals("I")) { // 이미지 파일이면 썸네일 파일도 지움
				String thumbnailPath = uploadFolder + "/" + attach.getUploadpath() + "/s_" + attach.getFilename();
				File thumbnailFile = new File(thumbnailPath);
				if (thumbnailFile.exists()) { // 삭제할 파일이 존재하면
					thumbnailFile.delete(); // 파일 삭제하기
				}
		} //if

		// DB에서 uuid에 해당하는 첨부파일정보 삭제하기
		attachDAO.deleteAttachesByUuid(uuid);

	} // for
}

//=====================삭제할 첨부파일 정보를 삭제하기 완료 ========================

//===================== board 테이블 게시글 수정하기 ========================
// 수정에 사용할 게시글 VO 객체 준비
BoardVO boardVO = new BoardVO();

//파라미터값 가져와서 VO에 저장
boardVO.setNum(num);
boardVO.setSubject(multi.getParameter("subject"));
boardVO.setContent(multi.getParameter("content"));
boardVO.setIpaddr(request.getRemoteAddr());

// DB에 게시글 수정하기
boardDAO.updateBoard(boardVO);

//===================== board 테이블 게시글 수정하기 완료 ========================

String pageNum = multi.getParameter("pageNum");

// 수정 후 글목록 화면으로 이동
//response.sendRedirect("/board/boardList.jsp?pageNum=" + pageNum);

// 상세보기화면으로 이동
response.sendRedirect("/board/boardContent.jsp?num=" + num + "&pageNum=" + pageNum);
%>