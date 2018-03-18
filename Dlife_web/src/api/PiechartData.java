package api;

public class PiechartData {

	private String category;
	private double categoryTime;

	public PiechartData() {
		super();
	}
	
	public PiechartData(String category, double categoryTime) {
		super();
		this.category = category;
		this.categoryTime = categoryTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getCategoryTime() {
		return categoryTime;
	}

	public void setCategoryTime(double categoryTime) {
		this.categoryTime = categoryTime;
	}

}