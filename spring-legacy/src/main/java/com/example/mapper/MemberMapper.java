package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.example.domain.MemberVO;

public interface MemberMapper {

	// 기존에 sql문 pstmt 입력하던걸 여기서는 이렇게 , ???? 1234 set멤버변수() 하던걸 #{멤버변수명}으로 설정함
	@Insert("INSERT INTO member(id, passwd, name, birthday, gender, email, recv_email, reg_date) "
			+ "VALUES(#{id},#{passwd}, #{name}, #{birthday}, #{gender}, #{email}, #{recvEmail}, #{regDate})")
	int insert(MemberVO memberVO);

	@Delete("DELETE FROM member")
	int deleteAll();

	@Delete("DELETE FROM member WHERE id = #{id} ")
	int deleteById(String id);

	@Update("UPDATE member  "
			+ " SET name = #{name}, birthday = #{birthday}, gender = #{gender}, email = #{email}, recv_email = #{recvEmail}, reg_date = #{regDate} "
			+ " WHERE id = #{id} ")
	void updateById(MemberVO memberVO);
	
	@Select("SELECT count(*) AS cnt FROM member WHERE id = #{id}")
	int getCountById(String id);
	
	@Select("SELECT count(*) AS cnt FROM member")
	int getCountAll();
	
	@Select("SELECT * FROM member WHERE id = #{id} ")
	MemberVO getMemberById(String id);
	
	@Select("SELECT * FROM member ORDER BY id")
	List<MemberVO> getMembers();
}
