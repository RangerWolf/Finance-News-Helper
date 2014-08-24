package controller;

import org.apache.commons.lang.StringUtils;

import utils.Constants;
import utils.MiscUtils;
import validator.LoginStatusValidator;
import validator.StockAPIValidator;

import com.jfinal.aop.Before;

@Before(LoginStatusValidator.class)
public class StockController extends JsonController {

	public void search() {
		String urlTemplate = "http://quotes.money.163.com/stocksearch/json.do?type=&count=20&word=%s";
		String q = getPara("q");
		String content = MiscUtils.simpleJsoupGet(urlTemplate, q, "UTF8");
		renderText(truncateMoney163CallbackString(content));
	}
	
	public void query() {
		String urlTemplate = "http://api.money.126.net/data/feed/%s,money.api";
		String stockStr = getPara(Constants.ATTR_USER_STOCK);
		String content = MiscUtils.simpleJsoupGet(urlTemplate, stockStr, "UTF8");
		System.out.println("QUERY=" + truncateMoney163CallbackString(content));
		renderText(truncateMoney163CallbackString(content));
	}
	
	private String truncateMoney163CallbackString(String content) {
		// 返回的Sample: _ntes_stocksearch_callback([])  // 已经将其中的数据删除，仅保留wrapper
		int startIdx = content.indexOf("(");
		if(startIdx == -1) startIdx = 0;
		int endIdx = content.lastIndexOf(")");
		if(endIdx == -1) endIdx = content.length() - 1;
		return StringUtils.substring(content, startIdx+1, endIdx);
	}
}
