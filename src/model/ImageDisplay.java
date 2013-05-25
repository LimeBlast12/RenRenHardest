package model;

public class ImageDisplay {
	
	private String url;
	private int owner;
	private int filter_type;
	
	public ImageDisplay(){
		
	}
	
	public ImageDisplay(String url, int owner, int filter_type){
		this.url = url;
		this.owner = owner;
		this.filter_type = filter_type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getFilter_type() {
		return filter_type;
	}

	public void setFilter_type(int filter_type) {
		this.filter_type = filter_type;
	}
	
	

}
