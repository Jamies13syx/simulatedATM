package app.atm.db;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Author: Maximus
 * Date: 2016/01/06
 * Time: 16:55
 */

public class Deal {  /* 交易 */

	/* 转账 */
	public static boolean transfer(String account) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		PreparedStatement updateStmt = null;
		int sum;
		int m;
		String money = null;

		String toAccount = JOptionPane.showInputDialog("请输入要转入的账户");
		if (toAccount == null)
			return false;

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT money FROM balance WHERE account=?");
			selectStmt.setString(1, toAccount);
			resultSet = selectStmt.executeQuery();

			if (!resultSet.next()) {
				JOptionPane.showMessageDialog(null, "无此账户", "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			money = JOptionPane.showInputDialog("请输入金额");
			if (money == null)
				return false;

			try {
				m = Integer.parseInt(money);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "输入错误", "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (m < 0) {
				JOptionPane.showMessageDialog(null, "需为正数", "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (m > 5000 || m % 100 != 0) {
				JOptionPane.showMessageDialog(null, "需为100的倍数", "错误", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			selectStmt = conn.prepareStatement("SELECT money FROM balance WHERE account=?");
			selectStmt.setString(1, account);
			resultSet = selectStmt.executeQuery();

			if (resultSet.next()) {
				sum = resultSet.getInt("money");
				sum -= m;

				if (sum < 0) {
					JOptionPane.showMessageDialog(null, "余额不足", "错误", JOptionPane.ERROR_MESSAGE);
					return false;
				}

				updateStmt = conn.prepareStatement("UPDATE balance SET money=? WHERE account=?");
				updateStmt.setInt(1, sum);
				updateStmt.setString(2, account);
				if (updateStmt.executeUpdate() != 1) {
					JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else {
				JOptionPane.showMessageDialog(null, "无此账户", "错误", JOptionPane.ERROR_MESSAGE);
			}

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

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT money FROM balance WHERE account=?");
			selectStmt.setString(1, toAccount);
			resultSet = selectStmt.executeQuery();

			if (resultSet.next()) {
				sum = resultSet.getInt("money");
				sum += m;

				if (sum < 0) {
					JOptionPane.showMessageDialog(null, "余额不足", "错误", JOptionPane.ERROR_MESSAGE);
					return false;
				}

				updateStmt = conn.prepareStatement("UPDATE balance SET money=? WHERE account=?");
				updateStmt.setInt(1, sum);
				updateStmt.setString(2, toAccount);
				if (updateStmt.executeUpdate() == 1) {
					JOptionPane.showMessageDialog(null, "交易成功", "成功", JOptionPane.INFORMATION_MESSAGE);
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
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

	/* 存款/取款 */
	public static boolean handle(String account, int money) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		PreparedStatement updateStmt = null;
		int sum;

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT money FROM balance WHERE account=?");
			selectStmt.setString(1, account);
			resultSet = selectStmt.executeQuery();

			if (resultSet.next()) {
				sum = resultSet.getInt("money");
				sum += money;

				if (sum < 0) {
					JOptionPane.showMessageDialog(null, "余额不足", "错误", JOptionPane.ERROR_MESSAGE);
					return false;
				}

				updateStmt = conn.prepareStatement("UPDATE balance SET money=? WHERE account=?");
				updateStmt.setInt(1, sum);
				updateStmt.setString(2, account);
				if (updateStmt.executeUpdate() == 1) {
					JOptionPane.showMessageDialog(null, "交易成功", "成功", JOptionPane.INFORMATION_MESSAGE);
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
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

	/* 查询余额 */
	public static int query(String account) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;

		try {
			conn = Database_config.getConnection();

			selectStmt = conn.prepareStatement("SELECT money FROM balance WHERE account=?");
			selectStmt.setString(1, account);
			resultSet = selectStmt.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt("money");
			} else {
				JOptionPane.showMessageDialog(null, "无此账户", "错误", JOptionPane.ERROR_MESSAGE);
			}

			return -1;

		} catch (ExceptionInInitializerError e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "网络异常", "异常", JOptionPane.ERROR_MESSAGE);
			return -1;
		} finally {
			try {
				Database_config.release(resultSet, selectStmt, conn);
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			}
		}
	}

}
