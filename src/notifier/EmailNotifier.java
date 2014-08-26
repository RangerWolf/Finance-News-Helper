package notifier;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.MailAccount;
import model.MailInfo;
import model.News;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import utils.EMailUtils;
import utils.MiscUtils;

import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;

public class EmailNotifier extends Notifier{

	public EmailNotifier(String userid) {
		super(userid);
	}

	@Override
	protected String formatNews(List<News> list) {
		String result = "";
		try {
			String filePath = PathKit.getWebRootPath() + File.separator 
	                + "template" + File.separator + "email_notify_template.html";
			if(new File(filePath).exists() == false)
				filePath = "E:\\Server\\email_notify_template.html";
			if(new File(filePath).exists() == false)
				filePath = "/home/finnews/email_notify_template.html";
			if(new File(filePath).exists() == false)
				filePath = getClass().getClassLoader().getResource("email_notify_template.html").getPath();
			String template = FileUtils.readFileToString(new File(filePath), "utf-8");
			
			// 写入关键字
			String[] keywordArr = getKeywords();
			String strKeyword = StringUtils.join(keywordArr, " ");
			template = StringUtils.replace(template, "${KEYWORD}", strKeyword);
			
			// 将关键字变红
			String[] redWordArr = new String[keywordArr.length];
			for(int i = 0; i < keywordArr.length; i++) {
				redWordArr[i] = "<font color='red'>" + keywordArr[i] + "</font>";
			}
			
			String templateStart = "<!-- [TEMPLATE-START] -->";
			String templateEnd = "<!-- [TEMPLATE-END] -->";
			int startIdx = template.indexOf(templateStart) + templateStart.length();
			int endIdx = template.indexOf(templateEnd);
			String templatePart = template.substring(startIdx, endIdx);
			
			StringBuffer sbf = new StringBuffer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(News news : list) {
				String temp = templatePart;
				String title = news.getTitle();
				String desc = news.getDescription();
				
				title = StringUtils.replaceEach(title, keywordArr, redWordArr);
				desc = StringUtils.replaceEach(desc, keywordArr, redWordArr);
				
				List<String> searchList = Lists.newArrayList("${LINK}", "${TITLE}", "${DESC}", "${UPDATE_TIME}", "${FROM}");
				List<String> replaceList = Lists.newArrayList(news.getNewsUrl(), title, desc, sdf.format(new Date(news.getDateTime())), news.getPubFrom());
				temp = StringUtils.replaceEach(temp, 
						convertListAsStringArray(searchList),
						convertListAsStringArray(replaceList));
				sbf.append(temp + "\r\n");
			}
			result = sbf.toString();
			result = template.substring(0, startIdx) + result + template.substring(endIdx);
			if(MiscUtils.getHostName().contains("wenjun"))
				FileUtils.write(new File("E:/mail.html"), result, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected Boolean sendNotification(String notifyContent) {
		MailAccount account =  maDao.getActiveMailAccount();
		System.out.println("MailAddr:" + account.getMailAddr());
		MailInfo info = new MailInfo();
		info.setTo(this.targetUser.getEmail());
		info.setBody(notifyContent);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		info.setSubject("StockNews [" + sdf.format(new Date()) + "]");
		System.out.println("Sent to:" + info.getTo()[0]);
		
		boolean ret = EMailUtils.send(account, info);
		return ret;
	}

	private String[] convertListAsStringArray(List<String> list) {
		if(list == null) return null;
		String[] arr = new String[list.size()];
		for(int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
	
}
