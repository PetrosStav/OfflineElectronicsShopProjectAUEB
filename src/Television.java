/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class television
public class Television extends VideoSoundDevice {
	
	//instance variables
	private TvType tv_type;
	private double dimension;
	private String resolution;
	private PortType ports_type;
	
	//constructor
	public Television(int ID, String modelName, int modelYear, String manufacturer,
			double price, TvType tv_type,double dimension,String resolution,
			PortType ports_type,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,quantity);
		setTv_type(tv_type);
		setDimension(dimension);
		setResolution(resolution);
		setPorts_type(ports_type);
	}

	//setters and getters
	public double getDimension() {
		return dimension;
	}

	public void setDimension(double dimension) {
		this.dimension = dimension;
	}

	public String getResolution() {
		return resolution;
	}

	public TvType getTv_type() {
		return tv_type;
	}

	public void setTv_type(TvType tv_type) {
		this.tv_type = tv_type;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public PortType getPorts_type() {
		return ports_type;
	}

	public void setPorts_type(PortType ports_type) {
		this.ports_type = ports_type;
	}
	
	//toString method, represents a television
	public String toString(){
		return("Television Type: " + tv_type + "\nDimension: " + dimension + "\"\nResolution: " + resolution + "\nPorts Type: " + ports_type + "\n" + super.toString());
	}
	
}
