/**
 * @author Anastasiou Iwannhs(3150232), Savvidis Kwnstantinos(3150229), Stavropoulos Petros(3150230)
 *
 */

//class MoviePlayer
public class MoviePlayer extends VideoSoundDevice {
	//instance variables
	private MoviePlayerType media_type;
	private String resolution;
	private FormatType format;
	
	//constructor
	public MoviePlayer(int ID, String modelName, int modelYear, String manufacturer,
			double price, MoviePlayerType media_type,String resolution,FormatType format,int quantity) {
		super(ID, modelName, modelYear, manufacturer, price,quantity);
		setMedia_type(media_type);
		setResolution(resolution);
		setFormat(format);
	}

	//setters and getters
	public MoviePlayerType getMedia_type() {
		return media_type;
	}

	public void setMedia_type(MoviePlayerType media_type) {
		this.media_type = media_type;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public FormatType getFormat() {
		return format;
	}

	public void setFormat(FormatType format) {
		this.format = format;
	}
	
	//toString method, represents a movie player
	public String toString(){
		return("Movie Player Type: " + media_type + "\nResolution: " + resolution + "\nPlay Format: " + format + "\n" + super.toString());
	}
	
}
