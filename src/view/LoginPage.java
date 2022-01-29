package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Client.ClientChatManager;
import jdbcConnection.ApplicationDB;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginPage extends JFrame {

	private JPanel contentPane;
	public JTextField usernameField;
	private JTextField passwordField;
	private MainWindow mainWindowFrame;
	private CreateAccountPage createAccountFrame;
	
	public void setFrame(MainWindow mainWindowFrame) {
		this.mainWindowFrame = mainWindowFrame;
	}
	public MainWindow getFrame() {
		return mainWindowFrame;
	}
	
	public LoginPage() {
		createAccountFrame = new CreateAccountPage();
		createAccountFrame.setLoginPageFrame(this);
		createAccountFrame.setVisible(false);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 321);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel loginPageLabel = new JLabel("         Login Page");
		loginPageLabel.setFont(new Font("宋体", Font.PLAIN, 30));
		
		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setFont(new Font("宋体", Font.PLAIN, 16));
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		
		JButton loginBtn = new JButton("Login");
		loginBtn.addMouseListener(new MouseAdapter() {///////
			@Override
			public void mouseClicked(MouseEvent e) {
				ApplicationDB aDB01 = new ApplicationDB();
				Connection connection = aDB01.getConnection();	
				boolean isAccount = aDB01.isLogin(connection, usernameField.getText(), passwordField.getText());
				if(isAccount == false) {
					JOptionPane.showMessageDialog(null, "Wrong Username or Password !!!");
				}else{
					mainWindowFrame.setVisible(true);
					setVisible(false);
					ClientChatManager.getCM().connect("127.0.0.1");
					ClientChatManager.getCM().clientHostName = usernameField.getText();
					mainWindowFrame.hostLabel.setText("                  host: "+usernameField.getText());
				}
				try {
					connection.close();
				} catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		loginBtn.setFont(new Font("宋体", Font.PLAIN, 16));//////
		
		JButton createAccountBtn = new JButton("Create Account");///////
		createAccountBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				createAccountFrame.setVisible(true);
				setVisible(false);
			}
		});
		createAccountBtn.setFont(new Font("宋体", Font.PLAIN, 16));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(loginPageLabel, GroupLayout.PREFERRED_SIZE, 426, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(24)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(passwordField))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(44)
							.addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
							.addGap(61)
							.addComponent(createAccountBtn, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(loginPageLabel, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(usernameLabel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addGap(44)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(loginBtn)
						.addComponent(createAccountBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addGap(13))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
