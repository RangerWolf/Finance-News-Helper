package dao;

import java.util.List;

import model.Keyword;

public interface KeywordDAO {

	/**
	 * 获得所有的keyword
	 * @return
	 */
	public List<String> query();
	
	/**
	 * 将新的关键字保存到数据库之后。如果list之中包含之前的关键字会被略过
	 * @param list
	 * @return
	 */
	public int save(List<Keyword> list);
	
	/**
	 * 清除所有的keyword
	 * @return
	 */
	public boolean clear();
}
