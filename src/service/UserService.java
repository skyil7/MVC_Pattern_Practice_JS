package service;

import java.sql.Connection;

import dao.UserDao;
import domain.User;
import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;

public class UserService {
	private static UserService us = new UserService();
	private UserService() {
		
	}
	
	public static UserService getInstance() {
		return us;
	}
	
	public int join(User user) {
		Connection conn=null;
		
		try {
			conn = ConnectionProvider.getConnection();
			User data = UserDao.getInstance().selectById(conn, user.getId());
			
			if(data!=null) {//DB에 해당 유저가 있어 가입 불가
				return 0;
			}
			UserDao.getInstance().insert(conn, user);
			return 1;
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}finally {
			JdbcUtil.close(conn);
		}
	}
	
	public User getUser(String id) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			User data = UserDao.getInstance().selectById(conn, id);
			return data;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			JdbcUtil.close(conn); 
		}
	}
	
}
