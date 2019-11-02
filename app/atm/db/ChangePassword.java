package app.atm.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Author: Maximus
 * Date: 2016/01/06
 * Time: 17:27
 */

public class ChangePassword {  /* 修改密码 */

	public static boolean doChange(String account, String old_password, String new_password) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		PreparedStatement updateStmt = null;
		int sum;

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT password FROM logininfo WHERE account=?");
			selectStmt.setString(1, account);
			resultSet = selectStmt.executeQuery();

			if (resultSet.next()) {
				if (old_password.equals(resultSet.getString("password"))) {
					updateStmt = conn.prepareStatement("UPDATE logininfo SET password=? WHERE account=?");
					updateStmt.setString(1, new_password);
					updateStmt.setString(2, account);
					if (updateStmt.executeUpdate() == 1) {
						JOptionPane.showMessageDialog(null, "修改密码成功", "成功", JOptionPane.INFORMATION_MESSAGE);
						return true;
					} else {
						JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "密码错误", "错误", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "无此账户", "错误", JOptionPane.ERROR_MESSAGE);
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
				Database_config.release(resultSet, selectStmt, updateStmt, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

}
