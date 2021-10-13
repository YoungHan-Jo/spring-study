package com.example.domain;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MemberVO {

	private String id;
	private String passwd;
	private String name;
	private String birthday;
	private String gender;
	private String email;
	private String recvEmail;
	private Timestamp regDate;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MemberVO [id=").append(id).append(", passwd=").append(passwd).append(", name=").append(name)
				.append(", birthday=").append(birthday).append(", gender=").append(gender).append(", email=")
				.append(email).append(", recvEmail=").append(recvEmail).append(", regDate=").append(regDate)
				.append("]");
		return builder.toString();
	}
}
