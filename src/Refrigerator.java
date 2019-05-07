/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class refrigerator
public class Refrigerator extends HomeDevice {
	//instance variables
	private FridgeType refr_type;
	private double preserve_capacity;
	private double freeze_capacity;
	
	//constructor
	public Refrigerator(int ID, String modelName, int modelYear, String manufacturer,
			double price, String energy_class,FridgeType refr_type,double preserve_capacity,
			double freeze_capacity,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price, energy_class,quantity);
		setRefr_type(refr_type);
		setPreserve_capacity(preserve_capacity);
		setFreeze_capacity(freeze_capacity);
	}

	//setters and getters
	public FridgeType getRefr_type() {
		return refr_type;
	}

	public void setRefr_type(FridgeType refr_type) {
		this.refr_type = refr_type;
	}

	public double getPreserve_capacity() {
		return preserve_capacity;
	}

	public void setPreserve_capacity(double preserve_capacity) {
		this.preserve_capacity = preserve_capacity;
	}

	public double getFreeze_capacity() {
		return freeze_capacity;
	}

	public void setFreeze_capacity(double freeze_capacity) {
		this.freeze_capacity = freeze_capacity;
	}
	
	//toString method, represents a refrigerator
	public String toString(){
		return("Refrigerator Type: " + refr_type + "\nPreserve Capacity: " + preserve_capacity + " Kg\nFreeze Capacity: " + freeze_capacity + " Kg\n" + super.toString());
	}
	
}
