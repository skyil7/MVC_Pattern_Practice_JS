package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.User;
import jdbc.JdbcUtil;

public class UserDao {
	private static UserDao ud=new UserDao();
	
	private UserDao() {
		//아무것도 없죠?
	}
	
	public static UserDao getInstance() {
		return ud;
	}
	
	public void insert(Connection conn, User user) throws SQLException{
		PreparedStatement pstmt=null;
		
		try {
			pstmt = conn.prepareStatement("INSERT INTO users(ID,NAME,PASSWORD) VALUES(?,?,?)");
			pstmt.setString(1, user.getId());
			pstmt.setString(2, user.getName());
			pstmt.setString(3, user.getPassword());
			pstmt.executeUpdate();
		}finally {
			JdbcUtil.close(pstmt);
		}
	}
	
	public User selectById(Connection conn, String id) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt=conn.prepareStatement("SELECT * FROM users WHERE ID=?");
			pstmt.setString(1, id);
			
			rs=pstmt.executeQuery();
			User user = null;
			if(rs.next()) {
				user = new User();
				user.setId(rs.getString("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
			}
			return user;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
}
