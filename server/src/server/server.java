package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class server {
	
	// VARIABLES DECLARATIONS
	private int PORT;
	private ArrayList<ClientHandler> clients;
	private ExecutorService pool;

	
	
	// CONSTRUCTOR
	public server(ArrayList<ClientHandler> clients, ExecutorService pool, int PORT) {
		super();
		this.clients = clients;
		this.pool = pool;
		this.PORT = PORT;
	}
	
	
	
	// METHODES
	public void sendMsgToAll(byte[] b) {
		System.out.println(new String(b));
		for (int i = 0; i < this.clients.size(); i++) {
			try {
				this.clients.get(i).getOs().write(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendMsgToAllOthers(byte[] b, String username) {
		System.out.println(new String(b));
		for (int i = 0; i < this.clients.size(); i++) {
			if(!this.clients.get(i).getUsername().equals(username)) {
				try {
					this.clients.get(i).getOs().write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<String> getConnectedUsers() {
		ArrayList<String> connectedUsers = new ArrayList<String>();
		for (int i = 0; i < clients.size(); i++) {
			String username = clients.get(i).getUsername();
			if(username != null) {
				connectedUsers.add(username);
			}
		}
		return connectedUsers;
	}
	
	public void removeUser(String username) {
		for (int i = 0; i < clients.size(); i++) {
			if(clients.get(i).getUsername().equals(username)) {
				clients.remove(i);
			}
		}
	}
	
	
	
	// MAIN
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			// create the server, with the list of client and a thread executor and the port
			server myServer = new server(new ArrayList<ClientHandler>(), Executors.newFixedThreadPool(2), 8080);
			
			// lunch the server
			try {
				ServerSocket ss = new ServerSocket(myServer.PORT);
				System.out.println("server created");
				
				while(true) {
					System.out.println("waiting for client");
					Socket s = ss.accept(); // block until he get something
					ClientHandler newClient = new ClientHandler(s, myServer);
					myServer.clients.add(newClient);
					myServer.pool.execute(newClient);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
