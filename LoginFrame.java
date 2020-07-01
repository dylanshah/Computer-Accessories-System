package Coursework;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField Title;
	private JComboBox UserId;
	private JLabel username;
	private JButton login;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//Method to append all the user Id's to an array that will be used to select from when logging in
	
	public String[] AccountId() throws IOException {
		File account = new File("UserAccounts.txt");
		Scanner useracc = new Scanner(account);
		
		//Creates an array length based on number of users
		
		FileInputStream fis = new FileInputStream(account);
		byte[] byteArray = new byte[(int) account.length()];
		fis.read(byteArray);
		String data = new String(byteArray);
		String[] stringArray = data.split("\r\n");
		int arrayIndex = 0;
		String[] userArray = new String[stringArray.length];
		while (useracc.hasNextLine()) {
			String[] useraccounts = useracc.nextLine().split(",");
			Person acc = new Person(Integer.parseInt(useraccounts[0].trim()), useraccounts[1].trim(), useraccounts[2].trim(), Integer.parseInt(useraccounts[3].trim()), useraccounts[4].trim(), useraccounts[5].trim(), useraccounts[6].trim());
			userArray[arrayIndex] = String.valueOf(acc.retId()); //Adds the Id's to the array 
			arrayIndex++;
		}
		useracc.close();
		return userArray;

	}
	
	//Method to get the type of user, which will be used to redirect the user to the correct window
	
	public String[] AccountAccess() throws IOException {
		File account1 = new File("UserAccounts.txt");
		Scanner useracc1 = new Scanner(account1);
		FileInputStream fis = new FileInputStream(account1);
		byte[] byteArray = new byte[(int) account1.length()];
		fis.read(byteArray);
		String data = new String(byteArray);
		String[] stringArray = data.split("\r\n");
		int arrayIndex1 = 0;
		String[] accessArray = new String[stringArray.length];
		while (useracc1.hasNextLine()) {
			String[] useraccounts1 = useracc1.nextLine().split(",");
			Person access = new Person(Integer.parseInt(useraccounts1[0].trim()), useraccounts1[1].trim(),useraccounts1[2].trim(), Integer.parseInt(useraccounts1[3].trim()), useraccounts1[4].trim(),useraccounts1[5].trim(), useraccounts1[6].trim());
			accessArray[arrayIndex1] = access.retType(); // Adds the user type to the array
			arrayIndex1++;
		}
		useracc1.close();
		return accessArray;
	}
	
	//Method to write the user ID selected and its postcode to the ActivityLogs file upon Checkout
	
	public void Id(String  entry) throws IOException {
		FileWriter writer = new FileWriter("ActivityLogs.txt", true);
		File account1 = new File("UserAccounts.txt");
		Scanner useracc1 = new Scanner(account1);
		while (useracc1.hasNextLine()) {
			String[] useraccounts1 = useracc1.nextLine().split(",");
			
			//Creates the Person Object to pass fields to write in the ActivityLogs file
			
			Person access = new Person(Integer.parseInt(useraccounts1[0].trim()), useraccounts1[1].trim(),useraccounts1[2].trim(), Integer.parseInt(useraccounts1[3].trim()), useraccounts1[4].trim(),useraccounts1[5].trim(), useraccounts1[6].trim());
			String code = access.retCode();
			if (String.valueOf(access.retId()).equals(entry)) {
				writer.write("\n"+ entry+", "+code+", ");
				writer.close();
			}
		}
	}
	
	//Temporarily writes the selected user Id to a separate ID file for reference when checking out 
	
	public void writeItem(String entry) throws IOException {
		FileWriter writer1 = new FileWriter("Id.txt", false);
		FileWriter writer = new FileWriter("Id.txt", true);
		writer.write(entry);
		writer.close();
		
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public LoginFrame() throws IOException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Title = new JTextField();
		Title.setBounds(5, 5, 758, 73);
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		Title.setToolTipText("");
		Title.setText(" Computer Accessories Shop");
		Title.setFont(new Font("Arial", Font.BOLD, 40));
		Title.setEditable(false);
		Title.setColumns(10);
		Title.setBorder(null);
		Title.setBackground(Color.CYAN);
		contentPane.add(Title);
		
		String[] UserArray = null;
		UserArray = AccountId();
		
		JComboBox UserId = new JComboBox();
		UserId.setFont(new Font("Arial", Font.BOLD, 16));
		UserId.setBounds(329, 253, 116, 23);
		UserId.setModel(new DefaultComboBoxModel(UserArray));
		contentPane.add(UserId);
		
		JLabel username = new JLabel("Person ID:");
		username.setFont(new Font("Arial", Font.BOLD, 16));
		username.setBounds(225, 253, 81, 23);
		contentPane.add(username);
		
		String[] AccessArray = null;
		AccessArray = AccountAccess();
		
		JButton login = new JButton("Login");
		login.setBorder(null);
		login.setFont(new Font("Arial", Font.BOLD, 16));
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] AccessArray = null;
				try {
					AccessArray = AccountAccess();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				String value = UserId.getSelectedItem().toString();
				int index = UserId.getSelectedIndex();
				
				/*Opens the User Window if the user is a Customer or Admin based on the selected index 
				of the user ID matched to the index position in the array of User types*/
				
				if (AccessArray[index].equals("customer")) {
					UserFrame userfr = new UserFrame();
					userfr.setVisible(true);
					dispose();
					try {
						writeItem(UserId.getSelectedItem().toString());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					if (AccessArray[index].equals("admin")) {
						AdminFrame adminfr = new AdminFrame(value);
						adminfr.setVisible(true);
						dispose();
					}
				}
			}
		});
		login.setForeground(Color.BLACK);
		login.setBackground(new Color(192, 192, 192));
		login.setBounds(329, 299, 116, 23);
		contentPane.add(login);
	}
}
