package model;


public class News {

	String title;
	String description;
	String newsUrl;
	Long dateTime;
	String pubFrom;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNewsUrl() {
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	public Long getDateTime() {
		return dateTime;
	}
	public void setDateTime(Long dateTime) {
		this.dateTime = dateTime;
	}
	public String getPubFrom() {
		return pubFrom;
	}
	public void setPubFrom(String pubFrom) {
		this.pubFrom = pubFrom;
	}
	
	
}
