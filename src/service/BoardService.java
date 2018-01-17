package service;

import java.sql.Connection;
import java.util.List;

import dao.BoardDao;
import domain.Board;
import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;

public class BoardService {
	private static BoardService bs = new BoardService();

	public static BoardService getInstance() {
		return bs;
	}

	private BoardService() {
	}

	public List<Board> getList(int start, int cnt) {
		Connection conn = null;

		try {
			conn = ConnectionProvider.getConnection();

			List<Board> list = BoardDao.getInstance().getList(conn, start, cnt);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	public int write(Board data) {
		Connection conn = null;

		try {
			conn = ConnectionProvider.getConnection();
			BoardDao.getInstance().insert(conn, data); // 데이터 삽입
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			JdbcUtil.close(conn);
		}
	}
	
	public Board read(int id) {
		Connection conn = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			Board board = BoardDao.getInstance().getBoard(conn, id);
			return board;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			JdbcUtil.close(conn);
		}
	}
	
	public int getTotalCnt() {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			return BoardDao.getInstance().getTotalCnt(conn);
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
			JdbcUtil.close(conn);
		}
	}
	
}