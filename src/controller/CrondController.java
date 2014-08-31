package controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Keyword;
import model.News;
import model.NotifyHistory;
import model.User;
import notifier.EmailNotifier;
import notifier.Notifier;
import validator.AdminActionValidator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jfinal.aop.Before;

import crawler.Crawler;
import crawler.impl.BaiduNewsCrawler;
import crawler.impl.GoogNewsCrawler;
import dao.KeywordDAO;
import dao.NotifyHistoryDAO;
import dao.UserDAO;
import dao.mysql.MySQLKeywordDAO;
import dao.mysql.MySQLNotifyHistoryDAO;
import dao.mysql.MySQLUserDAO;

@Before(AdminActionValidator.class)
public class CrondController extends JsonController {

	private KeywordDAO kDao = new MySQLKeywordDAO();
	private UserDAO uDao = new MySQLUserDAO();
	private NotifyHistoryDAO nhDao = new MySQLNotifyHistoryDAO();
	
	public void updateKeyword() {
		try {
			// update keyword
			kDao.clear();
			List<User> userList = uDao.queryAll();
			Set<String> wordSet = Sets.newHashSet();
			for(User user: userList ) {
				
				String keyword = user.getKeywords();
				String[] wordArr = keyword.split(",");
				for(String word : wordArr) {
					wordSet.add(word);
				}
			}
			List<Keyword> wordList = Lists.newArrayList();
			for(String word : wordSet) {
				if(word.trim().length() > 0)
					wordList.add(new Keyword(word));
			}
			kDao.save(wordList);
			renderGson(wordList);
		} catch(Exception e) {
			e.printStackTrace();
			renderText("keyword/update:" + e.getMessage());
		}
	}
	
	public void fetchNewsAndNotifyAllUsers() {
		
		// crawl news from google
		Crawler googCrawler = new GoogNewsCrawler();
		Crawler baiduCrawler = new BaiduNewsCrawler();
		
		List<String> keywordsList = googCrawler.getKeywords();
		Map<String, Object> map = Maps.newHashMap();
		for(String word : keywordsList) {
			// google
			String url = googCrawler.buildURL(word);
			List<News> list = googCrawler.parse(url);
			if(list.size() > 0) {
				map.put("Goog-" + word, googCrawler.save(list));
			} else {
				map.put("Goog-" + word, false);
			}
			
			// baidu
			url = baiduCrawler.buildURL(word);
			list = baiduCrawler.parse(url);
			if(list.size() > 0) {
				map.put("Baidu-" + word, googCrawler.save(list));
			} else {
				map.put("Baidu-" + word, false);
			}
		}
		
		
		List<User> userList = uDao.queryAll();
		for(User user : userList) {
			NotifyHistory before = nhDao.query(user.getEmail());
			int beforeSize = 0;
			if(before != null && before.getTitleList() != null) {
				beforeSize = before.getTitleList().size();
			}
			Notifier emailNotifier = new EmailNotifier(user.getEmail());
			emailNotifier.run();
			int afterSize = 0;
			NotifyHistory after = nhDao.query(user.getEmail());
			if(after != null && after.getTitleList() != null) {
				afterSize = after.getTitleList().size();
			}
			map.put(user.getEmail(), afterSize - beforeSize);
		}
		renderGson(map, true);
	}
}
