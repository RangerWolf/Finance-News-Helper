package utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;

public class MiscUtils {

	public static List<String> mergeKeywordList(List<String> list) {
		
		List<String> retList = Lists.newArrayList();
		String tmp = "";
		for(String word : list) {
			if((tmp.length() + word.length()) < Constants.MAX_QUERY_WORDING_LEN) {
				if(tmp.trim().length() == 0) tmp = word;
				else tmp = tmp + " | " + word;
			} else {
				retList.add(tmp);
				tmp = word;
			}
		}
		retList.add(tmp);
		return retList;
	}
	
	/**
	 * 简单的从目标地址进行网页源码采集
	 * @param urlTemplate
	 * @param queryWord
	 * @param charset
	 * @return
	 */
	public static String simpleJsoupGet(String urlTemplate, String queryWord, String charset) {
		try {
			String word = queryWord;
			word = URLEncoder.encode(queryWord, charset);
			String url = String.format(urlTemplate, word);
			
			System.out.println("URL=" + url);
			String ret = Jsoup.connect(url)
					.header("Content-Type", "application/x-javascript; charset=" + charset)
					.ignoreContentType(true)
					.get().text();
			return ret;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 在目标列表之中是否存在类似的字符串 阈值：0.85
	 * @param newStr 新的一个字符串
	 * @param targetList 目标字符串列表
	 * @return 是否存在类似的字符串
	 */
	public static boolean hasSimilarStr(String newStr, List<String> targetList) {
		
		boolean hasSimilarStr = false;
		JaroWinkler algorithm = new JaroWinkler();
		if(targetList == null || targetList.size() == 0 ) return false;
		for(String str : targetList) {
			if(algorithm.getSimilarity(newStr, str) > 0.85) {
				hasSimilarStr = true;
				break;
			}
		}
		return hasSimilarStr;
	}
	
	public static void enableProxy() {
		System.setProperty("http.proxyHost", "cnnjproxy-ha.tw.trendnet.org"); // set proxy server
		System.setProperty("http.proxyPort", "8080"); // set proxy port
	}
	
	public static void enableProxy(String host, String port) {
		System.setProperty("http.proxyHost", host); // set proxy server
		System.setProperty("http.proxyPort", port); // set proxy port
	}
	
	public static void disableProxy() {
		try {
			System.out.println(System.getProperty("http.proxyHost"));
			System.setProperty("http.proxyHost", null);
		} catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	public final static String getHostName() {
		String hostName = "unknown";
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			hostName =addr.getHostName().toString();//获得本机名称
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}
	
	 public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
