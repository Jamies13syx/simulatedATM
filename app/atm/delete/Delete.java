package app.atm.delete;

import app.atm.db.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class Delete {

	private JFrame frame = new JFrame("ATM机 删除账户界面");

	public static void main(String[] args) {
		new Delete();
	}

	public Delete() {

		JPanel panel = new JPanel();
		panel.setSize(840, 500);
		panel.setLayout(null);
		frame.add(panel);
		ImageIcon icon = new ImageIcon("src/app/atm/校徽.jpg");
		icon.setImage(icon.getImage().getScaledInstance(828, 220, Image.SCALE_DEFAULT));
		JLabel logo = new JLabel(icon);
		logo.setBounds(0, 0, 828, 220);
		panel.add(logo);
		final JLabel userLab = new JLabel("请输入卡号: ");
		userLab.setBounds(150, 300, 120, 40);
		userLab.setFont(new java.awt.Font("宋体", 0, 18));
		panel.add(userLab);
		final JTextField account = new JTextField();
		account.setBounds(290, 300, 240, 40);
		account.setFont(new java.awt.Font("Consolas", 0, 18));
		panel.add(account);
		final JButton submit = new JButton("查询");
		submit.setFont(new java.awt.Font("宋体", 0, 18));
		submit.setBounds(570, 300, 80, 40);
		panel.add(submit);

		show();

		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println(user.getText());
				if (Query.get(account.getText())) {  /* 在数据库中查询用户信息 */
					JOptionPane.showMessageDialog(null, "删除账户成功", "成功", JOptionPane.INFORMATION_MESSAGE);
					account.setText("");
					KeyboardFocusManager.
							getCurrentKeyboardFocusManager().focusNextComponent(userLab);
				}
			}
		});

		/* 键盘监听器，方便用户输入 */
		account.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					submit.doClick();
				}
			}
		});

	}

	/* 定义窗口位置和大小 */
	private void show() {
		frame.setSize(845, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}


