package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BoardDAO {
	// DB 드라이버
	final String JDBC_DRIVER = "org.h2.Driver";
	// DB URL(내 컴퓨터니까 LOCALHOST 남이 치면 IP주소 치고 와야함)
	final String JDBC_URL = "jdbc:h2:tcp://localhost/~/jwbookdb";
	
	Page page;

	// 연결한드아
	public Connection open() {
		Connection conn = null;
		try {
			// 클래스의 정보를 가져오는 것.
			Class.forName(JDBC_DRIVER);
			// ID, PW
			conn = DriverManager.getConnection(JDBC_URL, "jwbook", "1234");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	//column 총 줄 수
	public List<Board> getMainView(int page) throws Exception {
		Connection conn = open();
		List<Board> boardList = new ArrayList<>();	
		
		
	
		//게시글 끝에서 20번, 그게 아니면 0
		int startNum = ((page-1)*15)+1;
		//총 게시글
		int endNum = startNum + 14;

		System.out.println("startNum : " + startNum +" endNum : " + endNum);

		String sql = "select rownum1, aid, title, userId, to_char(date,'yyyy-mm-dd') as date, boardnumber, views from view1 where rownum1 between ? and ?;";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,startNum);
		pstmt.setInt(2, endNum);
		ResultSet rs = pstmt.executeQuery();
	
		
		//게시글 총 끝 숫자	
		try (conn; pstmt; rs) {
			while (rs.next()) {
				Board b = new Board();
				b.setAid(rs.getInt("aid"));
				b.setTitle(rs.getString("title"));
				b.setUserId(rs.getString("userId"));
				b.setDate(rs.getString("date"));
				b.setBoardNumber(rs.getInt("boardnumber"));
				b.setViews(rs.getInt("views"));

				boardList.add(b);
				
				System.out.println("게시글 목록 불러오기 성공");
			}
			return boardList;
		}
	}
	
	//드라마만. 나중에 게시판별로 구분하기 위해선 view 이름이 달라야함
	public List<Board> getBoardList(int boardNumber,int page) throws Exception {
		Connection conn = open();
		List<Board> boardList = new ArrayList<>();
		
		//게시글 끝에서 20번, 그게 아니면 0
		//수정 필요
		//게시글 끝에서 20번, 그게 아니면 0
		int startNum = ((page-1)*15)+1;
		//총 게시글
		int endNum = startNum + 14;
		
		String sql = "select rownum2, aid, title, userId, to_char(date,'yyyy-mm-dd') as date, boardnumber, "
				+ "views from view2 where rownum2 between ? and ?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1,startNum);
		pstmt.setInt(2,endNum);
		
		ResultSet rs = pstmt.executeQuery();

		try (conn; pstmt; rs) {
			while (rs.next()) {
				Board b = new Board();
				b.setAid(rs.getInt("aid"));
				b.setTitle(rs.getString("title"));
				b.setUserId(rs.getString("userId"));
				b.setDate(rs.getString("date"));
				b.setViews(rs.getInt("views"));
				b.setBoardNumber(rs.getInt("boardnumber"));

				boardList.add(b);
				
				System.out.println("게시글 목록 불러오기 성공");
			}
			return boardList;
		}
	}

	public Board getBoard(int boardNumber, int aid) throws SQLException {
		Connection conn = open();

		Board b = new Board();
		String sql = "select aid, title, img, to_char(date,'yyyy-mm-dd') as date, userId, views, content,"
				+ "boardnumber from board where boardnumber=? and aid=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		System.out.println(boardNumber + "     " + aid);
		pstmt.setInt(1, boardNumber);
		pstmt.setInt(2, aid);
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			try (conn; pstmt; rs) {
				b.setAid(rs.getInt("aid"));
				b.setTitle(rs.getString("title"));
				b.setImg("/img/"+rs.getString("img"));
				System.out.println(b.getImg());
				b.setDate(rs.getString("date"));
				b.setUserId(rs.getString("userId"));
				b.setViews(rs.getInt("views"));
				b.setContent(rs.getString("content"));
				b.setBoardNumber(rs.getInt("boardnumber"));
				System.out.println("게시글 조회 성공");
				
				//조회수 올리는 로직
				viewsNumber(rs.getInt("aid"),rs.getInt("views"));
				return b;
			}
		} else {
			return null;
		}
	}
	
	public void updateBoard(Board b) throws Exception {
		Connection conn = open();
		
		String sql = "update board  set "
				+ "title= ?, img= ?, content=?"
				+ "where aid = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getImg());
			pstmt.setString(3, b.getContent());
			pstmt.setInt(4, b.getAid());
			pstmt.executeUpdate();
			
			System.out.println("게시글 수정 성공");
		}
	}
	
	//삭제 필요??
	public void addBoard(Board b) throws Exception {
		Connection conn = open();
		
		String sql = "insert into board(title, img, date, content) values (?, ?, CURRENT_TIMESTAMP(), ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getImg());
			pstmt.setString(3, b.getContent());
			pstmt.executeUpdate();
			System.out.println("게시글 추가 성공");
		}
	}
	
	public void delBoard(int aid) throws SQLException {
		Connection conn = open();
		
		String sql = "delete from board where aid=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setInt(1, aid);
			//삭제할 뉴스 기사가 없는경우
			if(pstmt.executeUpdate() == 0) {
				System.out.println("게시글 삭제할 수 없습니다.");
				throw new SQLException("DB에러");
			}
				System.out.println("게시글 삭제 성공");
		}
	}
	
	public int boardTotalNumber() throws SQLException{
			Connection conn = open();
			
			String sql = "select count(*) from board;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
			try(conn;pstmt;rs){
				int boardTotalNumber = rs.getInt("count(*)");
				System.out.println("boardTOtalNumber :" + boardTotalNumber);
				System.out.println("게시글 총 숫자 불러오기 성공");
				return boardTotalNumber;
				
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("게시글 총 숫자 불러오기 실패");
				return 0;
			}
			}else return 0;
	}
	
	public int boardTotalNumber(int boardNumber) throws SQLException{
		Connection conn = open();
		
		String sql = "select count(*) from board where boardnumber = ?" ;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, boardNumber);
		ResultSet rs = pstmt.executeQuery();

		if(rs.next()) {
		try(conn;pstmt;rs){
			int boardTotalNumber = rs.getInt("count(*)");
			System.out.println("게시글 총 숫자 불러오기 성공");
			return boardTotalNumber;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("게시글 총 숫자 불러오기 실패");
			return 0;
		}
		}else return 0;
}
	//검색 총 숫자;
	public int searchTotalNumber(String title) throws SQLException{
		Connection conn = open();
		
		String sql = "select count(*) from board where title like concat ('%', ?, '%')  " ;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, title);
		ResultSet rs = pstmt.executeQuery();

		if(rs.next()) {
		try(conn;pstmt;rs){
			int boardTotalNumber = rs.getInt("count(*)");
			System.out.println("게시글 총 숫자 불러오기 성공");
			return boardTotalNumber;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("게시글 총 숫자 불러오기 실패");
			return 0;
		}
		}else return 0;
}

	public int searchBoardNumber(String title,int boardNumber) throws SQLException{
		Connection conn = open();
		
		String sql = "select count(*) from board where boardnumber=? and title like concat ('%', ?, '%')" ;
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,boardNumber);
		pstmt.setString(2, title);
		ResultSet rs = pstmt.executeQuery();

		if(rs.next()) {
		try(conn;pstmt;rs){
			int boardTotalNumber = rs.getInt("count(*)");
			
			System.out.println(" boardTotalNumber " +  boardTotalNumber + " boardNumber  " + boardNumber + "title" + title);
			
			System.out.println("게시글 총 숫자 불러오기 성공");
			return boardTotalNumber;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("게시글 총 숫자 불러오기 실패");
			return 0;
		}
		}else return 0;
}
	
	
	
	
	public void viewsNumber(int aid,int views) throws SQLException {
		Connection conn = open();
		views += 1;
		String sql = "update board set views=? where aid=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,views);
		pstmt.setInt(2, aid);
		pstmt.executeUpdate();
		System.out.println("게시글 조회수 업데이트 완료");

		}
	
	public void write(Board board,String userId) throws SQLException{
		Connection conn = open();
		
		String sql = "insert into board(boardnumber,title,userid,views,date,img,content) "
				+ "values(?,?,?,?,CURRENT_TIMESTAMP(),?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		System.out.println("board.getImg"+board.getImg());
		
		try(conn;pstmt){
			pstmt.setInt(1, board.getBoardNumber());
			pstmt.setString(2, board.getTitle());
			pstmt.setString(3, userId);
			pstmt.setInt(4, 0);
			
			if(board.getImg() != null) {
				pstmt.setString(5,board.getImg());
			}else {
				pstmt.setString(5,"");}
			
			pstmt.setString(6, board.getContent());
			pstmt.executeUpdate();
			System.out.println("글쓰기 성공");
			
			log(userId,"write");
			
		}catch(Exception e) {
			System.out.println("글쓰기 실패");
			
		}
	}
	
	
	public void edit(Board board,String userId) throws SQLException{
		Connection conn = open();
		
		String sql = "update board set "
				+ "title=?,img=?,content=? where aid=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn;pstmt){
			pstmt.setString(1, board.getTitle());
			
			if(board.getImg() != null) {
				pstmt.setString(2,board.getImg());
			}else {
				pstmt.setString(2,"");}
			
			pstmt.setString(3, board.getContent());
			pstmt.setInt(4, board.getAid());
			pstmt.executeUpdate();
			System.out.println("글 수정 성공");	
			
			log(userId,"edit");
			
		}catch(Exception e) {
			System.out.println("글 실패 실패");
			
		}
	}
	
	public void delete(String userId,int aid) throws SQLException{
		Connection conn = open();
		String sql = "delete board where aid=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		try(conn;pstmt){
			pstmt.setInt(1, aid);
			pstmt.executeUpdate();
			System.out.println("글 삭제 성공");
			log(userId, "delete");
		}catch(Exception e) {
			System.out.println("글 삭제 실패");
		}
	}
	
	
	//전체 게시판 대상으로 검색
	public List<Board> searchTotalList(String title,int page) throws Exception {
		Connection conn = open();
		List<Board> boardList = new ArrayList<>();
		
		//게시글 끝에서 20번, 그게 아니면 0
		int startNum = ((page-1)*15)+1;
		//총 게시글
		int endNum = startNum + 14;
		
		String sql = "with \r\n"
				+ "a as (select rownum as rownum1, b.aid, b.title, b.userId, to_char(b.date,'yyyy-mm-dd') as date, "
				+ "b.views,b.boardnumber from board b where title like concat ('%', ?, '%'))\r\n"
				+ "select * from a where rownum1 between ? and ? order by rownum1 desc";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, title);
		pstmt.setInt(2,startNum);
		pstmt.setInt(3,endNum);
		
		ResultSet rs = pstmt.executeQuery();

		try (conn; pstmt; rs) {
			while (rs.next()) {
				Board b = new Board();
				b.setAid(rs.getInt("aid"));
				b.setTitle(rs.getString("title"));
				b.setUserId(rs.getString("userId"));
				b.setDate(rs.getString("date"));
				b.setViews(rs.getInt("views"));
				b.setBoardNumber(rs.getInt("boardnumber"));

				boardList.add(b);
				
				System.out.println("게시글 목록 불러오기 성공");
			}
			return boardList;
		}
	}
	
	//일부 게시판 대상으로 검색
		public List<Board> searchBoardList(String title, int boardNumber, int page) throws Exception {
			Connection conn = open();
			List<Board> boardList = new ArrayList<>();
				
			//게시글 끝에서 20번, 그게 아니면 0
			int startNum = ((page-1)*15)+1;
			//총 게시글
			int endNum = startNum + 14;
		
			System.out.println("startNum : " + startNum +  " endNum : " + endNum);
			
			String sql = "with \r\n"
					+ "a as (select rownum as rownum1, b.aid, b.title, b.userId, to_char(b.date,'yyyy-mm-dd') as date, "
					+ "b.views,b.boardnumber from board b where boardnumber = ? and title like concat ('%', ?, '%'))\r\n"
					+ "select * from a where rownum1 between ? and ? order by rownum1 desc";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNumber);
			pstmt.setString(2, title);
			pstmt.setInt(3,startNum);
			pstmt.setInt(4,endNum);
			
			ResultSet rs = pstmt.executeQuery();

			try (conn; pstmt; rs) {
				while (rs.next()) {
					Board b = new Board();
					b.setAid(rs.getInt("aid"));
					b.setTitle(rs.getString("title"));
					b.setUserId(rs.getString("userId"));
					b.setDate(rs.getString("date"));
					b.setViews(rs.getInt("views"));
					b.setBoardNumber(rs.getInt("boardnumber"));

					boardList.add(b);
					
					System.out.println("게시글 목록 불러오기 성공");
				}
				return boardList;
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
				}case "write":{
					pstmt.setInt(2, 50);
					System.out.println("회원글쓰기 로그 기록");
					break;
				}case "edit":{
					pstmt.setInt(2, 60);
					System.out.println("회원글수정 로그 기록");
					break;
				}case "delete":{
					pstmt.setInt(2, 70);
					System.out.println("회원글삭제 로그 기록");
					break;
				}
				}
				pstmt.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
