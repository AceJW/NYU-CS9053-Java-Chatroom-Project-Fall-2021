package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ChatSocket extends Thread{
	String userName;
	String receiver = "Public Channel";
	Socket socket;
	BufferedWriter writer;
	public ChatSocket(Socket s) {
		this.socket = s;
	}
	public ChatSocket(Socket s, String userName) {
		this.socket = s;
		this.userName = userName;
	}
	public void out(String out) {
		try {
//			System.out.println("out: "+ out);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(out);
			writer.newLine();
			writer.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {

		try {
			BufferedReader br = 
					new BufferedReader(
							new InputStreamReader(
									socket.getInputStream(), "UTF-8"));
			String line = null;
			while((line = br.readLine()) != null) {
				if(line.contains("SYSTEM_CHANGE_on_message_receiver1250: ")) {
					receiver = line.substring(39);
					System.out.println("receiver:" + receiver);
				} else if (line.contains("SYSTEM_CHANGE_on_username1250: ")){ 
					userName = line.substring(31);
					if(ChatManager.getCM().checkExistence(userName, this)) {
//						socket.shutdownInput(); 
//						socket.shutdownOutput();
						out("SYSTEM_WARNING_account_already_exist1250");
						ChatManager.getCM().clientList.remove(this);
						socket.close();
					} else {
						ChatManager.getCM().publish(this, "SYSTEM_CHANGE_on_add_online_client1250: "+ userName);
					}
//					System.out.println(userName);
				} else {
					if(receiver.equals("Public Channel")) {
						ChatManager.getCM().publish(this, line);
					} else {
						ChatManager.getCM().sendToIndividual(this, receiver, line);
					}
				}			
			}
			br.close();
//			socket.shutdownInput();
//			socket.shutdownOutput();
		} catch (IOException e) {
			System.out.println("A client lost connection.");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ChatManager.getCM().clientList.remove(this);
			System.out.println("Clients number: " + ChatManager.getCM().clientList.size());
			System.out.println("A client closed connection.");
			ChatManager.getCM().publish(this, "SYSTEM_CHANGE_on_remove_online_client1250: "+ userName);
		}
//		int count = 0;
//		while(true) {
//			count++;
//			out("loop:" + count+ "  ");
//			try {
//				sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}
}
