package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import view.LoginPage;
import view.MainWindow;

public class ClientChatManager {
	private ClientChatManager() {};
	MainWindow window;
	LoginPage loginPage;
	public LoginPage getLoginPage() {
		return loginPage;
	}
	public void setLoginPage(LoginPage loginPage) {
		this.loginPage = loginPage;
	}
	Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	InputStreamReader receiver;
	public String clientHostName;
	
	public static final ClientChatManager instance = new ClientChatManager();
	public static ClientChatManager getCM() {
		return instance;	
	}
	public void connect(String ip) {
		new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket(ip, 1234);
					writer = new PrintWriter(
							new OutputStreamWriter(
									socket.getOutputStream()));
					receiver = new InputStreamReader(
							socket.getInputStream(),"utf-8");
					reader = new BufferedReader(receiver);
					specialSend("SYSTEM_CHANGE_on_username1250: " + clientHostName);
					String line = null;
					while((line = reader.readLine()) != null) {
						if(line.contains("SYSTEM_CHANGE_on_remove_online_client1250: ")) {
							window.removeChannel(line.substring(43));
						} else if(line.contains("SYSTEM_CHANGE_on_add_online_client1250: ")) {
							window.addChannel(line.substring(40));
						} else if(line.equals("SYSTEM_WARNING_account_already_exist1250")) {
							closeConnection();
							JOptionPane.showMessageDialog(null, "The account is already online.");
						} else {
							SimpleDateFormat sdf = new SimpleDateFormat();
							sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
							Date date = new Date();
							window.appendText("                       "+sdf.format(date));
							window.appendText(line);
							window.appendText("");
						}
						
					}
					writer.close();
					reader.close();
					writer = null;
					receiver = null;
					reader = null;
				} catch (ConnectException e) {
					JOptionPane.showMessageDialog(null, "Connection Refused");
					window.logoutFunction();
				} catch (UnknownHostException e) {
					JOptionPane.showMessageDialog(null, "Connection Lost");
					window.logoutFunction();
				} catch(SocketException e) {
					//JOptionPane.showMessageDialog(null, "Connection Lost");
					window.logoutFunction();
				} catch (IOException e) {
//					JOptionPane.showMessageDialog(null, "Connection Lost");
					e.printStackTrace();
					window.appendText("Server: Connection has lost!");
					System.out.println("Connection Closed.");
				}
			}
		}.start();
	}
	public void send(String message) {
		if(writer != null) {
			writer.write(loginPage.usernameField.getText()+": "+message+"\n");
			writer.flush();
		} else {
			window.appendText("Server: Connection has lost!");
		}
	}
	public void specialSend(String message) {
		if(writer != null) {
			writer.write(message+"\n");
			writer.flush();
		} else {
			window.appendText("Server: Connection has lost!");
		}
	}
	public void setWindow(MainWindow window) {
		this.window = window;
//		window.appendText("文本框已经和clientchatmanager绑定了");
	}
	public void closeConnection() {
		try {
			socket.shutdownInput();
			socket.close();
		} catch (IOException e) {
			System.out.println("Socket closed.");
		}	
	}
}
