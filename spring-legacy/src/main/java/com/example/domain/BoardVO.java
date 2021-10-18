package com.example.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BoardVO {
	
	// null 값을 저장하기때문에 Integer로 사용, 서버는 메모리가 넉넉해서 괜찮
	
	private Integer num;
	private String mid;
	private String subject;
	private String content;
	private Integer readcount;
	private Date regDate;
	private String ipaddr;
	private Integer reRef;
	private Integer reLev;
	private Integer reSeq;

	// private AttachVO attachVO; //SQL JOIN 쿼리에서 1:1 관계
	
	private List<AttachVO> attachList; // SQL JOIN 쿼리에서 1:N의 관계. N이 List로 들어옴
	// N의 내용만 값을 하나씩 attachVO 접어넣고
	// 1은 다 같은 내용이라서 마지막 행을 boardVO로 집어넣기
	// BoardVO 클래스에 List<AttachVO> 멤버변수를 선언하면
	// 이런 방식으로 mybatis가 자동으로 해줌. 
	
	
}
