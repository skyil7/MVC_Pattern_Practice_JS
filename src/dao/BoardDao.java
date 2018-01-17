package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Board;
import jdbc.JdbcUtil;

public class BoardDao {
	private static BoardDao bd = new BoardDao();

	public static BoardDao getInstance() {
		return bd;
	}

	private BoardDao() {
	}

	public void insert(Connection conn, Board board) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("INSERT INTO board(title, writer, date, content) VALUES (?,?,?,?)");
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getWriter());
			pstmt.setDate(3, board.getDate());
			pstmt.setString(4, board.getContent());

			pstmt.executeUpdate();
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	private Board makeBoardFromRs(ResultSet rs) throws SQLException {
		Board board = new Board();

		board.setId(rs.getInt("id"));
		board.setTitle(rs.getString("title"));
		board.setWriter(rs.getString("writer"));
		board.setDate(rs.getDate("date"));
		board.setContent(rs.getString("content"));

		return board;
	}

	public List<Board> getList(Connection conn, int start, int end) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		List<Board> boards = new ArrayList<Board>();

		try {
			pstmt = conn.prepareStatement("SELECT * FROM board ORDER BY ID DESC LIMIT ?,?");
			
			System.out.println(start);
			System.out.println(end);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				boards.add(makeBoardFromRs(rs));
			}
			return boards;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	public Board getBoard(Connection conn, int id) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn.prepareStatement("SELECT * FROM board WHERE ID = ?");
			pstmt.setInt(0, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				return makeBoardFromRs(rs);
			}
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		return null;
	}

	public int getTotalCnt(Connection conn) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement("SELECT count(*) as cnt from board");
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("cnt");
			}
			return 0;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

}
