/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class camera
public class Camera extends VideoSoundDevice {
	//instance variables
	private CameraType camera_type;
	private int megapixels;
	private int opt_zoom,dig_zoom;
	private double screen_dimension;
	
	//constructor
	public Camera(int ID, String modelName, int modelYear, String manufacturer,
			double price, CameraType camera_type,int megapixels,int opt_zoom,
			int dig_zoom,double screen_dimension,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,quantity);
		setCamera_type(camera_type);
		setMegapixels(megapixels);
		setOpt_zoom(opt_zoom);
		setDig_zoom(dig_zoom);
		setScreen_dimension(screen_dimension);
	}
	
	//setters and getters
	public CameraType getCamera_type() {
		return camera_type;
	}

	public void setCamera_type(CameraType camera_type) {
		this.camera_type = camera_type;
	}

	public int getMegapixels() {
		return megapixels;
	}

	public void setMegapixels(int megapixels) {
		this.megapixels = megapixels;
	}

	public int getOpt_zoom() {
		return opt_zoom;
	}

	public void setOpt_zoom(int opt_zoom) {
		this.opt_zoom = opt_zoom;
	}

	public int getDig_zoom() {
		return dig_zoom;
	}

	public void setDig_zoom(int dig_zoom) {
		this.dig_zoom = dig_zoom;
	}

	public double getScreen_dimension() {
		return screen_dimension;
	}

	public void setScreen_dimension(double screen_dimension) {
		this.screen_dimension = screen_dimension;
	}
	
	//toString method,represents a camera
	public String toString(){
		return("Camera Type: "+ camera_type + "\nMegapixels: " + megapixels + "\nOptical Zoom: " + opt_zoom + "x\nDigital Zoom: " + dig_zoom + "x\nScreen Dimension: " + screen_dimension + "\"\n" + super.toString());
	}
	
}
