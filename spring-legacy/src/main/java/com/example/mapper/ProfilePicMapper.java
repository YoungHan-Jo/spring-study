package com.example.mapper;

import com.example.domain.ProfilePicVO;

public interface ProfilePicMapper {

	int getCountById(String mid);
	
	void addProfilePic(ProfilePicVO profilePicVO);
	
	void updateProfilePic(ProfilePicVO profilePicVO);
	
	ProfilePicVO getProfilePic(String mid);
	
}
