package dao.mysql;

import java.util.List;

import model.User;
import utils.db.MySQLUtils;
import dao.UserDAO;

public class MySQLUserDAO implements UserDAO {

	@Override
	public void saveOrUpdate(User user) {
		User u = query(user.getEmail());
		if(u != null) {
			MySQLUtils.update("update user set keywords=?, stocks=?, password=? where email=?",
					user.getKeywords(), 
					user.getStocks(),
					user.getPassword(),
					user.getEmail());
		} else {
			MySQLUtils.insert("insert into user values(?)", user);
		}
	}

	@Override
	public List<User> queryAll() {
		return MySQLUtils.queryAll("select * from user", User.class);
	}

	@Override
	public User query(String email) {
		return (User) MySQLUtils.query(User.class, "select * from user where email = ?", email);
	}

	@Override
	public void clear() {
		MySQLUtils.truncate("user");
	}

}
