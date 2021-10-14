package com.example.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Criteria {

	private int pageNum; // 페이지번호
	private int amount; // 한 페이지당 글 개수

	private int startRow; // 가져올 글의 시작 행번호

	private String type = ""; // 검색 유형
	private String keyword = ""; // 검색어

	public Criteria() {
		this(1, 10);
	}

	public Criteria(int pageNum, int amount) {
		super();
		this.pageNum = pageNum;
		this.amount = amount;

		// 1페이지 0행부터
		// 2페이지 10행부터 시작
		// 3페이지 20행부터 시작
		// 4페이지 30행부터 시작
		this.startRow = (this.pageNum - 1) * this.amount;
	}// 생성자
}
