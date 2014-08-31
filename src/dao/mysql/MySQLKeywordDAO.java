package dao.mysql;

import java.util.List;

import main.Constants;
import model.Keyword;
import utils.db.MySQLUtils;

import com.google.common.collect.Lists;

import dao.KeywordDAO;

public class MySQLKeywordDAO implements KeywordDAO{
	
//	MySQLUtils dbUtils = new MySQLUtils();
	
	@Override
	public List<String> query() {
		List<Keyword> list = MySQLUtils.queryAll("select * from keyword", Keyword.class);
		List<String> retList = Lists.newArrayList();
		for(Keyword word: list) {
			retList.add(word.getWord());
		}
		return retList;
	}

	@Override
	public int save(List<Keyword> list) {
		List<String> insertedList = Lists.newArrayList();
		List<String> all = query();
		for(Keyword word : list) {
			if(!all.contains(word.getWord())) {
				insertedList.add(word.getWord());
			}
		}
		// translate list to object[][]
		String[][] objects = new String[insertedList.size()][1];
		for(int i = 0; i < insertedList.size(); i++) {
			objects[i][0] = insertedList.get(i);
		}
		boolean ret = MySQLUtils.batchInsert("insert into keyword(word) values(?)", objects);
		if(ret == true) {
			return Constants.LABEL_SUCCESS;
		}
		else return Constants.LABEL_FAILED;
	}

	@Override
	public boolean clear() {
		return MySQLUtils.truncate("keyword");
	}

}
