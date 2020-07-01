package Coursework;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;

public class BasketFrame extends JFrame {

	private JPanel contentPane;
	private JTextField Title;
	private JButton Savebtn;
	private JButton Cancelbtn;
	private JButton PayCardbtn;
	private JButton PayPalbtn;
	private JLabel Quantitylbl;
	private JButton Back;
	private JTextField PayPallbl;
	private JTextField EmailEntry;
	private JTextField CardNoEntry;
	private JTextField CodeEntry;
	private JTextField QuantityEntry;
	private JLabel Emaillbl;
	private JLabel CreditCardlbl;
	private JLabel CardNolbl;
	private JLabel Codelbl;
	private JTextField ErrorMessage;
	private String errormessage;
	

	//Method to get the Last Line in the ActivityLogs file, in order to append the line and add the appropriate log depending on the action
	
	public String getLastLine() throws IOException {
		FileInputStream in = new FileInputStream("Active.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		         
		String strLine = null, tmp;
		
		while ((tmp = br.readLine()) != null) // While there is no next line, which means that it is the last line
		{
		   strLine = tmp;
		}
		         
		String lastLine = strLine;
		     
		in.close();
		return lastLine;
	}
	
	//Method to edit the stock available value in the Product Listing, depending on the number of quantities bought by the User
	
	public void editStock(int Stock) throws IOException {
		
		File tempFile = new File("Active.txt");
		Scanner scan = new Scanner(tempFile);
		String selection = scan.nextLine();
		String[] split = selection.split(",");
		String code = split[0].trim();
		File data = new File("StockData.txt");
		Scanner sc = new Scanner(data);
		Customer p = null;
		BufferedReader file = new BufferedReader(new FileReader("StockData.txt"));
        String barcode = "";
        int stock = 0;
        String newline = "";
        FileWriter fw1 = new FileWriter("Active.txt", false);
        FileWriter fw = new FileWriter("Active.txt", true);
        PrintWriter pw = new PrintWriter(fw);
        pw.flush();
		while (sc.hasNextLine()) {
			String currentline = file.readLine();
			String[] lines = sc.nextLine().split(",");
			p = new Customer(Integer.parseInt(lines[0].trim()), lines[1].trim(), lines[2].trim(), lines[3].trim(), lines[4].trim(), lines[5].trim(), Integer.parseInt(lines[6].trim()), Float.parseFloat(lines[8].trim()), lines[9].trim());
			barcode = String.valueOf(p.retCode());
			stock = p.retStock();
			int newStock = p.retStock() - Stock;
			if (barcode.equals(code)) {
				if (Stock <= stock) {
					newline = lines[0]+","+lines[1]+","+lines[2]+","+lines[3]+","+lines[4]+","+lines[5]+", "+String.valueOf(newStock)+","+lines[7]+","+lines[8]+","+lines[9];
		            currentline.replace(lines[6], String.valueOf(newStock));
		            if (sc.hasNextLine()) {
		            	fw.write(newline);
						fw.write("\n");
		            	continue;
		            } else {
		            	fw.write(newline);
		            }
				} else {
					fw.write(currentline);
					fw.write("\n");
				}
					
			} else if (!(barcode.equals(code))){
				if (sc.hasNextLine()) {
					fw.write(currentline);
					fw.write("\n");
	            	continue;
	            } else {
	            	fw.write(currentline);
	            }
			}
		}
		fw.close();
	}
	
	public void copystock() throws IOException {
		File file = new File("Active.txt");
		Scanner sc = new Scanner(file);
		sc.useDelimiter("\\Z");
        FileWriter fw1 = new FileWriter("StockData.txt", false);
        FileWriter fw = new FileWriter("stockData.txt", true);
        while(sc.hasNextLine()) {
        	if (sc.hasNextLine()) {
	        	fw.write(sc.nextLine());
	        	fw.write("\n");
        	} else if (!(sc.hasNextLine())){
        		fw.write(sc.nextLine());
        	}
        }
        fw.close();
	}
	
	//Method to write the action of the User to the ActivityLogs file, depending if the item was cancelled or saved
	
	public String writeLog(String line, int Quantity, String method, String text) throws IOException {
		LocalDate objDate = LocalDate.now();
		String strDate1= objDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); //Changes the format of the current date to the required format
		String[] split = line.split(",");
		String code = split[0].trim();
		File data = new File("StockData.txt");
		Scanner sc = new Scanner(data);
		User p = null;
		while (sc.hasNextLine()) {
			String[] lines = sc.nextLine().split(",");
			p = new User(Integer.parseInt(lines[0].trim()), lines[1].trim(), lines[2].trim(), lines[3].trim(), lines[4].trim(), lines[5].trim(), Integer.parseInt(lines[6].trim()), Float.parseFloat(lines[7].trim()), lines[8].trim());
			String barcode = String.valueOf(p.retCode());
			int stock = p.retStock();
			if (barcode.contains(code)) {		//Iterates through the Stock file and matches the item selected with its occurence in the stock file
				if (Quantity <= stock) {	//Only allows there to be a log if the quantity selected is acceptable for the stock available for that item
					File tempFile = new File("Active.txt");
					Scanner scan = new Scanner(tempFile);
					String selection = scan.nextLine();
					UserFrame uframe = new UserFrame();
					uframe.writeSelection(selection);
					FileWriter write = new FileWriter("ActivityLogs.txt", true);
					write.write(Quantity+", "+text+", "+method+", "+strDate1);	//Writes the remaining details to the ActivityLogs file
					write.close();
					errormessage = "Item " + (String)text;
					
					
				} else {
					
					errormessage = "Insufficient Stock for Quantity Entered!";
					
				}
			}
			
		}
		return errormessage;
	}
	
	//Method to write the action of the User to the ActivityLogs File depending if the item is purchased by PayPal or Credit Card
	
	public String writepayLog(String line, int Quantity, String method, String text) throws IOException {
		LocalDate objDate = LocalDate.now();
		String strDate1= objDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")); //Changes the format of the date to write to the log file
		String[] split = line.split(",");
		String code = split[0].trim();
		File data = new File("StockData.txt");
		Scanner sc = new Scanner(data);
		Customer p = null;
		while (sc.hasNextLine()) {
			String[] lines = sc.nextLine().split(",");
			//Creates a Customer object to get fields from the required item
			p = new Customer(Integer.parseInt(lines[0].trim()), lines[1].trim(), lines[2].trim(), lines[3].trim(), lines[4].trim(), lines[5].trim(), Integer.parseInt(lines[6].trim()), Float.parseFloat(lines[8].trim()), lines[9].trim());
			String barcode = String.valueOf(p.retCode());
			int stock = p.retStock();
			float price = p.retprice();
			if (barcode.equals(code)) {
				if (Quantity <= stock) {
					File tempFile = new File("Active.txt");
					Scanner scan = new Scanner(tempFile);
					String selection = scan.nextLine();
					UserFrame uframe = new UserFrame();
					uframe.writeSelection(selection);
					FileWriter write = new FileWriter("ActivityLogs.txt", true);	//Sets the Log file to allow editing
					write.write(Quantity+", "+text+", "+method+", "+strDate1);		
					write.close();
					float total = Quantity*price;		//Gets the Total of the items purchased
					errormessage = String.valueOf(total) +" paid using "+ (String) method;
				} else {
					errormessage = "Insufficient Stock for Quantity Entered!";
				}
			}
			
		}
		return errormessage;
	}

	/**
	 * Create the frame.
	 */
	public BasketFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Title = new JTextField();
		Title.setText("Computer Accessories Shop");
		Title.setHorizontalAlignment(SwingConstants.CENTER);
		Title.setForeground(Color.BLACK);
		Title.setFont(new Font("Arial", Font.BOLD, 40));
		Title.setEditable(false);
		Title.setColumns(10);
		Title.setBorder(null);
		Title.setBackground(Color.CYAN);
		Title.setBounds(10, 11, 743, 61);
		contentPane.add(Title);
		
		JButton Savebtn = new JButton("Save Item");
		Savebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Allows the user to save the item if a quantity has been entered
				 
				if (QuantityEntry.getText().isEmpty()) {
					ErrorMessage.setEditable(true);
					ErrorMessage.setText("Enter a Quantity");
					
				} else {
					try {
						writeLog(getLastLine(), Integer.parseInt(QuantityEntry.getText()),"", "saved");
						ErrorMessage.setEditable(true);
						ErrorMessage.setText(errormessage);
						QuantityEntry.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		Savebtn.setFont(new Font("Arial", Font.BOLD, 16));
		Savebtn.setBounds(21, 478, 116, 30);
		contentPane.add(Savebtn);
		
		// Allows the user to cancel the item if a quantity has been entered
		
		JButton Cancelbtn = new JButton("Cancel Item");
		Cancelbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (QuantityEntry.getText().isEmpty()) {
					ErrorMessage.setEditable(true);
					ErrorMessage.setText("Enter a Quantity");
				} else {
					try {
						writeLog(getLastLine(), Integer.parseInt(QuantityEntry.getText()),"", "cancelled");
						ErrorMessage.setEditable(true);
						ErrorMessage.setText(errormessage);
						QuantityEntry.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		Cancelbtn.setFont(new Font("Arial", Font.BOLD, 16));
		Cancelbtn.setBounds(171, 478, 123, 30);
		contentPane.add(Cancelbtn);
		
		JButton PayCardbtn = new JButton("Pay by Card");
		PayCardbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Allows the user to pay for the item if a Quantity, Card Number and Security Code has been entered
				 
				if (CardNoEntry.getText().isEmpty() || CodeEntry.getText().isEmpty()) {
					ErrorMessage.setEditable(true);
					ErrorMessage.setText("Enter the required fields");
				} else {
					if (CardNoEntry.getText().length() != 16) {
						ErrorMessage.setEditable(true);
						ErrorMessage.setText("Invalid Card Number");
					} else {
						if (CodeEntry.getText().length() != 3) {
							ErrorMessage.setEditable(true);
							ErrorMessage.setText("Invalid Security Code");
						} else {
							if (QuantityEntry.getText().isEmpty()) {
								ErrorMessage.setEditable(true);
								ErrorMessage.setText("Enter a Quantity");
							} else {
								try {
									writepayLog(getLastLine(), Integer.parseInt(QuantityEntry.getText()),"Credit Card", "purchased");
									editStock(Integer.parseInt(QuantityEntry.getText()));
									copystock();
									ErrorMessage.setEditable(true);
									ErrorMessage.setText(errormessage);
									QuantityEntry.setText("");
									CodeEntry.setText("");
									CardNoEntry.setText("");
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
					}
				}
			}
		});
		PayCardbtn.setFont(new Font("Arial", Font.BOLD, 16));
		PayCardbtn.setBounds(581, 480, 156, 27);
		contentPane.add(PayCardbtn);
		
		JButton PayPalbtn = new JButton("Pay by PayPal");
		PayPalbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Allows the user to pay for the item if a Quantity and Email has been entered
				 
				if (EmailEntry.getText().isEmpty()) {
					ErrorMessage.setEditable(true);
					ErrorMessage.setText("Please Enter your Email Address");
					
				} else {
					if (!(EmailEntry.getText().contains("@"))) {
						ErrorMessage.setEditable(true);
						ErrorMessage.setText("Please Enter a Valid Email Address");
					} else {
						if (QuantityEntry.getText().isEmpty() ) {
							ErrorMessage.setEditable(true);
							ErrorMessage.setText("Enter a Quantity");
						} else {
							try {
								writepayLog(getLastLine(), Integer.parseInt(QuantityEntry.getText()),"PayPal", "purchased");
								editStock(Integer.parseInt(QuantityEntry.getText()));
								copystock();
								ErrorMessage.setEditable(true);
								ErrorMessage.setText(errormessage);
								QuantityEntry.setText("");
								EmailEntry.setText("");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		PayPalbtn.setFont(new Font("Arial", Font.BOLD, 16));
		PayPalbtn.setBounds(404, 478, 141, 30);
		contentPane.add(PayPalbtn);
		
		PayPallbl = new JTextField();
		PayPallbl.setText("PayPal");
		PayPallbl.setFont(new Font("Arial", Font.BOLD, 16));
		PayPallbl.setColumns(10);
		PayPallbl.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		PayPallbl.setBackground(Color.CYAN);
		PayPallbl.setBounds(10, 303, 96, 20);
		contentPane.add(PayPallbl);
		
		JLabel Emaillbl = new JLabel("Email Address:");
		Emaillbl.setFont(new Font("Arial", Font.BOLD, 16));
		Emaillbl.setBounds(10, 344, 141, 23);
		contentPane.add(Emaillbl);
		
		EmailEntry = new JTextField();
		EmailEntry.setFont(new Font("Arial", Font.PLAIN, 14));
		EmailEntry.setColumns(10);
		EmailEntry.setBounds(149, 345, 145, 22);
		contentPane.add(EmailEntry);
		
		JLabel CreditCardlbl = new JLabel("Credit Card");
		CreditCardlbl.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		CreditCardlbl.setFont(new Font("Arial", Font.BOLD, 16));
		CreditCardlbl.setBounds(444, 300, 105, 23);
		contentPane.add(CreditCardlbl);
		
		JLabel CardNolbl = new JLabel("Card Number:");
		CardNolbl.setFont(new Font("Arial", Font.BOLD, 16));
		CardNolbl.setBounds(444, 344, 128, 23);
		contentPane.add(CardNolbl);
		
		JLabel Codelbl = new JLabel("Security Code:");
		Codelbl.setFont(new Font("Arial", Font.BOLD, 16));
		Codelbl.setBounds(444, 378, 128, 23);
		contentPane.add(Codelbl);
		
		CardNoEntry = new JTextField();
		CardNoEntry.setFont(new Font("Arial", Font.PLAIN, 14));
		CardNoEntry.setColumns(10);
		CardNoEntry.setBounds(593, 344, 145, 23);
		contentPane.add(CardNoEntry);
		
		CodeEntry = new JTextField();
		CodeEntry.setFont(new Font("Arial", Font.PLAIN, 14));
		CodeEntry.setColumns(10);
		CodeEntry.setBounds(593, 378, 145, 23);
		contentPane.add(CodeEntry);
		
		QuantityEntry = new JTextField();
		QuantityEntry.setFont(new Font("Arial", Font.PLAIN, 14));
		QuantityEntry.setColumns(10);
		QuantityEntry.setBounds(114, 151, 108, 23);
		contentPane.add(QuantityEntry);
		
		JLabel Quantitylbl = new JLabel("Quantity:");
		Quantitylbl.setFont(new Font("Arial", Font.BOLD, 16));
		Quantitylbl.setHorizontalAlignment(SwingConstants.LEFT);
		Quantitylbl.setBounds(21, 150, 113, 23);
		contentPane.add(Quantitylbl);
		
		JButton Back = new JButton("Back");
		Back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserFrame uframe = new UserFrame();
				uframe.setVisible(true);
				dispose();
			}
		});
		Back.setFont(new Font("Tahoma", Font.BOLD, 14));
		Back.setBounds(630, 96, 123, 30);
		contentPane.add(Back);
		
		ErrorMessage = new JTextField();
		ErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		ErrorMessage.setBorder(null);
		ErrorMessage.setBackground(Color.CYAN);
		ErrorMessage.setBounds(10, 195, 727, 73);
		ErrorMessage.setFont(new Font("Arial", Font.BOLD, 20));
		ErrorMessage.setForeground(Color.BLACK);
		contentPane.add(ErrorMessage);
		ErrorMessage.setColumns(10);
	}
}
