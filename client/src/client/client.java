package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class client {
	
	// MAIN
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Socket s = new Socket();
		SocketAddress me = new InetSocketAddress("127.0.0.1", 8080);
		try {
			s.connect(me);
			System.out.println("connected to the server");
			InputStream i = s.getInputStream();
			OutputStream o = s.getOutputStream();
			
			Scanner scanner = new Scanner(System. in);
			while(true) {
				//send a message
				String inputString = scanner. nextLine();
				o.write(inputString.getBytes());
				
				// receive message
				byte[] rep = new byte[200];
				int n = i.read(rep);
				System.out.println("server : " + new String(rep));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
