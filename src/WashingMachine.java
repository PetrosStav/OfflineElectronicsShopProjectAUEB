/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class WashingMachine
public class WashingMachine extends HomeDevice {
	
	//instance variables
	private double capacity;
	private int rpm;
	
	//constructor
	public WashingMachine(int ID, String modelName, int modelYear,
			String manufacturer, double price, String energy_class,
			double capacity,int rpm,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price, energy_class,quantity);
		setCapacity(capacity);
		setRpm(rpm);
	}

	//setters and getters
	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public int getRpm() {
		return rpm;
	}

	public void setRpm(int rpm) {
		this.rpm = rpm;
	}
	
	//toString method, represents a washing machine
	public String toString(){
		return("Capacity: " + capacity + " Kg\nRPMs: " + rpm + "\n" + super.toString());
	}
	
}
