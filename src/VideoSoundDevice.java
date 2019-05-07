/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//category VideoSoundDevice
public abstract class VideoSoundDevice extends Device {	
	//constructor
	public VideoSoundDevice(int ID, String modelName, int modelYear, String manufacturer, double price,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,quantity);
	}

	//toString method, represents a VideoSound device
	public String toString(){
		return(super.toString());
	}
}
