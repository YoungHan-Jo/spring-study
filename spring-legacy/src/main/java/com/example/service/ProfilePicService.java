package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.ProfilePicVO;
import com.example.mapper.ProfilePicMapper;

@Service
@Transactional
public class ProfilePicService {
	
	private ProfilePicMapper profilePicMapper;

	public ProfilePicService(ProfilePicMapper profilePicMapper) {
		super();
		this.profilePicMapper = profilePicMapper;
	}
	
	public ProfilePicVO getProfilePic(String mid) {
		return profilePicMapper.getProfilePic(mid);
	}
	
	public void deleteByMid(String mid) {
		profilePicMapper.deleteByMid(mid); 
	}

}
