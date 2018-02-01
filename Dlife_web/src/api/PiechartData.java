package api;

public class PiechartData {

	String category;
	long categoryTime;

	public PiechartData(String category, long categoryTime) {
		super();
		this.category = category;
		this.categoryTime = categoryTime;
	}

	public PiechartData() {
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getCategoryTime() {
		return categoryTime;
	}

	public void setCategoryTime(long categoryTime) {
		this.categoryTime = categoryTime;
	}

}