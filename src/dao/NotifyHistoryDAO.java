package dao;

import java.util.List;

import model.News;
import model.NotifyHistory;

public interface NotifyHistoryDAO {

	/**
	 * 根据用户的邮件地址进行查询
	 * @param id 用户的邮件地址/微信ID/微博ID...
	 * @return 获得用户之前通知的历史记录
	 */
	public NotifyHistory query(String id);
	
	/**
	 * 更新用户的通知历史记录，当目标记录不存在的时候进行保存
	 * @param history 用户的新的通知历史
	 * @return 操作结果
	 */
	public boolean saveOrUpdate(NotifyHistory history);
	
	
}
