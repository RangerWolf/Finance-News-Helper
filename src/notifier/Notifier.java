package notifier;

import java.util.Date;
import java.util.List;

import model.News;
import model.NotifyHistory;
import model.User;
import utils.MiscUtils;

import com.google.common.collect.Lists;

import dao.MailAccountDAO;
import dao.NewsDAO;
import dao.NotifyHistoryDAO;
import dao.UserDAO;
import dao.mysql.MySQLMailAccountDAO;
import dao.mysql.MySQLNewsDAO;
import dao.mysql.MySQLNotifyHistoryDAO;
import dao.mysql.MySQLUserDAO;

public abstract class Notifier {

	protected String userid;
	protected User targetUser = null;
	private NotifyHistory history = null;
	protected List<String> sentTitleList = null;
	
	protected UserDAO userDao = new MySQLUserDAO();
	protected NewsDAO newsDao = new MySQLNewsDAO();
	protected MailAccountDAO maDao = new MySQLMailAccountDAO();
	protected NotifyHistoryDAO nhDao = new MySQLNotifyHistoryDAO();
	
	public Notifier(String userid) {
		// init targetUser
		this.userid = userid;
		this.targetUser = userDao.query(this.userid);
	
		// init NotifyHistory
		NotifyHistory tmpHist = nhDao.query(this.userid);
		if(tmpHist != null) {
			history = tmpHist;
			sentTitleList = history.getTitleList();
		}
		else if(targetUser != null){
			history = new NotifyHistory();
			history.setId(userid);
			history.setLastNotifyResult(false);
			history.setLastNotifyTime(new Date().getTime());
			sentTitleList = Lists.newArrayList();
			history.setTitleList(sentTitleList);
		}
	}
	
	public void run() {
		if(targetUser != null) {
			List<News> list = this.getNewsFromDB();
			if(list.size() > 0) {
				list = this.mergeAndFilterNews(list);
				
				if(list.size() > 0) {
					String content = this.formatNews(list);
					boolean ret = this.sendNotification(content);
					history.setLastNotifyResult(ret);
				}
			}
			this.recordSendHistory();
		}
	}
	
	/**
	 * 获得用户的订阅的关键字，在这里面可以做一些优化措施<br>
	 * 比如 过滤重复的关键字、自动分析可以增加哪些关键字等<br>
	 * 目前仅需要过滤重复的关键字即可
	 * @return
	 */
	protected String[] getKeywords() {
		return this.targetUser.getKeywords().split(",");
	}
	
	/**
	 * 根据关键字数组 遍历的从数据库之中获得抓取到的信息<br>
	 * 由于有多个关键字，可能会有重复，或者非常类似的新闻
	 * @return
	 */
	private List<News> getNewsFromDB() {
		String[] keywords = getKeywords();
		List<News> list = Lists.newArrayList();
		for(String keyword : keywords) {
			if(keyword.trim().length() > 0)
				list.addAll(newsDao.query(keyword));
		}
		return list;
	}
	
	/**
	 * 对从数据库之中取出来的新闻列表进行过滤、整合，减少重复信息量
	 * @param list 从数据库之中找到的所有新闻
	 * @return
	 */
	protected List<News> mergeAndFilterNews(List<News> list) {
		List<News> filterdList = Lists.newArrayList();
		for(News news : list) {
			String title = news.getTitle();
			if(!MiscUtils.hasSimilarStr(title, sentTitleList)) {
				sentTitleList.add(title);
				filterdList.add(news);
			} 
		}
		history.setTitleList(sentTitleList);
		return filterdList;
	}
	
	/**
	 * 根据对应实例，整理成不同的格式，比如for Email, for Weixin ...
	 * @param list 已经过滤过的新闻列表
	 * @return 对应格式的内容
	 */
	protected abstract String formatNews(List<News> list);
	
	/**
	 * 通过相应的方式将信息发送出去
	 * @param notifyContent
	 * @return
	 */
	protected abstract Boolean sendNotification(String notifyContent);
	
	/**
	 * 将推送信息的动作以及结果存放到数据库之中
	 */
	private void recordSendHistory() {
		nhDao.saveOrUpdate(this.history);
	}
	
}
