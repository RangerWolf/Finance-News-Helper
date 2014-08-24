package crawler.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.horrabin.horrorss.RssFeed;
import org.horrabin.horrorss.RssItemBean;
import org.horrabin.horrorss.RssParser;
import org.jsoup.Jsoup;

import utils.Constants;
import utils.MiscUtils;
import utils.USDateParser;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import model.News;
import crawler.Crawler;
import dao.KeywordDAO;
import dao.NewsDAO;
import dao.impl.MongoKeywordDAO;
import dao.impl.MongoNewsDAO;

public class GoogNewsCrawler implements Crawler {

	private static NewsDAO newsDao = new MongoNewsDAO();
	private static KeywordDAO wordDao = new MongoKeywordDAO();
	
	@Override
	public List<String> getKeywords() {
		return MiscUtils.mergeKeywordList(wordDao.query());
//		return wordDao.query();
	}

	@Override
	public String buildURL(String keyword) {
		// sample: 乐视 -> %E4%B9%90%E8%A7%86
//		String baseURL = "http://news.google.com.hk/news/feeds?pz=1&cf=all&ned=cn&hl=zh-CN&q=%s&output=rss&scoring=n&num=100";
		String baseURL = "http://www.5ikandy.com/goog.php?wd=(%s)";
		String queryURL = null;
		try {
			queryURL = String.format(baseURL, URLEncoder.encode(keyword, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return queryURL;
	}

	@Override
	public String fetch(String newsURL) {
		// do nothing
		return null;
	}

	@Override
	public List<News> parse(String str) {
		Date now = new Date();
		List<News> list = Lists.newArrayList();
		RssParser rss = new RssParser();
		rss.setCharset("utf-8");
		rss.setDateParser(new USDateParser());
//		rss.setProxy("63.223.64.119", 3128);
		try{
			RssFeed feed = null;
			feed = rss.load(str);
	        // Gets and iterate the items of the feed 
	        List<RssItemBean> items = feed.getItems();
	        for(RssItemBean item : items) {
	        	News news = new News();
	        	
	        	// Title - From
	        	String title = item.getTitle();
	        	int sepIdx = title.indexOf("-");
	        	if(sepIdx == -1)
	        		news.setTitle(title);
	        	else {
	        		news.setTitle(title.substring(0,sepIdx).trim());
	        		news.setFrom(title.substring(sepIdx + 1).trim());
	        	}
	        	
	        	// link
	        	String link = item.getLink();
	        	sepIdx = link.indexOf("url=");
	        	if(sepIdx == -1) {
	        		news.setNewsUrl(link);
	        	} else {
	        		news.setNewsUrl(link.substring(sepIdx + "url=".length()).trim());
	        	}
	        	
	        	// desc
	        	String desc = item.getDescription();
	        	desc = Jsoup.parse(desc).text();
	        	desc = StringUtils.replaceOnce(desc, title, "");
	        	if(news.getFrom().length() > 0)
	        		desc = StringUtils.replaceOnce(desc, news.getFrom(), "");
	        	sepIdx = desc.indexOf("...");
	        	if(sepIdx != -1) {
	        		desc = desc.substring(0, sepIdx).trim();
	        	}
	        	news.setDescription(desc);
	        	
	        	// publish time
	        	news.setDateTime(item.getPubDate().getTime());
	        	long timeDiff = now.getTime() - item.getPubDate().getTime();
	        	if(timeDiff <= Constants.MSEC_PER_DAY) 
	        		list.add(news);
	        	
	        }
		}catch(Exception e){
			e.printStackTrace();
		} 
		return list;
	}

	@Override
	public Boolean save(List<News> list) {
		if(list.size() > 0 && newsDao.save(list) >= 0) {
			return true;
		} else
			return false;
	}

}
