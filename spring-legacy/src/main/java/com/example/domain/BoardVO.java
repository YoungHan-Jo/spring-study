package com.example.domain;

import java.sql.Timestamp;
import java.util.Date;

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

	
	
}
