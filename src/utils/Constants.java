package utils;

public class Constants {

	/**
	 * 每天有多少毫秒
	 */
	public static final long MSEC_PER_DAY = 1000 * 60 * 60 * 24;
	
	/**
	 * 每天使用Gmail发送，冲顶1000封
	 */
	public static final String EXCEED_QUOTA_ERROR_INFO = "Daily sending quota exceeded";
	
	public static final Integer MAX_QUERY_WORDING_LEN = 38; //实际上可以到38的
	
	public static final boolean ACCOUNT_ACTIVE = true;
	public static final boolean ACCOUNT_DEACTIVE = false;
	
	public static final String ATTR_LOGIN_EMAIL = "login_email";
	public static final String ATTR_USER_STOCK = "stock";
	public static final String ATTR_USER_KEYWORD = "keyword";
	
	public static final int LABEL_SUCCESS = 1;
	public static final int LABEL_FAILED = 0;	
}
