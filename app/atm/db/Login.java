package app.atm.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Login {

	/* 查询登陆信息的正误 */
	public static boolean check(String account, String password) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT * FROM logininfo WHERE account=?");
			selectStmt.setString(1, account);
			resultSet = selectStmt.executeQuery();

			if (resultSet.next()) {
				if (resultSet.getString("status").equals("true")) {
					JOptionPane.showMessageDialog(null, "您的账户已在别处登陆", "错误", JOptionPane.ERROR_MESSAGE);
				} else if (password.equals(resultSet.getString("password"))) {
					if (Login.update(account) == 1) {
//						System.out.println("登陆成功");
					} else {
						JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
					}
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "密码错误", "错误", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "用户不存在", "错误", JOptionPane.ERROR_MESSAGE);
			}

			return false;

		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
			return false;
		} finally {
			try {
				Database_config.release(resultSet, selectStmt, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

	/* 修改用户状态 */
	private static int update(String account) {
		Connection conn = null;
		PreparedStatement updateStmt = null;
		ResultSet resultSet = null;

		try {
			conn = Database_config.getConnection();

			updateStmt = conn.prepareStatement("UPDATE logininfo SET status=? WHERE account=?");
			updateStmt.setString(1, "true");
			updateStmt.setString(2, account);
			return updateStmt.executeUpdate();

		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				Database_config.release(resultSet, updateStmt, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

	/* 退卡 */
	public static int exit(String account) {
		Connection conn = null;
		PreparedStatement updateStmt = null;
		ResultSet resultSet = null;

		try {
			conn = Database_config.getConnection();

			updateStmt = conn.prepareStatement("UPDATE logininfo SET status=? WHERE account=?");
			updateStmt.setString(1, "false");
			updateStmt.setString(2, account);
			return updateStmt.executeUpdate();

		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				Database_config.release(resultSet, updateStmt, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

}
