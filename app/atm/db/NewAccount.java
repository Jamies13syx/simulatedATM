package app.atm.db;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Author: Maximus
 * Date: 2016/01/06
 * Time: 9:49
 */

public class NewAccount {

	/* 添加账户 */
	public static boolean add(String account, String password) {
		if (NewAccount.query(account)) {
			JOptionPane.showMessageDialog(null, "账户已存在", "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		Connection conn = null;
		PreparedStatement insertStmt_account = null;
		PreparedStatement insertStmt_balance = null;
		ResultSet resultSet = null;

		try {
			conn = Database_config.getConnection();

			insertStmt_account = conn.prepareStatement("INSERT INTO logininfo (account, password, status) VALUES (?,?,?)");
			insertStmt_account.setString(1, account);
			insertStmt_account.setString(2, password);
			insertStmt_account.setString(3, "false");
			insertStmt_account.executeUpdate();
			insertStmt_balance = conn.prepareStatement("INSERT INTO balance (account) VALUES (?)");
			insertStmt_balance.setString(1, account);
			return insertStmt_balance.executeUpdate() == 1;

		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
			return false;
		} finally {
			try {
				Database_config.release(resultSet, insertStmt_account, insertStmt_balance, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

	/* 查询账户是否存在 */
	public static boolean query(String account) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT * FROM logininfo WHERE account=?");
			selectStmt.setString(1, account);
			resultSet = selectStmt.executeQuery();
			return resultSet.next();

		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
			return true;
		} finally {
			try {
				Database_config.release(resultSet, selectStmt, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

}
