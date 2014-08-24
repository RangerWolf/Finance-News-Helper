package model;

public class MailAccount {

	String mailAddr;		// ex: yang.rangerwolf@gmail.com
	String username;	
	String password;
	Integer usedTimes;
	Boolean isActive;
	
	public String getMailAddr() {
		return mailAddr;
	}
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getUsedTimes() {
		return usedTimes;
	}
	public void setUsedTimes(Integer usedTimes) {
		this.usedTimes = usedTimes;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
	
}
