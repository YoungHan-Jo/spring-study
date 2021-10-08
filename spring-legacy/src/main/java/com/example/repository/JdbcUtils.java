package com.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcUtils {

	public static final String URL = "jdbc:mysql://localhost:3306/jspdb?useUnicode=true&characterEncoding=utf8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul";
	public static final String USER = "jspid";
	public static final String PASSWD = "jsppass";

	public static Connection getConnection() throws Exception {// 호출하는곳에서 try 직접처리 하고 있기 때문에
																// 던지기
		Connection con = null;

		Class.forName("com.mysql.cj.jdbc.Driver");

		con = DriverManager.getConnection(URL, USER, PASSWD);

//		// =====커넥션 풀링으로 준비된 커넥션 객체들 중에서 한개를 빌려오기 ====
//		Context context = new InitialContext();
//		DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/jspdb"); // 키 입력해서 값 가져오기
//		con = ds.getConnection(); // 빌려오기

		return con;
	}

	public static void close(Connection con, PreparedStatement pstmt) {
		close(con, pstmt, null);
	}

	public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try { // catch 는
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	} // close()
}
