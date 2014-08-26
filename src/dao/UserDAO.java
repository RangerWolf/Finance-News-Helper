package dao;

import java.util.List;

import model.User;

public interface UserDAO {

	public void saveOrUpdate(User user);
	public List<User> queryAll();
	
	/**
	 * 通过email获取目标用户，其中email不允许重复，<br>
	 * 因此正确情况下只返回1个user, 在查询的email不存在的时候返回null
	 * @param email
	 * @return
	 */
	public User query(String email);
	public void clear();
}
