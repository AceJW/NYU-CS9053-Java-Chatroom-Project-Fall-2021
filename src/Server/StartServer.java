package Server;

public class StartServer {
	public static void main(String[] args) {
		new Thread(new ServerListener()).start();
		System.out.println("Server has started.");
	}
}
