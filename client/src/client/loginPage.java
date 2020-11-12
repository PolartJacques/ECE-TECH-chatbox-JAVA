package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

public class loginPage {

	private JFrame frame;
	private JTextField textField_username;
	private JTextField textField_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginPage window = new loginPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public loginPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(41, 33, 70, 15);
		frame.getContentPane().add(lblUsername);
		
		textField_username = new JTextField();
		textField_username.setBounds(41, 60, 114, 19);
		frame.getContentPane().add(textField_username);
		textField_username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(41, 113, 70, 15);
		frame.getContentPane().add(lblPassword);
		
		textField_password = new JTextField();
		textField_password.setBounds(41, 140, 114, 19);
		frame.getContentPane().add(textField_password);
		textField_password.setColumns(10);
		
		JButton btnConnect = new JButton("connect");
		btnConnect.setBounds(41, 207, 117, 25);
		frame.getContentPane().add(btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String username = textField_username.getText();
				String password = textField_password.getText();
				
				if(!username.isEmpty() && !password.isEmpty()) {
					if(username.equals("jacques") && password.equals("test")) {
						frame.dispose();
						IHM newWindow = new IHM();
						newWindow.setUsername(username);
						newWindow.getlblUsername().setText(username);
						newWindow.getFrame().setVisible(true);
					} else {
						
					}
				} else {
					
				}
			}
		});
	}
}
