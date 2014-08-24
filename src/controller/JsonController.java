package controller;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.jfinal.core.Controller;

/**
 * Please note that, this controller just an improvement of basic Controller.java
 * @author wenjun_yang
 *
 */
public class JsonController extends Controller{

	/**
	 * 针对原来的getParaMap()的方法的包装。使其更加通用
	 * @return
	 */
	public Map<String, Object> getNormalPara() {
		Map<String, String[]> map = getParaMap();
		Map<String, Object> attr = new HashMap<String, Object>();
		for(String key: map.keySet()) {
			String[] tmp = map.get(key);
			attr.put(key, tmp);
		}
		return attr;
	}
	
	public Map<String, String> getBasicMap() {
		Map<String, String[]> map = getParaMap();
		Map<String, String> attr = new HashMap<String, String>();
		for(String key: map.keySet()) {
			String[] tmpArr = map.get(key);
			String tmpStr = "";
			for(String tmp : tmpArr) {
				tmpStr += tmp;
			}
			attr.put(key, tmpStr);
		}
		return attr;
	}
	
	public void renderGson(Object json) {
		renderFreeMarker("");
		renderHtml(new Gson().toJson(json));
	}
	
	
}
