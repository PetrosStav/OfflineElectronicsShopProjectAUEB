/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class for sales
public class Sale {
	
	//instance variables
	private static int numOfSales = 0;		//this variable will be assigned as unique id of an order
	private int saleID;
	private Device saleProduct;
	private String name,surname,phoneNumber;
	private String saleDate;
	private double finalPrice;
	
	//constructor
	public Sale(Device saleProduct,String name,String surname,
			String phoneNumber,String saleDate) {
		numOfSales++;
		setSaleID(numOfSales);
		setSaleProduct(saleProduct);
		setName(name);
		setSurname(surname);
		setPhoneNumber(phoneNumber);
		setSaleDate(saleDate);
		setFinalPrice();
	}
	public Sale(int ID,Device saleProduct,String name,String surname,
			String phoneNumber,String saleDate,double fprice) {
		setSaleID(ID);
		setSaleProduct(saleProduct);
		setName(name);
		setSurname(surname);
		setPhoneNumber(phoneNumber);
		setSaleDate(saleDate);
		setFinalPrice(fprice);
	}
	
	//setters and getters
	public static int getNumOfSales() {
		return numOfSales;
	}
	
	public static void setNumofSales(int n){
		numOfSales = n;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}


	public double getFinalPrice() {
		return finalPrice;
	}
	
	//method - sets the final price to the created sale in accordance with the discount of every category
	public void setFinalPrice() {
		if(saleProduct instanceof VideoSoundDevice){
			finalPrice = saleProduct.getPrice()*0.75;
		}else if(saleProduct instanceof GamingDevice){
			finalPrice = saleProduct.getPrice()*0.9;
		}else{
			finalPrice = saleProduct.getPrice()*0.8;
		}
	}
	
	//setters and getters
	public int getSaleID() {
		return saleID;
	}

	public void setSaleID(int saleID) {
		this.saleID = saleID;
	}

	public Device getSaleProduct() {
		return saleProduct;
	}

	public void setSaleProduct(Device saleProduct) {
		this.saleProduct = saleProduct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	
	//toString method, represents a sale
	public String toString(){
		return("Sale No. "+ getSaleID() + "\nName: "+ getName() + "\nSurname: " + getSurname()
				+ "\nPhoneNumber: " + getPhoneNumber() + "\nSale Date: " + getSaleDate() + "\nFinal Price: " + getFinalPrice()
				+ "\n-----PRODUCT----- \n" + getSaleProduct());
	}

}
