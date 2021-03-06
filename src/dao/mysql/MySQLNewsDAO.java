package dao.mysql;

import java.util.Date;
import java.util.List;

import main.Constants;
import model.News;
import utils.db.MySQLUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import dao.NewsDAO;

public class MySQLNewsDAO implements NewsDAO {

	@Override
	public int save(List<News> newsList) {
		if (MySQLUtils.batchInsertWithBeans("insert ignore into news_record(title, description, newsUrl, dateTime, pubfrom) values(?,?,?,?,?)", 
				new String[]{"title", "description", "newsUrl", "dateTime", "pubFrom"},
				newsList.toArray()) == true) {
			return Constants.LABEL_SUCCESS;
		} else {
			return Constants.LABEL_FAILED;
		}
	}

	@Override
	public List<News> query(String... keywords) {
		Long now = new Date().getTime();
		Long before = now - 3600L * 1000L * 24;

		String orTemplate = " title like ? or description like ? ";
		String sql = "select * "
				+ "from (select * from news_record where dateTime > ? ) AS sb "
				+ "where  " + orTemplate;
		
		String[] queryParams = new String[keywords.length * 2];
		queryParams[0] = "%" + keywords[0] + "%";
		queryParams[1] = "%" + keywords[0] + "%";
		
		for(int i = 1 ; i < keywords.length; i++) {
			sql += " or " + orTemplate;
			queryParams[2*i] = "%" + keywords[i] + "%";
			queryParams[2*i + 1] = "%" + keywords[i] + "%";
		}
		
		sql += " order by dateTime desc";
		
		Object[] params = Lists.asList(before, queryParams).toArray();
		System.err.println("!!!SQL=" + sql);
		System.err.println("!!!Par=" + new Gson().toJson(params));
		
		return MySQLUtils.queryList(sql, News.class, params);
	}

	@Override
	public List<News> query() {
		return MySQLUtils.queryAll("select * from news_record", News.class);
	}

	@Override
	public boolean clear() {
		return MySQLUtils.truncate("news_record");
	}

}
