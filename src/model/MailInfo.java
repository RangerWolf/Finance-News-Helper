package model;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class MailInfo {

	Address[] to;
	Address[] cc;
	Address[] bcc;
	
	String body;
	String subject;
	
	
	public Address[] getCc() {
		return cc;
	}
	public void setCc(String ccList) {
		try {
			if(ccList == null) {
				ccList = "";
			}
			this.cc = InternetAddress.parse(ccList);
		} catch (AddressException e) {
			e.printStackTrace();
		}
	}
	public Address[] getBcc() {
		return bcc;
	}
	public void setBcc(String bccList) {
		try {
			if(bccList == null) {
				bccList = "";
			}
			this.bcc = InternetAddress.parse(bccList);
		} catch (AddressException e) {
			e.printStackTrace();
		}
	}
	public Address[] getTo() {
		return to;
	}
	public void setTo(String addressList) {
		try {
			if(addressList == null) {
				addressList = "";
			}
			this.to = InternetAddress.parse(addressList.replace("[", "").replace("]", ""));
		} catch (AddressException e) {
			e.printStackTrace();
		}
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
	
}
