package dao.mysql;

import java.util.List;
import java.util.Map;

import main.Constants;
import model.NotifyHistory;

import org.apache.commons.lang.StringUtils;

import utils.db.MySQLUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dao.NotifyHistoryDAO;

public class MySQLNotifyHistoryDAO implements NotifyHistoryDAO {

	@Override
	public NotifyHistory query(String id) {
//		return (NotifyHistory) MySQLUtils.query(NotifyHistory.class, "select * from notify_hist");
		Map map = MySQLUtils.query("select * from notify_hist where id = ?", id);
		if(map == null) return null;
		
		NotifyHistory hist = new NotifyHistory();
		if(map.containsKey("id"))
			hist.setId(map.get("id").toString());
		if(map.containsKey("lastNotifyResult") && map.get("lastNotifyResult") != null && (Integer)map.get("lastNotifyResult") == Constants.LABEL_SUCCESS)
			hist.setLastNotifyResult(true);
		else 
			hist.setLastNotifyResult(false);
		if(map.containsKey("lastNotifyTime")){
			hist.setLastNotifyTime((Long) map.get("lastNotifyTime"));
		}
		if(map.containsKey("titleList")) {
			List<String> titleList = new Gson().fromJson((String)map.get("titleList"), new TypeToken<List<String>>(){}.getType()); 
			hist.setTitleList(titleList);
		}
		if(map.containsKey("descList")) {
			List<String> descList = new Gson().fromJson((String)map.get("descList"), new TypeToken<List<String>>(){}.getType()); 
			hist.setDescList(descList);
		}
		return hist;
	}

	@Override
	public boolean saveOrUpdate(NotifyHistory history) {
		if(history == null || StringUtils.isEmpty(history.getId()))
			return false;
		
		NotifyHistory oldHistory = query(history.getId());
		if(oldHistory == null) {
//			mgoUtils.save(history);
//			return MySQLUtils.batchInsertWithBeans(
//					"insert into notify_hist(id, lastNotifyTime, titleList, lastNotifyResult) "
//					+ "values(?,?,?,?)",
//					new String[]{"id", "lastNotifyTime", "titleList", "lastNotifyResult"}, 
//					Lists.newArrayList(history).toArray());
			return MySQLUtils.insert("insert into notify_hist(id, lastNotifyTime, titleList, descList, lastNotifyResult) "
					+ "values(?,?,?,?,?)", 
					history.getId(),
					history.getLastNotifyTime(), 
					new Gson().toJson(history.getTitleList()),
					new Gson().toJson(history.getDescList()), 
					history.getLastNotifyResult());
		} else {
			return MySQLUtils.update("update notify_hist "
					+ "set "
					+ "lastNotifyTime = ?, "
					+ "titleList = ?, "
					+ "descList = ?, "
					+ "lastNotifyResult = ? "
					+ "where "
					+ "id = ?", 
					history.getLastNotifyTime(), 
					new Gson().toJson(history.getTitleList()),
					new Gson().toJson(history.getDescList()), 
					history.getLastNotifyResult(), 
					history.getId());
		}
		
	}

}
