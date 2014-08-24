package model;

import java.util.List;

public class NotifyHistory {

	/**
	 * 用户的id, 可能是邮件地址，也可能是email
	 */
	String id;
	
	/**
	 * 上次通知用户的时间，每次发送新的通知的时候更改此项
	 */
	Long lastNotifyTime;
	
	/**
	 * 之前发送的通知的新闻标题
	 */
	List<String> titleList;

	/**
	 * 上次通知的结果，成功 true 失败 false 
	 */
	Boolean lastNotifyResult;
	
	
	
	public Boolean getLastNotifyResult() {
		return lastNotifyResult;
	}

	public void setLastNotifyResult(Boolean lastNotifyResult) {
		this.lastNotifyResult = lastNotifyResult;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Long getLastNotifyTime() {
		return lastNotifyTime;
	}

	public void setLastNotifyTime(Long lastNotifyTime) {
		this.lastNotifyTime = lastNotifyTime;
	}

	public List<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}
	
	
}
