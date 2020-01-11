package graph;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import app.atm.db.ChangePassword;
import app.atm.db.Deal;
import app.atm.db.Login;
import app.atm.loginHandle.LoginHandle;


public class Main_GUI extends JFrame {

	private static final long serialVersionUID = -7268017520083946414L;

	private final String account;// 账户，用于窗口间传递、与数据库的交互

	private JPanel panel = new JPanel();// 底板，用于绝对定位
	private JLabel aLabel = new JLabel();// 显示卡号
	private TimePanel timePaenl;// 倒计时

	private JButton dpButton = new JButton("存款");
	private JButton wdButton = new JButton("取款");
	private JButton trans = new JButton("转账");

	private JButton cbButton = new JButton("查询余额");
	private JButton rpButton = new JButton("修改密码");
	private JButton exit = new JButton("退出登录");

	private Timer timer;
	private final int maximumTime = 60;
	private int currentTime = 0;

	/*
	 * 登录成功后，有账户传入，建立此对象
	 */
	public Main_GUI(final String account) {
		this.account = account;// 账户，用于窗口间传递、与数据库的交互

		/*
		 * 窗口基本设置
		 */
		this.setTitle("ATM");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);

		/*
		 * 各组件及布局
		 */
		// 计算底板左上角坐标
		int x0 = panel.getLocation().x;
		int y0 = panel.getLocation().y;
		// 标签，用于显示卡号
		aLabel.setHorizontalAlignment(JLabel.CENTER);
		aLabel.setText("欢迎光临， 您的卡号是： " + account);
		aLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
		panel.setLayout(null);
		aLabel.setBounds(x0, y0, 800, 80);
		panel.add(aLabel);
		// 板子，用于展示倒计时（60秒制）
		timePaenl = new TimePanel();
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentTime < maximumTime) {
					repaint();
					currentTime++;
				} else {
					// 退出登录
					Login.exit(account);
					new LoginHandle();

					timer.stop();
					close();
					// new Thread(new Runnable() {
					// @Override
					// public void run() {
					// new LoginHandle();
					// }
					// }).start();
				}
			}
		});
		timer.start();
		timePaenl.setBounds(x0, y0 + 80, 800, 80);
		panel.add(timePaenl);
		// 按钮，用于进入存款窗口
		dpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new dpFrame(getAccount()).setVisible(true);

				currentTime = 0;
				timer.stop();
				close();
			}
		});
		dpButton.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
		dpButton.setBounds(x0, y0 + 240, 150, 80);
		panel.add(dpButton);
		// 按钮，用于进入取款窗口
		wdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new wdFrame(getAccount()).setVisible(true);

				currentTime = 0;
				timer.stop();
				close();
			}
		});
		wdButton.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
		wdButton.setBounds(x0, y0 + 340, 150, 80);
		panel.add(wdButton);
		// 按钮，用于进入转账窗口
		trans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				timer.stop();
				currentTime = 0;
				Deal.transfer(account);
				timer.start();
			}
		});
		trans.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
		trans.setBounds(x0, y0 + 440, 150, 80);
		panel.add(trans);
		// 按钮，用于进入查询余额窗口
		cbButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentTime = 0;
				timer.stop();

				int balance = Deal.query(getAccount());
				if (balance != -1) {
					JOptionPane.showMessageDialog(null, "你的账户余额是" + balance
							+ "元");
					timer.start();
				}
			}
		});
		cbButton.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
		cbButton.setBounds(x0 + 620, y0 + 240, 180, 80);
		panel.add(cbButton);
		// 按钮，用于进入修改密码窗口
		rpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new rpFrame(getAccount()).setVisible(true);

				currentTime = 0;
				timer.stop();
				close();
			}
		});
		rpButton.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
		rpButton.setBounds(x0 + 620, y0 + 340, 180, 80);
		panel.add(rpButton);
		// 按钮，用于退出登录
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentTime = 0;
				timer.stop();
				close();

				// exit
				Login.exit(account);

				new Thread(new Runnable() {
					@Override
					public void run() {
						new LoginHandle();
					}
				}).start();

			}
		});
		exit.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
		exit.setBounds(x0 + 620, y0 + 440, 180, 80);
		panel.add(exit);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Login.exit(account);
			}
		});
	}

	/*
	 * 关闭当前窗口
	 */
	private void close() {
		this.dispose();
	}

	/*
	 * 
	 */
	private class TimePanel extends JPanel {
		/**
		 *
		 */
		private static final long serialVersionUID = -3230808343771663827L;

		public TimePanel() {

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setFont(new Font("宋体", Font.BOLD, 18));
			g.drawString("倒计时：" + (maximumTime - currentTime) + "秒", 800 - 150,
					80 - 40);
		}
	}

	/*
	 * 窗口，用于存款
	 */
	private class dpFrame extends JFrame {
		/**
		 *
		 */
		private static final long serialVersionUID = 5781606078422674207L;

		private final String account;// 账户，用于窗口间传递、与数据库的交互

		private JPanel dpPanel = new JPanel();// 底板

		private JTextField field = new JTextField();// 输入显示区域

		// 按钮组（中间）
		private JPanel buttonPanel = new JPanel();
		private JButton button7 = new JButton("7");
		private JButton button8 = new JButton("8");
		private JButton button9 = new JButton("9");
		private JButton button4 = new JButton("4");
		private JButton button5 = new JButton("5");
		private JButton button6 = new JButton("6");
		private JButton button1 = new JButton("1");
		private JButton button2 = new JButton("2");
		private JButton button3 = new JButton("3");
		private JButton buttonR = new JButton("重输");
		private JButton button0 = new JButton("0");
		private JButton buttonC = new JButton("清除");

		// 按钮组（底边）
		private JButton buttonS = new JButton("确定");
		private JButton buttonB = new JButton("后退");

		public dpFrame(final String account) {
			this.account = account;// 账户，用于窗口间传递、与数据库的交互

			/*
			 * 窗口基本设置
			 */
			this.setTitle("存款");
			this.setSize(800, 600);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(dpPanel);

			/*
			 * 绝对定位准备
			 */
			dpPanel.setLayout(null);
			int x0 = dpPanel.getLocation().x;
			int y0 = dpPanel.getLocation().y;

			/*
			 * 各组件及布局
			 */
			// 输入区域，显示存储金额
			field.setEditable(false);
			field.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
			field.setBounds(x0 + 80, y0 + 60, 640, 80);
			field.setHorizontalAlignment(JTextField.RIGHT);
			dpPanel.add(field);
			// 按钮板子，存放4*3个按钮
			buttonPanel.setLayout(new GridLayout(4, 3, 10, 10));
			buttonPanel.add(button7);
			button7.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "7");
				}
			});
			button7.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button8);
			button8.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "8");
				}
			});
			button8.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button9);
			button9.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "9");
				}
			});
			button9.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button4);
			button4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "4");
				}
			});
			button4.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button5);
			button5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "5");
				}
			});
			button5.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button6);
			button6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "6");
				}
			});
			button6.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button1);
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "1");
				}
			});
			button1.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button2);
			button2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "2");
				}
			});
			button2.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button3);
			button3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "3");
				}
			});
			button3.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(buttonR);
			buttonR.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText("");
				}
			});
			buttonR.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button0);
			button0.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "0");
				}
			});
			button0.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(buttonC);
			buttonC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String temp = field.getText();
					if (!temp.equals("")) {
						if (temp.length() == 1) {
							temp = "";
						} else {
							temp = temp.substring(0, temp.length() - 1);
						}
						field.setText(temp);
					}
				}
			});
			buttonC.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.setBounds(x0 + 80, y0 + 160, 640, 300);
			dpPanel.add(buttonPanel);

			// 底部按钮，确定
			buttonS.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int money = 0;
					try {
						money = Integer.parseInt(field.getText());
					} catch (NumberFormatException e1) {
						// e1.printStackTrace();
					}
					if (money > 0 && money % 100 == 0) {
						boolean isDeposited = Deal.handle(getAccount(), money);
						if (isDeposited) {
							close();
							new Main_GUI(getAccount()).setVisible(true);
						}
					} else if (money != 0) {
						JOptionPane.showMessageDialog(null, "仅限存100的倍数", "错误",
								JOptionPane.ERROR_MESSAGE);
						field.setText("");
					}
				}
			});
			buttonS.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
			buttonS.setBounds(x0, y0 + 480, 150, 80);
			dpPanel.add(buttonS);
			// 底部按钮，后退
			buttonB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					close();
					new Main_GUI(getAccount()).setVisible(true);
				}
			});
			buttonB.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
			buttonB.setBounds(x0 + 650, y0 + 480, 150, 80);
			dpPanel.add(buttonB);

			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					super.windowClosing(e);
					Login.exit(account);
				}
			});
		}

		/*
		 * 关闭当前窗口
		 */
		public void close() {
			this.dispose();
		}

		/*
		 * 返回当前账户，用于窗口传递
		 */
		public String getAccount() {
			return account;
		}
	}

	private class wdFrame extends JFrame {
		/**
		 *
		 */
		private static final long serialVersionUID = 1192328513039541562L;

		private final String account;// 账户，用于窗口间传递、与数据库的交互

		private JPanel wdPanel = new JPanel();// 底板

		private JTextField field = new JTextField();

		private JPanel buttonPanel = new JPanel();
		private JButton button7 = new JButton("7");
		private JButton button8 = new JButton("8");
		private JButton button9 = new JButton("9");
		private JButton button4 = new JButton("4");
		private JButton button5 = new JButton("5");
		private JButton button6 = new JButton("6");
		private JButton button1 = new JButton("1");
		private JButton button2 = new JButton("2");
		private JButton button3 = new JButton("3");
		private JButton buttonR = new JButton("重输");
		private JButton button0 = new JButton("0");
		private JButton buttonC = new JButton("清除");

		private JButton buttonS = new JButton("确定");
		private JButton buttonB = new JButton("后退");

		public wdFrame(final String account) {
			this.account = account;

			this.setTitle("取款");
			this.setSize(800, 600);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(wdPanel);

			wdPanel.setLayout(null);
			int x0 = wdPanel.getLocation().x;
			int y0 = wdPanel.getLocation().y;

			field.setEditable(false);
			field.setFont(new Font(Font.DIALOG, Font.PLAIN, 24));
			field.setBounds(x0 + 80, y0 + 60, 640, 80);
			field.setHorizontalAlignment(JTextField.RIGHT);
			wdPanel.add(field);

			buttonPanel.setLayout(new GridLayout(4, 3, 10, 10));
			buttonPanel.add(button7);
			button7.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "7");
				}
			});
			button7.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button8);
			button8.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "8");
				}
			});
			button8.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button9);
			button9.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "9");
				}
			});
			button9.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button4);
			button4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "4");
				}
			});
			button4.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button5);
			button5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "5");
				}
			});
			button5.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button6);
			button6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "6");
				}
			});
			button6.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button1);
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "1");
				}
			});
			button1.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button2);
			button2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "2");
				}
			});
			button2.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button3);
			button3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "3");
				}
			});
			button3.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(buttonR);
			buttonR.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText("");
				}
			});
			buttonR.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(button0);
			button0.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					field.setText(field.getText() + "0");
				}
			});
			button0.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.add(buttonC);
			buttonC.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String temp = field.getText();
					if (!temp.equals("")) {
						if (temp.length() == 1) {
							temp = "";
						} else {
							temp = temp.substring(0, temp.length() - 1);
						}
						field.setText(temp);
					}
				}
			});
			buttonC.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
			buttonPanel.setBounds(x0 + 80, y0 + 160, 640, 300);
			wdPanel.add(buttonPanel);

			buttonS.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int money = 0;
					try {
						money = Integer.parseInt(field.getText());
					} catch (NumberFormatException e1) {
						// e1.printStackTrace();
					}
					if (money > 0 && money % 100 == 0) {
						if (money > 5000) {
							JOptionPane.showMessageDialog(null,
									"超出最大取款限额, 单笔资金不超过5000", "错误",
									JOptionPane.ERROR_MESSAGE);
						} else {
							boolean isWithdrawed = Deal.handle(getAccount(),
									-money);
							if (isWithdrawed) {
								new Main_GUI(getAccount()).setVisible(true);
								close();
							}
						}
					} else if (money != 0) {
						JOptionPane.showMessageDialog(null, "取款仅限100的倍数", "错误",
								JOptionPane.ERROR_MESSAGE);
						field.setText("");
					}
				}
			});
			buttonS.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
			buttonS.setBounds(x0, y0 + 480, 150, 80);
			wdPanel.add(buttonS);

			buttonB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					close();
					new Main_GUI(getAccount()).setVisible(true);
				}
			});
			buttonB.setFont(new Font(Font.DIALOG, Font.BOLD, 28));
			buttonB.setBounds(x0 + 650, y0 + 480, 150, 80);
			wdPanel.add(buttonB);

			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					super.windowClosing(e);
					Login.exit(account);
				}
			});
		}

		public void close() {
			this.dispose();
		}

		public String getAccount() {
			return account;
		}
	}

	private class rpFrame extends JFrame {
		/**
		 *
		 */
		private static final long serialVersionUID = -3166944165917573581L;

		private final String account;// 账户，用于窗口间传递、与数据库的交互

		private JPanel rpPanel = new JPanel();// 底板

		private JLabel label1 = new JLabel("请输入旧的密码");
		private JPasswordField textfield1 = new JPasswordField();
		private JLabel label2 = new JLabel("请输入新的密码");
		private JPasswordField textfield2 = new JPasswordField();
		private JLabel label3 = new JLabel("请再输入一遍密码");
		private JPasswordField textfield3 = new JPasswordField();

		private JButton buttonS = new JButton("确定");
		private JButton buttonB = new JButton("返回");

		public rpFrame(final String account) {
			this.account = account;

			this.setTitle("修改密码");
			this.setSize(400, 300);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.add(rpPanel);

			rpPanel.setLayout(new GridLayout(4, 2, 20, 10));
			rpPanel.add(label1);
			rpPanel.add(textfield1);
			rpPanel.add(label2);
			rpPanel.add(textfield2);
			rpPanel.add(label3);
			rpPanel.add(textfield3);
			rpPanel.add(buttonS);
			rpPanel.add(buttonB);

			textfield1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					super.keyPressed(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						e.consume();
						KeyboardFocusManager.getCurrentKeyboardFocusManager()
								.focusNextComponent(label2);
					}
				}
			});

			textfield2.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					super.keyPressed(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						e.consume();
						KeyboardFocusManager.getCurrentKeyboardFocusManager()
								.focusNextComponent(label3);
					}
				}
			});

			textfield3.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					super.keyPressed(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						e.consume();
						buttonS.doClick();
					}
				}
			});

			buttonS.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (String.valueOf(textfield2.getPassword()).equals(
							String.valueOf(textfield3.getPassword()))) {
						String old_password = String.valueOf(textfield1.getPassword());
						String new_password = String.valueOf(textfield2.getPassword());
						boolean isResetPassword = ChangePassword.doChange(
								getAccount(), old_password, new_password);

						if (isResetPassword) {
							close();

							// 退出到登录界面
							Login.exit(account);

							new Thread(new Runnable() {
								@Override
								public void run() {
									new LoginHandle();
								}
							}).start();
						}
					}
				}
			});

			// 按钮，后退
			buttonB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					close();
					new Main_GUI(getAccount()).setVisible(true);
				}
			});

			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					super.windowClosing(e);
					Login.exit(account);
				}
			});
		}

		public String getAccount() {
			return account;
		}

		public void close() {
			dispose();
		}
	}

	public static void main(String[] args) {
		Main_GUI startFrame = new Main_GUI("888888");
		startFrame.setVisible(true);
	}

	public String getAccount() {
		return account;
	}
}
