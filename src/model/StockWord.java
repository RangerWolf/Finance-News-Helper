package model;

import java.util.List;


/**
 * 用户记录用户关注的股票对应的关键字
 * @author wenjun_yang
 *
 */
public class StockWord {

	private String stockCode;
	private List<String> targetWords;
	
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public List<String> getTargetWords() {
		return targetWords;
	}
	public void setTargetWords(List<String> targetWords) {
		this.targetWords = targetWords;
	}
}
