/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//category GamingDevice
public abstract class GamingDevice extends Device {
	//instance variables
	protected String processor;
	protected String graphics;
	protected String sound;
	protected int disk_capacity;
	
	//constructor
	public GamingDevice(int ID, String modelName, int modelYear, String manufacturer, double price,String processor,String graphics, String sound,int disk_capacity,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,quantity);
		setProcessor(processor);
		setGraphics(graphics);
		setSound(sound);
		setDisk_capacity(disk_capacity);
	}
	
	//setters and getters
	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getGraphics() {
		return graphics;
	}

	public void setGraphics(String graphics) {
		this.graphics = graphics;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public int getDisk_capacity() {
		return disk_capacity;
	}

	public void setDisk_capacity(int disk_capacity) {
		this.disk_capacity = disk_capacity;
	}
	
	//toString method,represents a Gaming device
	public String toString(){
		return("Processor: " + processor + "\nGraphics: " + graphics + "\nSound: " + sound + "\nDisk Capacity: " + disk_capacity + " GB\n" + super.toString());
	}
}
