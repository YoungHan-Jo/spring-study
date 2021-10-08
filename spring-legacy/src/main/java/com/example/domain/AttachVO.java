package com.example.domain;

import lombok.Data;

@Data
public class AttachVO {
	
	private String uuid;
	private String uploadpath;
	private String filename;
	private String filetype; // 파일타입. 이미지"I", 아니면 "O"
	private int bno; // 첨부파일을 포함하고있는 게시글 번호
}
