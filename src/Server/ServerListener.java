package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerListener implements Runnable{
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(1234);
			while(true) {
				Socket socket = serverSocket.accept();
//				JOptionPane.showMessageDialog(null, "A client connecting to port 1234");
				//´«µÝsocket
				ChatSocket cs = new ChatSocket(socket);
				cs.start();
				ChatManager.getCM().add(cs);
				ChatManager.getCM().updateOnlineClients(cs);
				System.out.println("clients number: " + ChatManager.getCM().clientList.size());
				System.out.println("A client has connected to server.");
				cs.out("Connection Established.");
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
