package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JList;

public class IHM implements Runnable {

	// VARIABLES DECLARATION
	private JFrame frame;
	private JTextArea textArea;
	private JLabel lblUsername;
	private JLabel lblEtat;
	private JLabel lblBytes;
	private DefaultListModel<String> listModel;
	
	private Socket s;
	private OutputStream o;
	private InputStream i;
	private SocketAddress me;
	private int PORT = 8080;
	private String ADDRESS = "127.0.0.1";
	
	private Thread thread;
	
	private String username;
	private ArrayList<String> participants;

	
	
	// CONSTRUCTOR
	public IHM() {
		initialize();
	}
	
	public IHM(String username) {
		super();
		this.username = username;
	}
	
	
	
	// GETTERS AND SETTERS
	public JFrame getFrame() {
		return frame;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public JLabel getlblUsername() {
		return lblUsername;
	}
	
	
	
	// AUTO GENERATED METHODES
	private void initialize() {
		frame = new JFrame();
		getFrame().setBounds(100, 100, 600, 500);
		// getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		// manage closing event
		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	disconnect();
		    	System.exit(JFrame.DISPOSE_ON_CLOSE);
		    }
		});
		
		textArea = new JTextArea();
		textArea.setBounds(12, 49, 300, 250);
		getFrame().getContentPane().add(textArea);
		
		JTextField textField = new JTextField();
		textField.setBounds(12, 311, 300, 19);
		getFrame().getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// get the message
				String message = textField.getText();
				// send the message
				sendMessage("message", message);
				// reset input text
				textField.setText("");
			}
		});
		btnEnvoyer.setBounds(12, 342, 117, 25);
		getFrame().getContentPane().add(btnEnvoyer);
		
		lblEtat = new JLabel("Etat : non connecté");
		lblEtat.setBounds(12, 443, 175, 15);
		getFrame().getContentPane().add(lblEtat);
		
		lblBytes = new JLabel("0 bytes");
		lblBytes.setBounds(12, 416, 70, 15);
		getFrame().getContentPane().add(lblBytes);
		
		lblUsername = new JLabel(username);
		lblUsername.setBounds(209, 17, 70, 15);
		frame.getContentPane().add(lblUsername);
		
		listModel = new DefaultListModel<String>();
		JList<String> list = new JList<String>(listModel);
		list.setBounds(386, 49, 155, 250);
		frame.getContentPane().add(list);
		
		JLabel lblParticiapnts = new JLabel("Particiapnts");
		lblParticiapnts.setBounds(386, 17, 98, 15);
		frame.getContentPane().add(lblParticiapnts);
		
		JButton btnJoindre = new JButton("Joindre");
		btnJoindre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connectToServer();
				if(s.isConnected()) {
					lblEtat.setText("Etat : connecté");
				}
			}
		});
		btnJoindre.setBounds(12, 12, 117, 25);
		getFrame().getContentPane().add(btnJoindre);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("in thread");
		try {
			while (true) {
				// get back response in bytes
				byte[] rep = new byte[200];
				int n = i.read(rep);
				
				// convert in JSON
				ObjectNode json = new ObjectMapper().readValue(new String(rep), ObjectNode.class);
				System.out.println("recive : " + json);
				
				// check type of the message
				String type = json.get("type").asText();
				
				// extract the content
				String content = json.get("content").asText();
				if(type.equals("message")) {
					lblBytes.setText(n + " bytes");
					// display the message
					textArea.append(content + "\n");
				} else if(type.equals("send user list")) {
					// convert content from String to ArrayList
					participants = new ArrayList<>(Arrays.asList(content.substring(1, content.length() - 1).split(", ")));
					// display participants
					for(int i = 0; i < participants.size(); i++) {
						listModel.addElement(participants.get(i));
					}
				} else if(type.equals("new connection")) {
					listModel.addElement(content);
				} else if(type.equals("disconnect")) {
					listModel.removeElement(content);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// METHODES
	public void connectToServer() {
		// connect to the server
		s = new Socket();
		me = new InetSocketAddress(ADDRESS, PORT);
		try {
			s.connect(me);
			System.out.println("connected to the server");
			o = s.getOutputStream();
			i = s.getInputStream();
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// notify the new connection
		sendMessage("new connection", username);
		
	}
	
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
			o.write(messageInBytes);
			o.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void disconnect() {
		// inform the server that you disconnect
		sendMessage("disconnect", this.username);
		// close the socket and the thread
		thread.stop();
    	try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// MAIN
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHM myIHM = new IHM();
					myIHM.thread = new Thread(myIHM);	// we initialize the Thread here so we can pass myIHM and have access to all variables
					loginPage login = new loginPage(myIHM, true);
					login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
