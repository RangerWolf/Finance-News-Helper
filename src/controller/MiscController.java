package controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;

import validator.AdminActionValidator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.jfinal.aop.Before;

public class MiscController extends JsonController{

	@Before(AdminActionValidator.class)
	public void clear() {
		renderText("OK");
	}
	
	public void para() {
		renderGson(getPara("11"));
	}
	
	public void path() {
		renderText("Base=" + getAttr("base").toString());
	}
	
	public void hostIP() {
		String ip = "unknown";
		try {
			ip = Jsoup.connect("http://apputils.sinaapp.com/ip").get().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderText(ip);
	}
	
	public void hostname() {
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();//获得本机IP
			String address=addr.getHostName().toString();//获得本机名称
			Map<String, String> map = Maps.newHashMap();
			map.put("IP", ip);
			map.put("HostName", address);
			renderGson(map);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		
	}

	public void emailTemplate() {
//		renderGson(new File("email_notify_template.html").exists());
		File file = new File(getClass().getClassLoader().getResource("email_notify_template.html").getPath());
		System.out.println(file.getAbsolutePath());
		renderGson(file.exists());
	}
	
	public void arr() {
		String[] arr = {
				"888", "Alaska", "Arizona", "Arkansas","California", "Colorado", "Connecticut", "Delaware", "Florida","Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa","Kansas", "Kentucky", "Louisiana", "Maine", "Maryland","Massachusetts", "Michigan", "Minnesota", "Mississippi","Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire","New Jersey", "New Mexico", "New York", "North Carolina","North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania","Rhode Island", "South Carolina", "South Dakota", "Tennessee","Texas", "Utah", "Vermont", "Virginia", "Washington","West Virginia", "Wisconsin", "Wyoming" 
		};
		List<Map<String, String>> list = Lists.newArrayList();
		for(String str : arr) {
			Map<String, String> tmpMap = Maps.newHashMap();
			tmpMap.put("value", str); 
			list.add(tmpMap);
		}
		renderGson(list);
	}
}
