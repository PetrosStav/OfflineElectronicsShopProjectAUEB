/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class Device as abstract
public abstract class Device {
	//instance variables
	protected int ID;
	protected String modelName;
	protected int modelYear;
	protected String manufacturer;
	protected double price;
	protected int quantity;
	protected String imagePath;

	//constructor
	public Device(int ID,String modelName,int modelYear,String manufacturer,double price,int quantity){
		setID(ID);
		setModelName(modelName);
		setModelYear(modelYear);
		setManufacturer(manufacturer);
		setPrice(price);
		setQuantity(quantity);
		setImagePath("../Images/" + getID() + ".jpg");
	}
	
	//setters and getters
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	//toString method,represents a device
	public String toString(){
		return("ID: " + ID + "\nModelName: " + modelName + "\nModelYear: " + modelYear + "\nManufacturer: " + manufacturer + "\nPrice: " + price +" E");  //+ "\nDevices Available: " + getQuantity());
	}
}
