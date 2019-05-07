/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class console
public class Console extends GamingDevice {
	
	//instance variables
	private ConsoleType console_type;

	//constructor
	public Console(int ID, String modelName, int modelYear, String manufacturer,
			double price, ConsoleType console_type,String processor,String graphics,
			String sound,int disk_capacity,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,processor,graphics,sound,disk_capacity,quantity);
		setConsole_type(console_type);
	}
	
	//setter and getter
	public ConsoleType getConsole_type() {
		return console_type;
	}

	public void setConsole_type(ConsoleType console_type) {
		this.console_type = console_type;
	}
	
	//toString method, represents a non-portable Console
	public String toString(){
		return "Console Type: " + console_type + "\n" + super.toString();
	}
}
