package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.domain.MemberVO;

// DAO(Data Access Object) : 데이터 접근 오브젝트 역할

public class MemberDAO {

	// 싱글톤(singleton) 클래스 설계 : 객체 한개만 공유해서 사용하기
	private static MemberDAO instance;

	public static MemberDAO getInstance() { // 외부에서 호출할때 이 메소드 사용
		if(instance == null) {// 한 문장으로 instance 객체가 준비되지않을 때
			instance = new MemberDAO();
		}
		return instance;
	}

	private MemberDAO() {
	}
	

//==============================================================insert()=====================================================
	public void insert(MemberVO memberVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = JdbcUtils.getConnection();

			String sql = "";
			sql += "INSERT INTO member(id, passwd, name, birthday, gender, email, recv_email, reg_date) ";
			sql += " VALUES(?,?,?,?,?,?,?,?) ";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, memberVO.getId());
			pstmt.setString(2, memberVO.getPasswd());
			pstmt.setString(3, memberVO.getName());
			pstmt.setString(4, memberVO.getBirthday());
			pstmt.setString(5, memberVO.getGender());
			pstmt.setString(6, memberVO.getEmail());
			pstmt.setString(7, memberVO.getRecvEmail());
			pstmt.setTimestamp(8, memberVO.getRegDate());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt);
		}

	} // insert()

	// ==========================================================deleteAll()============================================
	public void deleteAll() {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = JdbcUtils.getConnection();

			String sql = "DELETE FROM member";

			pstmt = con.prepareStatement(sql);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt);
		}

	}

//=============================================================deleteById()====================================================

	public void deleteById(String id) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = JdbcUtils.getConnection();

			String sql = "";
			sql += " DELETE FROM member ";
			sql += " WHERE id = ? ";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt);
		}

	}// deleteById()

	// =====================================================updateById()======================================================

	public void updateById(MemberVO memberVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = JdbcUtils.getConnection();

			StringBuilder sb = new StringBuilder(); // 쓰레기 객체(String으로 계속 만드는거) 방지.
			sb.append(" UPDATE member ");
			sb.append(" SET name = ?, birthday = ?, gender = ?, email = ?, recv_email = ?, reg_date = ? ");
			sb.append(" WHERE id = ? ");

			pstmt = con.prepareStatement(sb.toString());
			pstmt.setString(1, memberVO.getName());
			pstmt.setString(2, memberVO.getBirthday());
			pstmt.setString(3, memberVO.getGender());
			pstmt.setString(4, memberVO.getEmail());
			pstmt.setString(5, memberVO.getRecvEmail());
			pstmt.setTimestamp(6, memberVO.getRegDate());
			pstmt.setString(7, memberVO.getId());

			pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt);
		}
	}// updateById()

	// =================================================================getMemberById()================================================

	public MemberVO getMemberById(String id) {
		MemberVO memberVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = JdbcUtils.getConnection();

			String sql = "SELECT * FROM member WHERE id = ? ";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				memberVO = new MemberVO();
				memberVO.setId(rs.getString("id"));
				memberVO.setPasswd(rs.getString("passwd"));
				memberVO.setName(rs.getString("name"));
				memberVO.setBirthday(rs.getString("birthday"));
				memberVO.setGender(rs.getString("gender"));
				memberVO.setEmail(rs.getString("email"));
				memberVO.setRecvEmail(rs.getString("recv_email"));
				memberVO.setRegDate(rs.getTimestamp("reg_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt, rs);
		}

		return memberVO;
	}

	// ================================================================getCountAll()============================================

	public int getCountAll() {
		int count = 0;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = JdbcUtils.getConnection();
			String sql = "SELECT COUNT(*) AS cnt FROM member ";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt, rs);
		}

		return count;
	}

	// ======================================================================getCountById()==========================================

	public int getCountById(String id) {
		int count = 0;

		Connection con = null; // 접속
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = JdbcUtils.getConnection();
			String sql = "";
			sql += " SELECT COUNT(*) ";
			sql += " FROM member ";
			sql += " WHERE id = ? ";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt("COUNT(*)");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt, rs);
		}
		return count;
	}

	// ============================================================getMembers()==========================================================

	public List<MemberVO> getMembers() {
		List<MemberVO> list = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = JdbcUtils.getConnection();
			String sql = "SELECT * FROM member ORDER BY name ";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				MemberVO memberVO = new MemberVO();
				memberVO.setId(rs.getString("id"));
				memberVO.setPasswd(rs.getString("passwd"));
				memberVO.setName(rs.getString("name"));
				memberVO.setBirthday(rs.getString("birthday"));
				memberVO.setGender(rs.getString("gender"));
				memberVO.setEmail(rs.getString("email"));
				memberVO.setRecvEmail(rs.getString("recv_email"));
				memberVO.setRegDate(rs.getTimestamp("reg_date"));

				list.add(memberVO); // list에 <- 생성된 memberVO를 담기
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt, rs);
		}

		return list;
	} //getMembers
	
	public List<Map<String, Object>> getCountPerGender(){
		List<Map<String, Object>> list = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = JdbcUtils.getConnection();
			String sql = "";
			sql += "SELECT CASE ";
			sql += " 		WHEN gender = 'F' then '여성' ";
			sql += "         WHEN gender = 'M' then '남성' ";
			sql += "         ELSE '모름' ";
			sql += " 		END AS gender_name, ";
			sql += "         count(*) AS cnt ";
			sql += " FROM member ";
			sql += " GROUP BY gender ";
			sql += " ORDER BY gender_name ";
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Map<String, Object> map = new HashMap<>();
				map.put("genderName",rs.getString("gender_name"));
				map.put("cnt", rs.getInt("cnt"));
				
				list.add(map);
			}


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcUtils.close(con, pstmt, rs);
		}
		
		
		return list;
	} //getCountPerGender
	
	

}
