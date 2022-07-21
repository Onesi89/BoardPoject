package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

@Component
public class MemberDAO {
	final String JDBC_DRIVER = "org.h2.Driver";
	final String JDBC_URL = "jdbc:h2:tcp://localhost/~/jwbookdb";
	
	public Connection open() {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL, "jwbook", "1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public boolean addMember(Member member) throws Exception{
		boolean check = memberIdSearch(member.getUserId());
		
		if(!check) {
		Connection conn = open();

		String sql = "insert into member(id,password,name,email,date,hash)";
			   sql += "values(?,?,?,?,sysdate,?)";
			   
	    System.out.println("가입한 회원 아이디 : " + member.getUserId());
	    System.out.println("가입한 회원 암호 : " + member.getPassword());
	    System.out.println(member.getName());
	    System.out.println(member.getEmail());
		
		String salt = getSalt();
		String finalpassword = getPassword(member.getPassword(), salt);
	
	    PreparedStatement pstmt = conn.prepareStatement(sql);
	    
	    try(conn;pstmt){
	    	pstmt.setString(1, member.getUserId());
	    	//암호 해싱
	    	pstmt.setString(2, finalpassword);
	    	pstmt.setString(3, member.getName());
	    	pstmt.setString(4, member.getEmail());
	    	//솔트 생성
	    	pstmt.setString(5,salt);
	    	pstmt.executeUpdate();
	    }catch(Exception e) {
	    	System.out.println("DB회원가입 문제 발생");
	    }
	    
	    log(member.getUserId(),"signup");
	    System.out.println("DB 회원가입 처리 완료");
	    
	    return true; 
	    
		}else {
			System.out.println("ID 중복");
			
			return false;
		}
	}
	
	public String login(Member member) throws SQLException {
		Connection conn = open();
		String sql = "select id, password, hash from member ";
			   sql += "where id = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, member.getUserId());
		ResultSet rs = pstmt.executeQuery();
		String alert = "";
		
		try(conn;pstmt;rs){
			if(!rs.next()) {
				//"ID가 존재하지 않습니다.
				alert = "noneID";
				return alert;
			}
			
			String secretPw = rs.getString("password");
			String hash = rs.getString("hash");
			String password = getPassword(member.getPassword(),hash);
			
			System.out.println(password);
			
			System.out.println("비밀번호 일치 여부 : " + (password.equals(secretPw)));
			
			if(!(password.equals(secretPw))){
				alert = "noneID";
				return alert;
			};	
			
		}catch(Exception e) {
				e.printStackTrace();
		}	
		
		//log 기록
		log(member.getUserId(),"login");
		alert = "sucessLogin";
		
		
		return alert;	
	}
	
	//해싱과 솔트
	public static String getPassword(String password, String salt){
		String encriptPassword = "";
	    try {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        digest.reset();
	        digest.update(salt.getBytes());
	        byte[] input = digest.digest(password.getBytes("UTF-8"));
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < input.length; i++) {
	            digest.reset();
	            sb.append(Integer.toString((input[i] & 0xff) + 0x100, 16)
                        .substring(1));
	        }
	       
	        encriptPassword = sb.toString();
	    }catch(NoSuchAlgorithmException | UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    return encriptPassword;
	}
	
	
	public static String getSalt() {
	    String value = "";
	    try {
	        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	        // Salt generation 128 bits long
	        byte[] salt = new byte[16];
	        secureRandom.nextBytes(salt);
	        value = salt.toString();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return value;
	}
	

	public void logout(Member m){
		try {
			System.out.println(m.getUserId());
			log(m.getUserId(),"logout");
			m = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void log(String memberID, String logInfo) throws SQLException {
		
		
		Connection conn = open();
		String sql = "insert into log(id,loginfo_number,date) ";
		sql += "values(?,?,current_timestamp());";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		try (conn; pstmt;) {
		
			pstmt.setString(1, memberID);

			switch (logInfo) {
			case "login": {
				pstmt.setInt(2, 10);
				System.out.println("로그인 로그 기록");
				break;
			}
			case "logout": {
				pstmt.setInt(2, 20);
				System.out.println("로그아웃 로그 기록");
				break;
			}case "signup":{
				pstmt.setInt(2, 30);
				System.out.println("회원가입 로그 기록");
				break;
			}case "memberUpdate":{
				pstmt.setInt(2, 40);
				System.out.println("회원수정 로그 기록");
				break;
			}case "memberDelete":{
				pstmt.setInt(2, 99);
				System.out.println("회원삭제 로그 기록");
				break;
			}
			}
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean memberIdSearch(String memberID) throws SQLException {
		Connection conn = open();
		String sql = "select id from member where id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberID);
		ResultSet rs = pstmt.executeQuery();
		
		try(conn;pstmt;rs;){
			if(rs.next()) {
			return true;
		}
		}catch(SQLException e) {e.printStackTrace();}
		return false;		
	}
	
	public Member memberSearch(String memberID) throws SQLException {
			
			Connection conn = open();
			String sql = "select id,name,email,date,nvl(totalwrite,0) as totalwrite,nvl(totalcomment,0) as totalcomment from member where id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberID);
			ResultSet rs = pstmt.executeQuery();
			
			try(conn;pstmt;rs){
				if(rs.next()) {
					Member member = new Member();
					member.setUserId(rs.getString("id"));		
					member.setName(rs.getString("name"));
					member.setEmail(rs.getString("email"));
					member.setDate(rs.getTimestamp("date").toString());
					member.setTotalWriteNumber(rs.getInt("totalwrite"));
					member.setTotalCommentNumber(rs.getInt("totalcomment"));
					
					return member;
				}
			}catch(SQLException e) {
				e.printStackTrace();
				System.out.println("회원 정보 조회 실패");
			}
			
			//시스템이 멈추지 않게 빈 객체로 전달.
			return new Member();
	}
	
	public boolean memberUpdate(Member member) throws SQLException{
		Connection conn = open();
		String sql = "update member set password=?,name=?,email=?,hash=? where id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		String salt = getSalt();
		String finalpassword = getPassword(member.getPassword(), salt);
		
		try(conn;pstmt;){
			pstmt.setString(1,finalpassword);
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, salt);
			pstmt.setString(5, member.getUserId());
			pstmt.executeUpdate();
	
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("회원 정보 업데이트 실패");
			return false;
		}
		
		System.out.println("회원 정보 업데이트 성공");
		log(member.getUserId(),"memberUpdate");
		return true;
	}
	
	public boolean memberDelete(String memberId) throws SQLException{
		Connection conn = open();
		String sql = "delete member where id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);	
		try(conn;pstmt;){
			pstmt.setString(1, memberId);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			System.out.println("회원 정보 삭제 실패");
			return false;
		}
		
		System.out.println("회원 정보 삭제 성공");
		log(memberId,"memberDelete");
		return true;
		

	}
	
	public void memberWrite(Member member) throws SQLException{
		Connection conn = open();
		
		int memberTotlaWriteNumber = memberTotalWriteSearch(member.getUserId())+1;	
		
		member.setTotalWriteNumber(memberTotlaWriteNumber);
		
		String sql = "update member set TOTALWRITE=? where id=?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);

		
		try(conn;pstmt){
			pstmt.setInt(1, memberTotlaWriteNumber);
			pstmt.setString(2,member.getUserId());
			pstmt.executeUpdate();
			System.out.println("회원이 글을 작성 했습니다.");
		}catch(Exception e) {
			System.out.println("회원이 글을 작성 실패했습니다.");
		}
	}
	
	public int memberTotalWriteSearch(String memberID) throws SQLException {
		Connection conn = open();
		String sql = "select nvl(TOTALWRITE,0) as TOTALWRITE from member where id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberID);
		ResultSet rs = pstmt.executeQuery();
		
		try(conn;pstmt;rs;){
			if(rs.next()) {
				int memberTotalWriteSearch = Integer.parseInt(rs.getString("TOTALWRITE"));
			return memberTotalWriteSearch;
		}
		}catch(SQLException e) {e.printStackTrace();}
		return 0;		
	}
	
	
	
}
	
