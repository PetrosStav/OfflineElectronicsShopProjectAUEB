/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//category HomeDevice
public abstract class HomeDevice extends Device {
	//instance variable
	protected String energy_class;
	
	//constructor
	public HomeDevice(int ID, String modelName, int modelYear, String manufacturer,
			double price, String energy_class,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,quantity);
		setEnergy_class(energy_class);
	}

	//setter and getter
	public String getEnergy_class() {
		return energy_class;
	}

	public void setEnergy_class(String energy_class) {
		this.energy_class = energy_class;
	}
	
	//toString method, represents a Home device
	public String toString(){
		return("Energy Class: " + energy_class + "\n" + super.toString());
	}
}
