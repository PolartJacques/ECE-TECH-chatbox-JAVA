package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ClientHandler implements Runnable {

	// VARIABLES DECLARATION
	private server myServer;
	private Socket s;
	private InputStream is;
	private OutputStream os;
	private String username;
	
	
	
	// CONSTRUCTOR
	public ClientHandler(Socket s, server myServer) throws IOException {
		super();
		this.s = s;
		this.is = s.getInputStream();
		this.os = s.getOutputStream();
		this.myServer = myServer;
		System.out.println("Connected");
	}

	
	
	// GETTERS AND SETTERS
	public OutputStream getOs() {
		return os;
	}
	
	public String getUsername() {
		return username;
	}

	
	
	// AUTO GENERATED METHODES
	@Override
	public void run() {
		// looking for incoming messages
		try {
			while(true) {
				// Receive a message
				byte[] rep = new byte[200];
				is.read(rep);
				
				// convert in JSON
				ObjectNode json = new ObjectMapper().readValue(new String(rep), ObjectNode.class);
				System.out.println("recive : " + json);
				
				// check message type
				String type = json.get("type").asText();
				String content = json.get("content").asText();
				
				if(type.equals("message")) {
					// send message to all client
					System.out.println("send to all : " + json);
					this.myServer.sendMsgToAll(rep);
				} else if(type.equals("new connection")) {
					// set user name
					this.username = content;
					System.out.println("ClientHandler get identity from : " + this.username);
					// send list of connected user
					ArrayList<String> userList = myServer.getConnectedUsers();
					sendMessage("send user list", userList.toString());
					// notify new connection
					this.myServer.sendMsgToAllOthers(rep, this.username);
				} else if(type.equals("disconnect")) {
					this.myServer.sendMsgToAllOthers(rep, this.username);
					this.s.close();
					this.myServer.removeUser(this.username);
					break;
				}
			}
			System.out.println(this.username + " disconnect - end of thread");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// METHODES
	public void sendMessage(String type, String content) {
		try {
			// build the message in JSON
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode messageInJson = mapper.createObjectNode();
			messageInJson.put("type", type);
			messageInJson.put("content", content);
			// convert the message in bytes array
			byte[] messageInBytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageInJson).getBytes();
			// send the message
			System.out.println("send : " + messageInJson);
			os.write(messageInBytes);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	
	
	// MAIN
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
