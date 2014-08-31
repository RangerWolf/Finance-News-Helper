package dao.mysql;

import java.util.List;
import java.util.Map;

import model.BlockPattern;
import utils.db.MySQLUtils;

import com.google.common.collect.Lists;

import dao.BlockPatternDAO;

public class MySQLBlockPatternDAO implements BlockPatternDAO{

	
	@Override
	public List<BlockPattern> query() {
//		List<BlockPattern> list = MySQLUtils.queryAll("select * from block_pattern", BlockPattern.class);
		List<Map<String, Object>> list = MySQLUtils.queryList("select * from block_pattern", null);
		List<BlockPattern> patternList = Lists.newArrayList();
		
		for(Map<String, Object> map : list) {
			BlockPattern bp = new BlockPattern();
			if(map.containsKey("id")) bp.setId((Integer)map.get("id"));
			if(map.containsKey("pattern")) bp.setPattern((String)map.get("pattern"));
			if(map.containsKey("method")) {
				Integer intMethod = (Integer) map.get("method");
				if(intMethod == BlockPattern.Method.SIMPLE_MATCH.getValue()) {
					bp.setMethod(BlockPattern.Method.SIMPLE_MATCH);
				} 
				else if(intMethod == BlockPattern.Method.REGEX.getValue()) {
					bp.setMethod(BlockPattern.Method.REGEX);
				} 
				else if(intMethod == BlockPattern.Method.FULL_TEXT_SEARCH.getValue()) {
					bp.setMethod(BlockPattern.Method.FULL_TEXT_SEARCH);
				}
			}
			patternList.add(bp);
		}
		
		return patternList;
	}

	
	
	public Map<String, Object> findOne(Integer id) {
		return MySQLUtils.query("select id from block_pattern where id = ?", id);
	}
	
	@Override
	public Boolean saveOrUpdate(BlockPattern pattern) {
		if(pattern.getId() != null && findOne(pattern.getId()) != null) {
			return MySQLUtils.update("update block_pattern set pattern = ?, method = ? where id = ?",
					pattern.getPattern(),
					pattern.getMethod().getValue(),
					pattern.getId());
		} else {
			if(pattern.getId() == null)
				return MySQLUtils.insert("insert into block_pattern(pattern, method) values(?,?)", 
						pattern.getPattern(),
						pattern.getMethod().getValue()
				);
			else 
				return MySQLUtils.insert("insert into block_pattern(id,pattern, method) values(?,?,?)", 
						pattern.getId(),
						pattern.getPattern(),
						pattern.getMethod().getValue()
				);
		}
	}

	public static void main(String[] args) {
		MySQLBlockPatternDAO dao = new MySQLBlockPatternDAO();
//		System.out.println(new Gson().toJson(dao.query()));
		BlockPattern bp = new BlockPattern();
//		bp.setId(2222);
		bp.setMethod(BlockPattern.Method.FULL_TEXT_SEARCH);
		bp.setPattern("?");
		dao.saveOrUpdate(bp);
//		System.out.println(BlockPattern.Method.FULL_TEXT == 3);
	}
	
}
