package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Client.ClientChatManager;
import Server.ChatManager;
import jdbcConnection.ApplicationDB;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField channel;
	public JTextField insertField;
	JButton LogoutButton;
	JTextArea outputField;
	JPanel panelForOutputField;
	private JScrollPane outputFieldScrollPane;
	private LoginPage loginPageFrame;
	private JMenuBar menuBar;
	private JMenu channelMenu;
	private LinkedList<String> channelList = new LinkedList<String>();
	public JLabel hostLabel;
	
	public void addChannel(String channel) {
		channelList.add(channel);
		updateMenu();
	}
	
	public void removeChannel(String channel) {
		channelList.remove(channel);
		updateMenu();
	}
	
	public void updateMenu() {
		channelMenu.removeAll();
		for(String channel: channelList) {
			JMenuItem item = new JMenuItem(channel);
			item.addActionListener(new changeChannelListener(channel));
			channelMenu.add(item);
		}
	}
	
	public LoginPage getLoginPageFrame() {
		return loginPageFrame;
	}

	public void setLoginPageFrame(LoginPage loginPageFrame) {
		this.loginPageFrame = loginPageFrame;
	}
	
	class changeChannelListener implements ActionListener {
		String text;
		public changeChannelListener(String text) {
			this.text = text;
		}
		public void actionPerformed(ActionEvent e) {
			channel.setText(text);
			ClientChatManager.getCM().specialSend("SYSTEM_CHANGE_on_message_receiver1250: " + text);
		}
	}
	public MainWindow() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 506, 379);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		channelMenu = new JMenu("Channels");
		menuBar.add(channelMenu);
		
		hostLabel = new JLabel("                  host");
		hostLabel.setFont(new Font("ËÎÌו", Font.PLAIN, 15));
		menuBar.add(hostLabel);
		
		channelList.add("Public Channel");
		updateMenu();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		outputField = new JTextArea();
		outputField.setEditable(false);
		outputField.setText("Ready...");
		outputField.setBackground(Color.WHITE);
		panelForOutputField = new JPanel();
		panelForOutputField.setSize(400, 180);
		outputFieldScrollPane = new JScrollPane(panelForOutputField);
		outputFieldScrollPane.setEnabled(false);
		GroupLayout gl_panelForOutputField = new GroupLayout(panelForOutputField);
		gl_panelForOutputField.setHorizontalGroup(
			gl_panelForOutputField.createParallelGroup(Alignment.LEADING)
				.addComponent(outputField, GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
		);
		gl_panelForOutputField.setVerticalGroup(
			gl_panelForOutputField.createParallelGroup(Alignment.LEADING)
				.addComponent(outputField, GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
		);
		panelForOutputField.setLayout(gl_panelForOutputField);
		
		channel = new JTextField();
		channel.setEditable(false);
		channel.setText("Public Channel");
		channel.setColumns(10);
		
		LogoutButton = new JButton("Logout");
		LogoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ClientChatManager.getCM().closeConnection();
				logoutFunction();
			}
		});
		
		insertField = new JTextField();
		insertField.setFont(new Font("ËÎÌו", Font.PLAIN, 13));
		insertField.setText("insert text here...");
		insertField.setColumns(10);
		
		JButton sendBtn = new JButton("Send");
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SimpleDateFormat sdf = new SimpleDateFormat();
				sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				ClientChatManager.getCM().send(insertField.getText());
				appendText("                       "+sdf.format(date));
				appendText("me: "+insertField.getText());
				appendText("");
				insertField.setText("");
			}
		});
		sendBtn.setFont(new Font("ËÎÌו", Font.BOLD, 15));
		
		JButton viewHistoryBTN = new JButton("View History");
		viewHistoryBTN.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ApplicationDB aDB = new ApplicationDB();
				Connection connection =aDB.getConnection();
				ArrayList<String> records = aDB.showHistoryRecord(connection, loginPageFrame.usernameField.getText());
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				outputField.setText("Ready...");
				for(String s: records) {
					outputField.append("\n" + s);
				}
				outputField.append("\n" + "		History Message Above");
				outputField.append("\n" + "--------------------------------------------------------------------------------------------------------------------------");
				outputField.append("\n" );
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(insertField, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
						.addComponent(channel, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(viewHistoryBTN, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(LogoutButton, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
						.addComponent(sendBtn, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)))
				.addComponent(outputFieldScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(channel, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(LogoutButton)
						.addComponent(viewHistoryBTN))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(outputFieldScrollPane, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(sendBtn, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
						.addComponent(insertField, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void appendText(String in) {
		outputField.append("\n" + in);
		ApplicationDB aDB03 = new ApplicationDB();
		Connection connection =aDB03.getConnection();
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		aDB03.insertMessageRecord(connection,loginPageFrame.usernameField.getText(),sdf.format(date),in);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void logoutFunction() {
		setVisible(false);
		loginPageFrame.setVisible(true);
		channelList = new LinkedList<String>();
		channelList.add("Public Channel");
		outputField.setText("Ready...");
		updateMenu();
	}
}
