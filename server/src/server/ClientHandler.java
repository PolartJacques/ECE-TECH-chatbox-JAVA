package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket s;
	InputStream is;
	OutputStream os;
	
	public ClientHandler(Socket s) throws IOException {
		super();
		this.s = s;
		this.is = s.getInputStream();
		this.os = s.getOutputStream();
		System.out.println("Connected");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
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
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
