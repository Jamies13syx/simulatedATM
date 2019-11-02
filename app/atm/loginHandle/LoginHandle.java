package app.atm.loginHandle;

import app.atm.db.Login;
import graph.Main_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Author: Maximus
 * Date: 2016/01/06
 * Time: 8:08
 */

public class LoginHandle {

	private JFrame frame = new JFrame("ATM机 登陆界面");

	public static void main(String[] args) {
		new LoginHandle();
	}

	public LoginHandle() {

		JPanel panel = new JPanel();
		panel.setSize(840, 600);
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
		final JButton submit = new JButton("登陆");
		submit.setFont(new java.awt.Font("宋体", 0, 18));
		submit.setBounds(570, 300, 80, 40);
		panel.add(submit);
		final JLabel passLab = new JLabel("请输入密码: ");
		passLab.setFont(new java.awt.Font("宋体", 0, 18));
		passLab.setBounds(150, 380, 120, 40);
		panel.add(passLab);
		final JPasswordField password = new JPasswordField();
		password.setFont(new java.awt.Font("Consolas", 0, 24));
		password.setBounds(290, 380, 240, 40);
		panel.add(password);
		JButton clean = new JButton("清除");
		clean.setFont(new java.awt.Font("宋体", 0, 18));
		clean.setBounds(570, 380, 80, 40);
		panel.add(clean);

		show();

		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println(user.getText());
//				System.out.println(password.getPassword());
				if (Login.check(account.getText(), String.valueOf(password.getPassword()))) {  /* 核对登陆信息 */
					// todo **登陆成功**
					Main_GUI startFrame = new Main_GUI(account.getText());
					startFrame.setVisible(true);

					frame.dispose();
				} else {
					password.setText("");
					KeyboardFocusManager.
							getCurrentKeyboardFocusManager().focusNextComponent(userLab);
				}
			}
		});

		clean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				account.setText("");
				password.setText("");
				KeyboardFocusManager.
						getCurrentKeyboardFocusManager().focusNextComponent(userLab);
			}
		});

		/* 键盘监听器，方便用户输入 */
		account.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					KeyboardFocusManager.
							getCurrentKeyboardFocusManager().focusNextComponent(passLab);
				}
			}
		});

		/* 键盘监听器，方便用户输入 */
		password.addKeyListener(new KeyAdapter() {
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
		frame.setSize(845, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
