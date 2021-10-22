package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.AttachVO;
import com.example.mapper.AttachMapper;

@Service
@Transactional
public class AttachService {
	
	@Autowired
	private AttachMapper attachMapper;
	
	public List<AttachVO> getAttachesByBno(int bno){
		return attachMapper.getAttachesByBno(bno);
	}
	
	public List<AttachVO> getAttachesByUuids(List<String> uuidList){
		return attachMapper.getAttachesByUuids(uuidList);
	}
	
	public int deleteAttachesByUuids(List<String> uuidList) {
		return attachMapper.deleteAttachesByUuids(uuidList);
	}
	
	public List<AttachVO> getAttachesByUploadpath(String uploadpath){
		return attachMapper.getAttachesByUploadpath(uploadpath);
	}
}
