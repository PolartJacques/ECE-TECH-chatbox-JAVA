package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class server {
	
	private static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private static ExecutorService pool = Executors.newFixedThreadPool(2);

	// MAIN
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*try {
			ServerSocket ss = new ServerSocket(8080 , 2);
			System.out.println("server created");
			
			Socket s = ss.accept(); // block until he get something
			System.out.println("Connected");
			
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			
			while(true) {
				// Receive a message
				byte[] b = new byte[200];
				is.read(b);
				System.out.println("from client : " + new String(b));
				// send message
				os.write(b);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			ServerSocket ss = new ServerSocket(8080 , 2);
			System.out.println("server created");
			
			while(true) {
				System.out.println("waiting for client");
				Socket s = ss.accept(); // block until he get something
				ClientHandler newClient = new ClientHandler(s);
				System.out.println("created new client");
				clients.add(newClient);
				System.out.println("added new client in array");
				pool.execute(newClient);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
