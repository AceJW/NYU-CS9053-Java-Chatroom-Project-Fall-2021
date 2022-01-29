package Server;

import java.util.ArrayList;
import java.util.Vector;

import view.MainWindow;

public class ChatManager {
	private ChatManager() {}
	private static final ChatManager cm = new ChatManager();
	public static ChatManager getCM() {
		return cm;
	}
	ArrayList<ChatSocket> clientList = new ArrayList<ChatSocket>();
	
	public void add(ChatSocket cs) {
		clientList.add(cs);
	}
	
	public void publish(ChatSocket cs, String message) {
//		System.out.println("message: "+ message);
//		System.out.println("size: "+ clientList.size());
		for(int i = 0; i < clientList.size(); i++) {
			ChatSocket csChatSocket = clientList.get(i);
			if(!cs.equals(csChatSocket)) {
				csChatSocket.out(message);
			}
		}
	}
	public void updateOnlineClients (ChatSocket receiver) {
		for(ChatSocket cs: clientList) {
			if(!cs.equals(receiver)) {
				receiver.out("SYSTEM_CHANGE_on_add_online_client1250: "+ cs.userName);
			}
		}
	}
	
	public void sendToIndividual(ChatSocket sender, String receiver, String message) {
		boolean found = false;
		for(int i = 0; i < clientList.size(); i++) {
			ChatSocket csChatSocket = clientList.get(i);
			if(csChatSocket.userName.equals(receiver)) {
				csChatSocket.out(message);
				found = true;
			}
		}
		if(!found) {
			sender.out(receiver + " is offline, message sent failed. (SYSTEM_NOTICE)");
		}
	}
	public boolean checkExistence(String username, ChatSocket chatsocket) {
		for(ChatSocket cs: clientList) {
			if(cs.userName.equals(username) && chatsocket != cs) {
				return true;
			}
		}
		return false;
	}
}
