package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.MemberVO;
import com.example.mapper.MemberMapper;

@Service //@Component 계열 애노테이션 + 트랜잭션 처리 가능
@Transactional // 클래스에 선언하면 이 클래스에 있는 모든 메소드가 트랜잭션으로 동작함
public class MemberService {
	
	// @Autowired //멤버객체로 @Autowired로 하면 간단하지만 스프링에서만 사용가능해서 안되는게 많음(ex. text할때)
	private MemberMapper memberMapper;

	// 생성자로 의존객체를 받도록 선언하면 @Autowired 애노테이션 생략가능 
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	// 클래스 안(여러개의 DB를 묶음으로 처리해야할 때)에서 예외가 발생하면 rollback
	// 완료가 되어야 commit 됨
	public void register(MemberVO memberVO) {
		memberMapper.insert(memberVO);
	}
	
	public int deleteById(String id) {
		return memberMapper.deleteById(id);
	}
	
	public void modifyPasswd(MemberVO memberVO) {
		memberMapper.modifyPasswd(memberVO);
	}
	
	public void updateById(MemberVO memberVO) {
		memberMapper.updateById(memberVO);
	}
	
	public int getCountById(String id) {
		return memberMapper.getCountById(id);
	}
	
	public MemberVO getMemberById(String id) {
		return memberMapper.getMemberById(id);
	}
	
	public List<MemberVO> getMembers(){
		return memberMapper.getMembers();
	}

}
