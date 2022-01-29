package Client;

import java.awt.EventQueue;

import Server.ChatManager;
import view.LoginPage;
import view.MainWindow;

public class StartClient {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage login = new LoginPage();
					login.setVisible(true);
					MainWindow frame = new MainWindow();
					frame.setVisible(false);
					login.setFrame(frame);
					frame.setLoginPageFrame(login);
					ClientChatManager.getCM().setWindow(frame);
					ClientChatManager.getCM().setLoginPage(login);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
