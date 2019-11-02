package app.atm.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Author: Maximus
 * Date: 2016/01/06
 * Time: 18:20
 */

public class Query {

	/* 获取用户信息 */
	public static boolean get(String account) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		PreparedStatement deleteStmt = null;
		ResultSet resultSet = null;

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT money FROM balance WHERE account=?");
			selectStmt.setString(1, account);
			resultSet = selectStmt.executeQuery();

			if (resultSet.next()) {
				String message = "<html>" +
						"<font face='宋体' size='5'>" +
						"<br>" +
						"账户: " + account +
						"<br><br>" +
						"余额: " + resultSet.getInt("money") +
						"<br><br>" +
						"</font>" +
						"<font face='宋体' size='4'>" +
						"确认删除?" +
						"<br><br></font>" +
						"</html>";
				int choice = JOptionPane.showConfirmDialog(null, new JLabel(message), "查询账户", JOptionPane.YES_NO_OPTION);
				if (choice == 0) {
					deleteStmt = conn.prepareStatement("DELETE FROM logininfo WHERE account=?");
					deleteStmt.setString(1, account);
					if (deleteStmt.executeUpdate() == 1) {
						deleteStmt = conn.prepareStatement("DELETE FROM balance WHERE account=?");
						deleteStmt.setString(1, account);
						return deleteStmt.executeUpdate() == 1;
					}
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
				Database_config.release(resultSet, selectStmt, deleteStmt, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

}
