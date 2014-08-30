package dao;

import java.util.List;

import model.BlockPattern;

public interface BlockPatternDAO {

	public List<BlockPattern>	query();
	public Boolean saveOrUpdate(BlockPattern pattern);
}
