package Coursework;

public class Admin extends User {
	private float OriginalCost;
	
	//Defining the structure of a Admin Object
	public Admin(int Barcode,String item, String kind, String Brand, String Colour, String Connectivity, int StockQuantity, float OriginalCost, float RetailPrice, String productType) {
		super(Barcode, item, kind, Brand, Colour, Connectivity, StockQuantity, RetailPrice, productType);
		this.OriginalCost = OriginalCost;
	}
	public float retOriginal() {
		return this.OriginalCost;
	}

	
}
