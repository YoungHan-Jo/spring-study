package com.example.aop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.domain.MemberVO;
import com.example.mapper.MemberMapper;
import com.example.service.MemberService;

public class MemberServiceProxy extends MemberService {

	// proxy pattern 원리
	// 서비스 클래스를 상속 받아.
	// MemberService 빈을 MemberServiceProxy로 자동으로 바꿔버림
	// 컨트롤러는 그냥 멤버서비스를 사용한다고 생각함.
	
	// 그냥 AOP @Around 사용하는게 편함
	
	@Autowired
	private LogAdvice logAdvice;
	@Autowired
	private MemberService memberService;
	
	public MemberServiceProxy(MemberMapper memberMapper) {
		super(memberMapper);
	}

	@Override
	public void register(MemberVO memberVO) {
		
		logAdvice.logBefore();
		
		// TODO Auto-generated method stub
		super.register(memberVO);
	}

	@Override
	public int deleteById(String id) {
		// TODO Auto-generated method stub
		return super.deleteById(id);
	}

	@Override
	public void modifyPasswd(MemberVO memberVO) {
		// TODO Auto-generated method stub
		super.modifyPasswd(memberVO);
	}

	@Override
	public void updateById(MemberVO memberVO) {
		// TODO Auto-generated method stub
		super.updateById(memberVO);
	}

	@Override
	public int getCountById(String id) {
		// TODO Auto-generated method stub
		return super.getCountById(id);
	}

	@Override
	public MemberVO getMemberById(String id) {
		// TODO Auto-generated method stub
		return super.getMemberById(id);
	}

	@Override
	public List<MemberVO> getMembers() {
		// TODO Auto-generated method stub
		return super.getMembers();
	}
	
	
	
}
