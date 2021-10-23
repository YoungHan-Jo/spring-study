package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.example.domain.AttachVO;
import com.example.domain.Criteria;

public interface AttachMapper {

	@Delete("DELETE FROM attach")
	void deleteAll();
	
	@Insert("INSERT INTO attach (uuid, uploadpath, filename, filetype, bno) "
			+ "VALUES (#{uuid}, #{uploadpath}, #{filename}, #{filetype}, #{bno})")
	void addAttach(AttachVO attachVO);
	
	void addAttaches(List<AttachVO> attachList);
	
	@Select("SELECT * FROM attach WHERE uuid = #{uuid}")
	AttachVO getAttachByUuid(String uuid);

	List<AttachVO> getAttachesByUuids(List<String> uuidList);
	
	List<AttachVO> getAttachesByBno(int bno);
	
	@Select("SELECT * FROM attach WHERE uploadpath = #{uploadpath}")
	List<AttachVO> getAttachesByUploadpath(String uploadpath);
	
	@Delete("DELETE FROM attach WHERE bno = #{bno}")
	void deleteAttachesByBno(int bno);
	
	@Delete("DELETE FROM attach WHERE uuid = #{uuid}")
	void deleteAttachByUuid(String uuid);
	
	int deleteAttachesByUuids(List<String> uuidList);
	
	List<AttachVO> getImgAttachesByCri(Criteria cri);
	
	int getCountImgAttaches();
	
}
