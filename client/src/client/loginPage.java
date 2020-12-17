package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Color;

public class loginPage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * Create the dialog.
	 */
	public loginPage(IHM myIHM, boolean modal) {
		loginPage myFrame = this;
		myFrame.setModal(modal);
		
		// manage closing event
		myFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		myFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	System.exit(DISPOSE_ON_CLOSE);
		    }
		});
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblUsername = new JLabel("username");
		lblUsername.setBounds(176, 84, 90, 15);
		contentPanel.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(165, 111, 114, 19);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblUserNotFound = new JLabel("user not found");
		lblUserNotFound.setVisible(false);
		lblUserNotFound.setForeground(Color.RED);
		lblUserNotFound.setBounds(170, 195, 119, 15);
		contentPanel.add(lblUserNotFound);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						String username = textField.getText();
						// connect to database
						final String DB_USER = "JacquesPolart";
						final String DB_PASSWORD = "Superdragon11";
						final String DB_NAME = "minichat_database";
						final String TABLE_NAME = "user";
						try {
							Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/" + DB_NAME + "?useSSL=false&serverTimezone=Europe/Paris", DB_USER, DB_PASSWORD);
							System.out.println("connexion reussie");
							Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
							String query = "SELECT COUNT(1) FROM " + TABLE_NAME + " WHERE username = '" + username + "'"; // count number of row (max 1) where 'username' = username
							ResultSet rs = stmt.executeQuery(query);	// return 1 row with one column containing 1 if the user was found, else 0
							rs.first();
							if(rs.getInt(1) == 1) {
								con.close();
								myFrame.dispose();
								myIHM.getFrame().setTitle(username);
								myIHM.getFrame().setVisible(true);
								myIHM.setUsername(username);
							} else {
								lblUserNotFound.setVisible(true);
								textField.setText("");
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						System.exit(DISPOSE_ON_CLOSE);
					}
				});
			}
		}
	}
}
