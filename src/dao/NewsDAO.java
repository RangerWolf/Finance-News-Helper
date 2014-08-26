package dao;

import java.util.List;

import model.News;

public interface NewsDAO {

	/**
	 * 将新闻列表存储到数据库之中
	 * @param newsList
	 */
	public int save(List<News> newsList);
	
	/**
	 * 根据关键字进行查询10个小时以内的新闻<br>
	 * 关键字在新闻标题或者内容之中出现<br>
	 * 关键字支持一次使用多个
	 * @param keywords
	 * @return
	 */
	public List<News> query(String... keywords);
	
	/**
	 * 查询所有记录
	 * @return
	 */
	public List<News> query();
	
	
	/**
	 * 清楚所有记录
	 * @return
	 */
	public boolean clear();
}
