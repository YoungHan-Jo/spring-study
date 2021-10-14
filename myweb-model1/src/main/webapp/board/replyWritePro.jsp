<%@page import="net.coobird.thumbnailator.Thumbnailator"%>
<%@page import="java.io.IOException"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.util.UUID"%>
<%@page import="com.example.domain.AttachVO"%>
<%@page import="java.util.Enumeration"%>
<%@page import="com.example.repository.AttachDAO"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.example.repository.BoardDAO"%>
<%@page import="com.example.domain.BoardVO"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
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
// 파일 업로드 완료

// form으로부터 enctype="multipart/form-data" 로 전송받으면
// 기본내장객체인 request로 부터 파라미터값을 바로 가져올 수 없음! null이 리턴됨
// MultipartRequest 객체로부터 파라미터 값을 가져와야함. 사용방법은 rquest와 동일함

// AttachDAO 객체 준비
AttachDAO attachDAO = AttachDAO.getInstance();

//BoardDAO 객체준비
BoardDAO boardDAO = BoardDAO.getInstance();

// insert할 새 게시글 번호 가져오기
int num = boardDAO.getNextnum();

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

	attachDAO.addAttach(attachVO);

} //while


//======================== 답글 DB에 board 추가하기 ============================
	
// 답글 BoardVO 객체 준비
BoardVO boardVO = new BoardVO();

// 파라미터값 가져와서 VO에 저장. MultipartRequest 로 부터 가져옴
boardVO.setMid(multi.getParameter("id"));
boardVO.setSubject(multi.getParameter("subject"));
boardVO.setContent(multi.getParameter("content"));

//글 번호 설정
boardVO.setNum(num);

//ipaddr regDate readcount
boardVO.setIpaddr(request.getRemoteAddr()); //ip 주소 String으로 가져오기
boardVO.setRegDate(new Timestamp(System.currentTimeMillis()));
boardVO.setReadcount(0);

// 답글을 작성할 대상글의  re_ref re_lev re_seq 설정하기
boardVO.setReRef(Integer.parseInt(multi.getParameter("reRef"))); 
boardVO.setReLev(Integer.parseInt(multi.getParameter("reLev"))); 
boardVO.setReSeq(Integer.parseInt(multi.getParameter("reSeq"))); 

//글 등록하기 전에 update로 re_seq 새로 정렬하기
boardDAO.updqteReSeqAndAddReply(boardVO);

//요청 페이지번호.파라미터 가져오기
String pageNum = multi.getParameter("pageNum");

//글목록으로 이동
//response.sendRedirect("/board/boardList.jsp?pageNum="+pageNum);

//글 상세보기 화면으로 이동// 글번호 페이지번호 같이 넣어서 보내기
response.sendRedirect("/board/boardContent.jsp?num=" + boardVO.getNum() + "&pageNum=" + pageNum);
%>






