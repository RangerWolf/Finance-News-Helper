package crawler;

import java.util.List;

import model.News;

public interface Crawler {

	/**
	 * 从数据库之中获得目标关键字
	 * @return
	 */
	public List<String> getKeywords();
	
	
	/**
	 * 根据关键字进行URL拼接
	 * @return
	 */
	public String buildURL(String keywords);
	
	/**
	 * 从目标新闻地址爬取新闻内容
	 * @param newsURL 新闻地址
	 */
	public String fetch(String newsURL);
	
	/**
	 * 将目标新闻内容利用一定的方法进行解析
	 * @param content 获取的新闻内容
	 * @return 从新闻原内容解析出来的新闻对象列表
	 */
	public List<News> parse(String content);
	
	/**
	 * 将新闻存放到数据库之中
	 * @param news 目标新闻列表
	 * @return 是否存放成功
	 */
	public Boolean save(List<News> news);
	
}
