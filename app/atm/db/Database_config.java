package app.atm.db;

import java.sql.*;

/**
 * Author: Maximus
 * Date: 2015/09/11
 * Time: 18:46
 */

/**
 * 数据库配置信息
 */
public class Database_config {

	private static String url = "jdbc:mysql://localhost:3306/atm?useUnicode=true&characterEncoding=utf-8";   // 使用Unicode编码，字符编码为UTF-8
	private static String user = "root";
	private static String password = "";

	private Database_config() {
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError("No suitable driver found");
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static void release(ResultSet resultSet, Statement statement, Connection conn) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void release(ResultSet resultSet, Statement statement1, Statement statement2, Connection conn) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (statement1 != null) {
					statement1.close();
				}
				if (statement2 != null) {
					statement2.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
