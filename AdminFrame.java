package Coursework;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminFrame extends JFrame {

	private JPanel contentPane;
	private JTextField Title;
	private JTextField KeyboardEntry;
	private JTextField MouseEntry;
	private JComboBox display;
	private JLabel Detailslbl;
	private JLabel Detailslbl2;
	private JLabel Orderlbl;
	private JButton AddMouse;
	private JButton AddKeyboard;
	private JButton View;
	private JTextField ErrorMessage;
	private String errormessage;
	
	public String AddKeyboard(String entry) throws IOException {
		File code = new File("StockData.txt");
		Scanner codes = new Scanner(code);
		
		//Creates an array length based on barcodes
		
		FileInputStream fis = new FileInputStream(code);
		byte[] byteArray = new byte[(int) code.length()];
		fis.read(byteArray);
		String data = new String(byteArray);
		String[] stringArray = data.split("\r\n");
		int arrayIndex = 0;
		ArrayList<String> barcode = new ArrayList<>(stringArray.length);
		while (codes.hasNextLine()) {
			String[] barcodes = codes.nextLine().split(",");
			User acc = new User(Integer.parseInt(barcodes[0].trim()), barcodes[1].trim(), barcodes[2].trim(), barcodes[3].trim(), barcodes[4].trim(), barcodes[5].trim(), Integer.parseInt(barcodes[6].trim()), Float.parseFloat(barcodes[7].trim()), barcodes[8].trim());
			barcode.add(String.valueOf(acc.retCode())); //Adds the Id's to the array 
			arrayIndex++;
		}
		codes.close();
		
		String[] fields = entry.split(",");
		String type = fields[1].trim();
		int bar = Integer.parseInt(fields[0].trim());
		int stock = Integer.parseInt(fields[6].trim());
		float orprice = Float.parseFloat(fields[7].trim());
		float retprice = Float.parseFloat(fields[8].trim());
		FileWriter writer = new FileWriter("StockData.txt", true);
		//Validation of User Input
		if (bar > 100000) {
			if (!(barcode.contains(fields[0].trim()))) {
				if (type.trim().equals("keyboard")) {
					if (stock > 0) {
						if (orprice > 0) {
							if (retprice > 0.0) {
								writer.write("\n");
								writer.write(entry);
								errormessage ="Item Added";
							} else  {
								errormessage = "Enter a Valid Retail Price";
							}
						} else {
							errormessage = "Enter a Valid Original Price";
						}
					} else {
						errormessage = "Enter a Valid Stock Quantity";
					}
				} else {
					errormessage = "Enter Keyboard Details!";
				}
			} else {
				errormessage = "Item Already Present in Stock";
			}
		} else {
			errormessage = "Enter a Valid 6 Digit Barcode";
		}	
		writer.close();
		return errormessage;
	}
	
	//Method to Add a Mouse to Product List based on the specifications input
	
	public String AddMouse(String entry) throws IOException {
		File code = new File("StockData.txt");
		FileInputStream in = new FileInputStream("StockData.txt");
		Scanner codes = new Scanner(code);
		
		//Creates an array length based on barcodes
		
		FileInputStream fis = new FileInputStream(code);
		BufferedReader codes1 = new BufferedReader(new InputStreamReader(in));
		byte[] byteArray = new byte[(int) code.length()];
		fis.read(byteArray);
		String data = new String(byteArray);
		String[] stringArray = data.split("\r\n");
		int arrayIndex = 0;
		ArrayList<String> barcode = new ArrayList<>(stringArray.length);
		
		while (codes.hasNextLine()) {
			
			String[] barcodes = codes.nextLine().split(",");
			User acc = new User(Integer.parseInt(barcodes[0].trim()), barcodes[1].trim(), barcodes[2].trim(), barcodes[3].trim(), barcodes[4].trim(), barcodes[5].trim(), Integer.parseInt(barcodes[6].trim()), Float.parseFloat(barcodes[7].trim()), barcodes[8].trim());
			barcode.add(String.valueOf(acc.retCode())); //Adds the Id's to the array 
			arrayIndex++;
			
		}
		
		String[] fields = entry.split(",");
		String type = fields[1].trim();
		int bar = Integer.parseInt(fields[0].trim());
		int stock = Integer.parseInt(fields[6].trim());
		float orprice = Float.valueOf(fields[7].trim());
		float retprice = Float.valueOf(fields[8].trim());
		FileWriter writer = new FileWriter("StockData.txt", true);
		//Validation of User Input
		if (bar > 100000) {
			if (!(barcode.contains(fields[0].trim()))) {
				if (type.trim().equals("mouse")) {
					if (stock > 0) {
						if (orprice > 0) {
							if (retprice > 0.0) {
								writer.write("\n");
								writer.write(entry);
								errormessage ="Item Added";
							} else  {
								errormessage = "Enter a Valid Retail Price";
							}
						} else {
							errormessage = "Enter a Valid Original Price";
						}
					} else {
						errormessage = "Enter a Valid Stock Quantity";
					}
				} else {
					errormessage = "Enter Mouse Details!";
				}
			} else {
				errormessage = "Item Already Present in Stock";
			}
		} else {
			errormessage = "Enter a Valid 6 Digit Barcode";
		}
		codes.close();
		writer.close();
		return errormessage;
	}
	
	//Method to view the details of the items in the Product List
	
	public String viewDetails() throws IOException {
		File file = new File("StockData.txt");
		Scanner sc = new Scanner(file);
		sc.useDelimiter("\\Z");
		return sc.next();
	}
	
	//Method to sort the data in descending quantity of stock
	public void sort() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("StockData.txt"));
        ArrayList<Admin> order = new ArrayList<Admin>();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            String[] detail = currentLine.split(",");
            int code = Integer.valueOf(detail[0]);
            String item = detail[1].trim();
            String type = detail[2].trim();
            String brand = detail[3].trim();
            String colour = detail[4].trim();
            String connectivity = detail[5].trim();
            int stock = Integer.valueOf(detail[6].trim());
            float original = Float.valueOf(detail[7].trim());
            float retail = Float.valueOf(detail[8].trim());
            String additional = detail[9].trim();
         
            order.add(new Admin(code, item, type, brand, colour, connectivity, stock, original, retail, additional));
             
            currentLine = reader.readLine();
        }
        
        Collections.sort(order, new stockCompare());
        BufferedWriter writer = new BufferedWriter(new FileWriter("StockData.txt"));
        for (Admin admin : order) 
        {	
        	if (order.get(order.size() - 1) == admin) {
        		String prod = String.valueOf(admin.retCode())+", "+admin.retitem()+", "+admin.retkind()+", "+admin.retbrand()+", "+admin.retcolour()+", "+admin.retconnect()+", "+String.valueOf(admin.retStock())+", "+String.valueOf(admin.retOriginal())+", "+String.valueOf(admin.retprice())+", "+admin.rettype();
                writer.write(prod);
        	} else {
	        	String prod = String.valueOf(admin.retCode())+", "+admin.retitem()+", "+admin.retkind()+", "+admin.retbrand()+", "+admin.retcolour()+", "+admin.retconnect()+", "+String.valueOf(admin.retStock())+", "+String.valueOf(admin.retOriginal())+", "+String.valueOf(admin.retprice())+", "+admin.rettype()+"\n";
	            writer.write(prod);
        	}
        }
        
        reader.close();
        writer.close();
        
	}
	/*Method to display the Items from the Product List onto the Window. The items are put into an array then displayed*/
	
	public String[] display() throws IOException {
	    sort();
        File tempFile = new File("StockData.txt");
		Scanner scan = new Scanner(tempFile);
		FileInputStream fis = new FileInputStream(tempFile);
	    byte[] byteArray = new byte[(int)tempFile.length()];
	    fis.read(byteArray);
	    String data = new String(byteArray);
	    String[] stringArray = data.split("\n");
		int arrayIndex = 0;
		int arrayIndex1 = 0;
		String[] itemArray = new String[stringArray.length];
		int[] stockArray = new int[stringArray.length];
		int[] barcode = new int[stringArray.length];
		Admin acc = null;
		while (scan.hasNextLine()) {
			String[] entry =scan.nextLine().split(",");
			acc = new Admin(Integer.parseInt(entry[0].trim()), entry[1].trim(), entry[2].trim(), entry[3].trim(), entry[4].trim(), entry[5].trim(), Integer.parseInt(entry[6].trim()), Float.parseFloat(entry[7].trim()), Float.parseFloat(entry[8].trim()), entry[9].trim());
			itemArray[arrayIndex] = String.valueOf(acc.retCode())+", "+acc.retitem()+", "+acc.retkind()+", "+acc.retbrand()+", "+acc.retcolour()+", "+acc.retconnect()+", "+String.valueOf(acc.retStock())+", "+String.valueOf(acc.retOriginal())+", "+String.valueOf(acc.retprice())+", "+acc.rettype();
			arrayIndex++;
			
		}
		scan.close();
		return itemArray;
		
	}

	/**
	 * Create the frame.
	 */
	public AdminFrame(String Id) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 1000, 700);
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
		Title.setBounds(10, 11, 957, 57);
		contentPane.add(Title);
		
		JLabel Detailslbl = new JLabel("Barcode, \"keyboard\", Type, Brand, Colour, Connectivity, Quantity in Stock, Original Cost, Retail price, Keyboard Layout");
		Detailslbl.setFont(new Font("Arial", Font.BOLD, 12));
		Detailslbl.setBounds(10, 95, 671, 28);
		contentPane.add(Detailslbl);
		
		KeyboardEntry = new JTextField();
		KeyboardEntry.setColumns(10);
		KeyboardEntry.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		KeyboardEntry.setBackground(Color.CYAN);
		KeyboardEntry.setBounds(10, 130, 671, 34);
		contentPane.add(KeyboardEntry);
		
		MouseEntry = new JTextField();
		MouseEntry.setColumns(10);
		MouseEntry.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		MouseEntry.setBackground(Color.CYAN);
		MouseEntry.setBounds(10, 211, 671, 34);
		contentPane.add(MouseEntry);
		
		JLabel Detailslbl2 = new JLabel("Barcode, \"mouse\", Type, Brand, Colour, Connectivity, Quantity in Stock, Original Cost, Retail price, No. of Buttons");
		Detailslbl2.setFont(new Font("Arial", Font.BOLD, 12));
		Detailslbl2.setBounds(10, 182, 671, 28);
		contentPane.add(Detailslbl2);
		
		JComboBox display = new JComboBox();
		display.setFont(new Font("Tahoma", Font.BOLD, 13));
		display.setBounds(10, 322, 948, 28);
		contentPane.add(display);
		
		JLabel Orderlbl = new JLabel("Barcode,  Name,  Type,  Brand,  Colour,  Connectivity,  Stock,  Original Price, Retail Price,  Additional Info");
		Orderlbl.setFont(new Font("Arial", Font.BOLD, 14));
		Orderlbl.setBounds(10, 288, 730, 28);
		contentPane.add(Orderlbl);
		
		JButton AddMouse = new JButton("Add Mouse");
		AddMouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Only Adds the Mouse to the Product List if the Entry box is filled with the required text
				
				if (MouseEntry.getText().isEmpty()) {
					display.removeAllItems();
					display.addItem("Please Enter the Product Details");
				} else {
					try {
						display.removeAllItems();
						if (AddMouse(MouseEntry.getText()) == "Item Added") {
							ErrorMessage.setEditable(true);
							ErrorMessage.setText("Item Added");
							AddMouse(MouseEntry.getText());
							MouseEntry.setText("");
						} else {
							ErrorMessage.setEditable(true);
							ErrorMessage.setText(errormessage);
							MouseEntry.setText("");
						}
							
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		AddMouse.setFont(new Font("Tahoma", Font.BOLD, 14));
		AddMouse.setBounds(702, 213, 122, 34);
		contentPane.add(AddMouse);
		
		String text = KeyboardEntry.getText();
		
		JButton AddKeyboard = new JButton("Add Keyboard");
		AddKeyboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (KeyboardEntry.getText().isEmpty()) {
					display.removeAllItems();
					display.addItem("Please Enter the Product Details");
				} else {
					try {
						display.removeAllItems();
						if (AddKeyboard(KeyboardEntry.getText()) == "Item Added") {
							ErrorMessage.setEditable(true);
							ErrorMessage.setText("Item Added");
							AddKeyboard(KeyboardEntry.getText());
							KeyboardEntry.setText("");
						} else {
							ErrorMessage.setEditable(true);
							ErrorMessage.setText(errormessage);
							KeyboardEntry.setText("");
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		AddKeyboard.setFont(new Font("Tahoma", Font.BOLD, 14));
		AddKeyboard.setBounds(702, 132, 144, 34);
		contentPane.add(AddKeyboard);
		
		JButton View = new JButton("View Products");
		View.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					display.removeAllItems();
					String[] items = null;
					items = display();
					
					//Add the items from the display array as entries into a comboBox to select from
					
					display.setModel(new DefaultComboBoxModel(items));
						
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		View.setFont(new Font("Tahoma", Font.BOLD, 14));
		View.setBounds(772, 574, 181, 34);
		contentPane.add(View);
		
		JButton Back = new JButton("Back");
		Back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					LoginFrame lframe = new LoginFrame();
					lframe.setVisible(true);
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		Back.setFont(new Font("Tahoma", Font.BOLD, 14));
		Back.setBounds(21, 574, 122, 34);
		contentPane.add(Back);
		
		ErrorMessage = new JTextField();
		ErrorMessage.setBorder(null);
		ErrorMessage.setBackground(Color.CYAN);
		ErrorMessage.setBounds(10, 253, 578, 34);
		ErrorMessage.setFont(new Font("Arial", Font.BOLD, 20));
		ErrorMessage.setForeground(Color.red);
		contentPane.add(ErrorMessage);
		ErrorMessage.setColumns(10);
	}
	
}
