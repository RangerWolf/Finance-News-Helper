package model;

public class BlockPattern {

	private Integer id;
	private String pattern;
	private Method method;
	public static enum Method {
		SIMPLE_MATCH(0),REGEX(1),FULL_TEXT_SEARCH(2);
		private final int value;
	    private Method(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	}
	
	
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
}
