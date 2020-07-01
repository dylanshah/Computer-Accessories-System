package Coursework;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.List;
import java.awt.SystemColor;

import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField;

public class UserFrame extends JFrame {

	private JPanel contentPane;
	private JTextField Title;
	private JTextField InputBrand;
	private JTextField InputKeyboard;
	private JButton FilterBrand;
	private JButton FilterKeyboard;
	private JButton FilterByBoth;
	private JButton Checkout;
	private JButton View;
	private JButton Back;
	protected JComboBox display;
	private JLabel Orderlbl;
	

	//Method to filter the Brand requested by the User
	public String SearchBrand(String Brand) throws IOException {
		File file = new File("StockData.txt");
		String line = "";
    	Scanner filescanner = new Scanner(file).useDelimiter( "\\Z");
        while (filescanner.hasNext()) {
        	final String lineFromFile = filescanner.nextLine();
	    	if (lineFromFile.contains(Brand)) {
                line += (lineFromFile.toString() + " \n");
                continue;
            }
        }
        return line;
	}
	
	// Method to filter the Layout type requested by the user
	
	public String SearchLayout(String Layout) throws IOException {
		File file = new File("StockData.txt");
		String line = "";
	    Scanner filescanner = new Scanner(file).useDelimiter( "\\Z");
	    
	    while (filescanner.hasNext()) {
	    	final String lineFromFile = filescanner.nextLine();
	    	if (lineFromFile.contains(Layout)) {
	    		
	    		//collects all the lines found
	    		
                line += (lineFromFile.toString() + " \n");
            }
	    	
		}
	    return line;
    }
	
	//Method to filter the product List by both Brand and Layout Type requested by the user
	
	public String SearchBoth(String Brand, String Layout) throws IOException {
		File file = new File("StockData.txt");
		String line = "";
	    Scanner filescanner;
	    filescanner = new Scanner(file).useDelimiter( "\\Z");
	    
	    while (filescanner.hasNext()) {
	    	final String lineFromFile = filescanner.nextLine();
	    	if (lineFromFile.contains(Brand) && lineFromFile.contains(Layout)) {
	    		
	    		// Collects all the matching lines
	    		
                line += (lineFromFile.toString() + " \n");
            }
	    }
		return line;
	}
	
	//Method to select all the products and make available to display
	
	public String view() throws IOException {
		File file = new File("StockData.txt");
		Scanner sc = new Scanner(file);
		sc.useDelimiter("\\Z");
		return sc.next();
	}
	
	//Method to write the selected product to a separate file for reference when writing to ActivityLogs file
	
	public void write(String text) throws IOException {
		FileWriter tempfile1 = new FileWriter("Active.txt", false);
		FileWriter tempfile = new FileWriter("Active.txt", true);
	    tempfile.write(text);
	    tempfile.close();
	}
	
	// Method to sort the data according to descending quantity of stock
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
         
            order.add(new Admin(code, item, type, brand, colour, connectivity, stock, original, retail, additional)); //Adds the admin object to the array list so it can be ordered
             
            currentLine = reader.readLine();
        }
        
        Collections.sort(order, new stockCompare());
        BufferedWriter writer = new BufferedWriter(new FileWriter("StockData.txt"));
        for (Admin admin : order) 
        {
        	if (order.get(order.size() - 1) == admin) {	//Adds a new line ot the end if it not the last line to be added
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
	//Method to display the returned results into an array used to display into a comboBox
	
	public String[] display() throws IOException {
		sort();
	    File tempFile = new File("Active.txt");
		Scanner scan = new Scanner(tempFile);
		FileInputStream fis = new FileInputStream(tempFile);
	    byte[] byteArray = new byte[(int)tempFile.length()];
	    fis.read(byteArray);
	    String data = new String(byteArray);
	    String[] stringArray = data.split("\n");
		int arrayIndex = 0;
		String[] itemArray = new String[stringArray.length];
		Customer acc = null;
		while (scan.hasNextLine()) {
			String[] entry =scan.nextLine().split(",");
			
			//Creates a Customer Object for each entry returned
			
			acc = new Customer(Integer.parseInt(entry[0].trim()), entry[1].trim(), entry[2].trim(), entry[3].trim(), entry[4].trim(), entry[5].trim(), Integer.parseInt(entry[6].trim()), Float.parseFloat(entry[8].trim()), entry[9].trim());
			itemArray[arrayIndex] = String.valueOf(acc.retCode())+", "+acc.retitem()+", "+acc.retkind()+", "+acc.retbrand()+", "+acc.retcolour()+", "+acc.retconnect()+", "+String.valueOf(acc.retStock())+", "+String.valueOf(acc.retprice())+", "+acc.rettype();
			arrayIndex++;
			
		}
		scan.close();
		return itemArray;
		
	}
	
	//Method to write to the ActivityLogs File the details of the item selected
	
	public void writeSelection(String selection) throws IOException {
		File tempFile = new File("Id.txt");
		Scanner scan = new Scanner(tempFile);
		String id = scan.nextLine().toString();
		LoginFrame lgframe = new LoginFrame();
		lgframe.Id(id);
		File tempFile1 = new File("Active.txt");
		Scanner scan1 = new Scanner(tempFile1);
		String[] line = selection.split(",");
		String code = line[0];
		User acc = null;
		while (scan1.hasNextLine()) {
			String[] entry =scan1.nextLine().split(",");
			acc = new User(Integer.parseInt(entry[0].trim()), entry[1].trim(), entry[2].trim(), entry[3].trim(), entry[4].trim(), entry[5].trim(), Integer.parseInt(entry[6].trim()), Float.parseFloat(entry[7].trim()), entry[8].trim());
			String bar = String.valueOf(acc.retCode());
			
			//Compares the barcode of the item selected with all entries in the Product List and uses that to obtain the Price to add to ActivityLogs
			
			if (bar.contains(code)) {
				FileWriter tempfile = new FileWriter("ActivityLogs.txt", true);
			    tempfile.write(line[0]+", "+line[7]+", ");
			    tempfile.close();
			    break;
			}
		}
		
		
	}
	/**
	 * Create the frame.
	 */
	public UserFrame() {
		
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
		Title.setFont(new Font("Arial", Font.BOLD, 36));
		Title.setEditable(false);
		Title.setColumns(10);
		Title.setBorder(null);
		Title.setBackground(Color.CYAN);
		Title.setBounds(12, 11, 743, 59);
		contentPane.add(Title);
		
		InputBrand = new JTextField();
		InputBrand.setFont(new Font("Tahoma", Font.PLAIN, 14));
		InputBrand.setColumns(10);
		InputBrand.setBackground(SystemColor.controlHighlight);
		InputBrand.setBounds(10, 104, 176, 28);
		contentPane.add(InputBrand);
		
		InputKeyboard = new JTextField();
		InputKeyboard.setFont(new Font("Tahoma", Font.PLAIN, 14));
		InputKeyboard.setColumns(10);
		InputKeyboard.setBackground(SystemColor.controlHighlight);
		InputKeyboard.setBounds(12, 144, 174, 28);
		contentPane.add(InputKeyboard);
		
		JComboBox display = new JComboBox();
		display.setFont(new Font("Tahoma", Font.BOLD, 13));
		display.setBounds(10, 249, 743, 28);
		contentPane.add(display);
		
		JButton FilterKeyboard = new JButton("Filter by Keyboard Type\r\n");
		FilterKeyboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Only filters by Layout if there is a requested Layout type by the User
				
				if (InputKeyboard.getText().isEmpty()) {
					display.removeAllItems();
		    		display.addItem("Please Enter a Layout Type");
		    	} else {	    
		    		try {
						display.removeAllItems();
						write(SearchLayout(InputKeyboard.getText()));
						String[] item = null;
						item = display();
						display.setModel(new DefaultComboBoxModel(item)); //Adds each item to the comboBox
						InputKeyboard.setText(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			    }
			}
		});
		FilterKeyboard.setHorizontalAlignment(SwingConstants.LEFT);
		FilterKeyboard.setFont(new Font("Arial", Font.BOLD, 14));
		FilterKeyboard.setBackground(Color.WHITE);
		FilterKeyboard.setBounds(196, 144, 212, 28);
		contentPane.add(FilterKeyboard);
		
		JButton FilterBrand = new JButton("Filter by Brand");
		FilterBrand.setFont(new Font("Arial", Font.BOLD, 16));
		FilterBrand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//Only filters by Brand if there is a requested Brand by the User
				
				if (InputBrand.getText().isEmpty()) {
					display.removeAllItems();
					display.addItem("Please Enter a Brand");
				} else {
					try {
						display.removeAllItems();
						write(SearchBrand(InputBrand.getText()).toString());
						String[] item = null;
						item = display();
						display.setModel(new DefaultComboBoxModel(item));
						InputBrand.setText(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		FilterBrand.setBackground(Color.WHITE);
		FilterBrand.setBounds(196, 104, 161, 28);
		contentPane.add(FilterBrand);
		
		JButton FilterByBoth = new JButton("Filter by Both");
		FilterByBoth.setFont(new Font("Arial", Font.BOLD, 16));
		FilterByBoth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//Allows the User to filter by both methods if they input the items correctly, else displays an error message
				
				if (InputKeyboard.getText().isEmpty() && InputBrand.getText().isEmpty()) {
					display.removeAllItems();
					display.addItem("Please Enter a Brand and a Keyboard Layout");
				} else if (InputBrand.getText().isEmpty() || InputKeyboard.getText().isEmpty()) {
					display.removeAllItems();
					display.addItem("1 or more Required Fields Missing");
				} else {
					try {
						
						// Adds the items returned to the comboBox for displaying

						display.removeAllItems();
						write(SearchBoth(InputBrand.getText(), InputKeyboard.getText()));
						String[] item = null;
						item = display();
						display.setModel(new DefaultComboBoxModel(item));
						InputBrand.setText(null);
						InputKeyboard.setText(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		FilterByBoth.setBounds(12, 179, 174, 28);
		contentPane.add(FilterByBoth);
		
		JLabel Orderlbl = new JLabel("Barcode,  Name,  Type,  Brand,  Colour,  Connectivity,  Stock,  Price,  Additional Info");
		Orderlbl.setFont(new Font("Arial", Font.BOLD, 16));
		Orderlbl.setBounds(10, 223, 658, 20);
		contentPane.add(Orderlbl);
		
		JButton Checkout = new JButton("Checkout");
		Checkout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Only opens the Basket Window if an item is added to Basket
				
				if (display.getSelectedIndex() != -1) {
					try {
						BasketFrame bframe = new BasketFrame();
						bframe.setVisible(true);
						write(display.getSelectedItem().toString());
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					display.addItem("Please Select an item to proceed!");
				}
			}
		});
		Checkout.setFont(new Font("Arial", Font.BOLD, 16));
		Checkout.setBounds(631, 474, 122, 34);
		contentPane.add(Checkout);
		
		JButton View = new JButton("View");
		View.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//Adds all the products to the comboBox as entries to select from
				
				try {
					display.removeAllItems();
					write(view());
					String[] item = null;
					item = display();
					display.setModel(new DefaultComboBoxModel(item));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		View.setFont(new Font("Arial", Font.BOLD, 16));
		View.setBounds(10, 474, 122, 34);
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
		Back.setFont(new Font("Arial", Font.BOLD, 16));
		Back.setBounds(631, 99, 122, 34);
		contentPane.add(Back);
	}
}
