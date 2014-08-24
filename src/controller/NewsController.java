package controller;

import java.util.List;
import java.util.Map;

import model.News;

import org.apache.commons.lang.StringUtils;

import validator.AdminActionValidator;

import com.google.common.collect.Maps;
import com.jfinal.aop.Before;

import crawler.Crawler;
import crawler.impl.BaiduNewsCrawler;
import crawler.impl.GoogNewsCrawler;
import dao.NewsDAO;
import dao.impl.MongoNewsDAO;

public class NewsController extends JsonController{
	private static NewsDAO dao = new MongoNewsDAO();
	public void index() {
		renderGson(dao.query());
	}
	
	@Before(AdminActionValidator.class)
	public void clear() {
		renderGson(dao.clear());
	}
	
	public void show() {
		renderGson(dao.query().size());
	}
	
	
	public void query() {
		String keyword = getPara("keyword");
		if(StringUtils.isEmpty(keyword)) {
			renderGson("Error");
		} else {
			String[] words = keyword.split(",");
			Map<String, List<News>> map = Maps.newHashMap();
			for(String word: words) {
				map.put(word, dao.query(word));
			}
			renderGson(map);
		}
	}
	
	public void baidu() {
		Crawler bdCrawler = new BaiduNewsCrawler();
		List<String> keywordsList = bdCrawler.getKeywords();
		Map<String, Boolean> map = Maps.newHashMap();
		for(String word : keywordsList) {
			String url = bdCrawler.buildURL(word);
			List<News> list = bdCrawler.parse(url);
			if(list.size() > 0) {
				map.put(word, bdCrawler.save(list));
			} else {
				map.put(word, false);
			}
		}
		renderGson(map);
	}
	
	public void google() {
		try {
			Crawler bdCrawler = new GoogNewsCrawler();
			List<String> keywordsList = bdCrawler.getKeywords();
			Map<String, Boolean> map = Maps.newHashMap();
			for(String word : keywordsList) {
				String url = bdCrawler.buildURL(word);
				List<News> list = bdCrawler.parse(url);
				if(list.size() > 0) {
					map.put(word, bdCrawler.save(list));
				} else {
					map.put(word, false);
				}
			}
			renderGson(map);
		} catch(Exception e) {
			e.printStackTrace();
			renderText(e.getMessage());
		}
		
	}
}
