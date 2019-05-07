/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class PortableConsole
public class PortableConsole extends GamingDevice {
	
	//instance variable
	private PortableConsoleType portable_type;
	
	//constructor
	public PortableConsole(int ID, String modelName, int modelYear, String manufacturer,
			double price, PortableConsoleType portable_type,String processor,String graphics,
			String sound,int disk_capacity,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,processor,graphics,sound,disk_capacity,quantity);
		setPortable_type(portable_type);
	}
	
	//setter and getter
	public PortableConsoleType getPortable_type() {
		return portable_type;
	}

	public void setPortable_type(PortableConsoleType portable_type) {
		this.portable_type = portable_type;
	}

	//toString method, represents a portable console
	public String toString(){
		return "Portable Console Type: " + portable_type + "\n" + super.toString();
	}
	
}
