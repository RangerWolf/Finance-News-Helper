package controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import notifier.EmailNotifier;
import notifier.Notifier;

import org.apache.commons.lang.StringUtils;

import validator.AdminActionValidator;
import model.Keyword;
import model.News;
import model.NotifyHistory;
import model.User;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jfinal.aop.Before;

import crawler.Crawler;
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
	
	public void updateKeywordAndGetNews() {
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
			
			// crawl news from google
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
			renderText("keyword/update:" + e.getMessage());
		}
	}
	
	public void notifyAllUsers() {
		List<User> userList = uDao.queryAll();
		Map<String, Integer> map = Maps.newHashMap();
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
		renderGson(map);
	}
}
