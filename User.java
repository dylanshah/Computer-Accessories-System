package Coursework;

public class User {
	private int Barcode;
	private String item;
	private String kind;
	private String Brand;
	private String Colour;
	private String Connectivity;
	private int StockQuantity;
	private float RetailPrice;
	private String ProductType;
	
	//Defining the structure of a User object
	public User(int Barcode,String item, String kind, String Brand, String Colour, String Connectivity, int StockQuantity, float RetailPrice, String productType) {
		this.Barcode = Barcode;
		this.item = item;
		this.kind = kind;
		this.Brand = Brand;
		this.Colour = Colour;
		this.Connectivity = Connectivity;
		this.StockQuantity = StockQuantity;
		this.RetailPrice = RetailPrice;
		this.ProductType = productType;
	}
	//Defining methods to get a field of the object
	public int retCode() {
		return this.Barcode;
	}
	public String retitem() {
		return this.item;
	}
	public String retkind() {
		return this.kind;
	}
	public String retbrand() {
		return this.Brand;
	}
	public String retcolour() {
		return this.Colour;
	}
	public String retconnect() {
		return this.Connectivity;
	}
	public int retStock() {
		return this.StockQuantity;
	}
	public float retprice() {
		return this.RetailPrice;
	}
	public String rettype() {
		return this.ProductType;
	}


}
