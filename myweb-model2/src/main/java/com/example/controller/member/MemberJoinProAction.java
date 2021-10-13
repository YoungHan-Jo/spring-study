package com.example.controller.member;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import com.example.controller.Action;
import com.example.controller.ActionForward;
import com.example.domain.MemberVO;
import com.example.repository.MemberDAO;

// 회원가입 처리를 담당하는 컨트롤러 클래스
public class MemberJoinProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 처리...

		MemberVO memberVO = new MemberVO();

		memberVO.setId(request.getParameter("id"));
		memberVO.setPasswd(request.getParameter("passwd"));
		memberVO.setName(request.getParameter("name"));
		memberVO.setBirthday(request.getParameter("birthday"));
		memberVO.setGender(request.getParameter("gender"));
		memberVO.setEmail(request.getParameter("email"));
		memberVO.setRecvEmail(request.getParameter("recvEmail"));

		memberVO.setRegDate(new Timestamp(System.currentTimeMillis()));

		// 비밀번호를 jbcrypt 라이브러리 사용해서 암호화하여 저장하기
		String passwd = memberVO.getPasswd();
		String pwHash = BCrypt.hashpw(passwd, BCrypt.gensalt());
		memberVO.setPasswd(pwHash); // 암호화된 비밀번호 문자열로 수정하기

		String birthday = memberVO.getBirthday();
		birthday = birthday.replace("-", "");
		memberVO.setBirthday(birthday);

		MemberDAO memberDAO = MemberDAO.getInstance();

		memberDAO.insert(memberVO);
		
		// ====== 서버에서의 처리 완료 ======
		
		//forward 형식으로
//		ActionForward forward = new ActionForward();
//		forward.setRedirect(false);
//		forward.setPath("member/login");
		
		// 보안상 안전을 위해
		// 데이터를 받는 주소를 forward 형식으로 하면 새로고침할때 서버터짐.
		// 데이터를 받을때는 forward 말고 *redirect* 형식으로 해야함
		
		// 폼 태그로부터 사용자 입력값을 받아서 처리한 이후에
		// 응답을 줄때는 항상 리다이렉트 방식을 사용함. (사용자 입력값이 남아있음)
		// ex) 회원가입 후 주소가 남아있으면 새로고침 눌렀을 시, 재 요청 일어나기 때문에 서버 에러
		
		// 리다이렉트 방식1.
		//redirect 형식으로 해야함
//		ActionForward forward = new ActionForward();
//		forward.setRedirect(true);
//		forward.setPath("memberLogin.do");
//		return forward;
		
		//리다이렉트 방식2.
		// 자바스크립트 코드가 간단할 경우는 다음과같이 컨트롤러에서 바로 처리 가능함.
		response.setContentType("text/html; charset=UTF-8"); // jsp파일 제일위에 있는 contenttype // html로 인식
		PrintWriter out = response.getWriter(); // <%%> 출력스트림
		out.println("<script>");
		out.println("	alert('회원가입 성공');");
		out.println("	location.href = '/memberLogin.do';");
		out.println("</script>");
		out.flush(); // 출력스트림은 버퍼에 쌓여있어서 비워줘야함
		
		// 자바스크립트 코드가 여러줄로 아주 많을 경우에는
		// 별도의 jsp파일로 만들어서 forwarding 방식으로 
		// 자바스크립트 실행문이 들어있는 해당 jsp 파일을 바로 실행하는 방식이 좋음.
		
		return null;
	}

}
