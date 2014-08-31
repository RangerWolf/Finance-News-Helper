package crawler.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import main.Constants;
import model.News;

import org.horrabin.horrorss.RssFeed;
import org.horrabin.horrorss.RssItemBean;
import org.horrabin.horrorss.RssParser;

import utils.MiscUtils;
import utils.USDateParser;

import com.google.common.collect.Lists;

import crawler.Crawler;
import dao.KeywordDAO;
import dao.NewsDAO;
import dao.mysql.MySQLKeywordDAO;
import dao.mysql.MySQLNewsDAO;

public class BaiduNewsCrawler implements Crawler{

//	private static NewsDAO newsDao = new MongoNewsDAO();
//	private static KeywordDAO wordDao = new MongoKeywordDAO();
	private NewsDAO newsDao = new MySQLNewsDAO();
	private KeywordDAO wordDao = new MySQLKeywordDAO();
	
	
	@Override
	public List<String> getKeywords() {
		return MiscUtils.mergeKeywordList(wordDao.query());
//		return wordDao.query();
	}

	@Override
	public String buildURL(String keyword) {
		// Baidu新闻采用RSS爬虫的形式
		String baseURL = "http://news.baidu.com/ns?word=%s&tn=newsrss&sr=0&cl=2&rn=100&ct=0";
		String queryURL = null;
		try {
			queryURL = String.format(baseURL, URLEncoder.encode(keyword, "GB2312"));
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
		rss.setCharset("gb2312");
		rss.setDateParser(new USDateParser());
		try{
			RssFeed feed = null;
			feed = rss.load(str);

	        // Gets and iterate the items of the feed 
	        List<RssItemBean> items = feed.getItems();
	        for(RssItemBean item : items) {
	        	News news = new News();
	        	news.setTitle(item.getTitle());
	        	news.setDescription(item.getDescription());
	        	news.setPubFrom(item.getAuthor());
	        	news.setNewsUrl(item.getLink());
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

	public static void main(String[] args) throws UnknownHostException {
		
	}
	
}
