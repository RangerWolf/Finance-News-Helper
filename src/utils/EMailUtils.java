package utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import model.MailAccount;
import model.MailInfo;

import org.apache.commons.lang.StringUtils;

import dao.mysql.MySQLMailAccountDAO;
public class EMailUtils {
	private MimeMessage mimeMsg;
	private Session session;
	private Properties props;
	private String username;
	private String password;
	private Multipart mp;
	
	public EMailUtils(String smtp) {
		setSmtpHost(smtp);
		createMimeMessage();
	}
	private void setSmtpHost(String hostName) {
//		System.out.println("设置系统属性：mail.smtp.host=" + hostName);
		if (props == null) {
			props = System.getProperties();
		}
		props.put("mail.smtp.host", hostName);
	}
	private boolean createMimeMessage() {
		try {
//			System.out.println("准备获取邮件会话对象！");
			session = Session.getDefaultInstance(props, null);
		} catch (Exception e) {
//			System.out.println("获取邮件会话错误！" + e);
			return false;
		}
//		System.out.println("准备创建MIME邮件对象！");
		try {
			mimeMsg = new MimeMessage(session);
			mp = new MimeMultipart();

			return true;
		} catch (Exception e) {
//			System.out.println("创建MIME邮件对象失败！" + e);
			return false;
		}
	}

	/*定义SMTP是否需要验证*/
	private void setNeedAuth(boolean need) {
//		System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}
	private void setNamePass(String name, String pass) {
		username = name;
		password = pass;
	}

	/*定义邮件主题*/
	private boolean setSubject(String mailSubject) {
//		System.out.println("定义邮件主题！");
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
//			System.err.println("定义邮件主题发生错误！");
			return false;
		}
	}

	/*定义邮件正文*/
	private boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + mailBody, "text/html;charset=GBK");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
//			System.err.println("定义邮件正文时发生错误！" + e);
			return false;
		}
	}

	/*设置发信人*/
	private boolean setFrom(String from) {
//		System.out.println("设置发信人！");
		try {
			mimeMsg.setFrom(new InternetAddress(from)); //发信人
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*定义收信人*/
	private boolean setTo(String to) {
		if (to == null)
			return false;
//		System.out.println("定义收信人！");
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*定义抄送人*/
	private boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress
					.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*发送邮件模块*/
	private boolean sendOut() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
//			System.out.println("邮件发送中....");
			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username, password);
			transport.sendMessage(mimeMsg, mimeMsg
			.getRecipients(Message.RecipientType.TO));
//			System.out.println("发送成功！");
			transport.close();
			return true;
		} catch (Exception e) {
//			System.err.println("邮件失败！" + e);
			return false;
		}
	}

	public static Boolean sendFromGMailTLS(final MailAccount account, final MailInfo info) {
		if(!account.getMailAddr().endsWith("@gmail.com")) {
			System.out.println("发邮件的类型不对！");
			return false;
		}
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(account.getUsername(), account.getPassword());
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(account.getMailAddr()));
			message.setRecipients(Message.RecipientType.TO, info.getTo());
			message.setRecipients(Message.RecipientType.CC, info.getCc());
			message.setRecipients(Message.RecipientType.BCC, info.getBcc());
			message.setSubject(info.getSubject());
			message.setContent(info.getBody(), "text/html; charset=UTF-8");
			// 下面的代码可以被用来发送图片
//			MimeMultipart multipart = new MimeMultipart("related");
//	        BodyPart messageBodyPart = new MimeBodyPart();
//	        String htmlText = "<img src=\"cid:image\">";
//	        messageBodyPart.setContent(htmlText, "text/html");
//	        multipart.addBodyPart(messageBodyPart);
//            messageBodyPart = new MimeBodyPart();
//            DataSource ds=new FileDataSource("Files/" + account.getMailAddr() +  ".png");
//            messageBodyPart.setDataHandler(new DataHandler(ds));
//            messageBodyPart.setHeader("Content-ID","<image>");
//            multipart.addBodyPart(messageBodyPart);
//            message.setContent(multipart);
            
            Transport.send(message);
			
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			System.err.println("Error!");
//			logger.error(e.getMessage());
			if( e.getMessage().indexOf(Constants.EXCEED_QUOTA_ERROR_INFO) != -1) {
//				logger.info("Set mail account[" + account.getMailAddr() + "] as not active.");
//				MailAccountDAO.deActiveMailAccount(account);
				new MySQLMailAccountDAO().deActiveMailAccount(account.getMailAddr());
			}
		}
		return false;
	}
	
	/*调用sendOut方法完成发送*/
	private static boolean sendAndCc(String smtp, String from, String to, String copyto,
		String subject, String content, String username, String password) {
		EMailUtils theMail = new EMailUtils(smtp);
		theMail.setNeedAuth(true); // 验证
		if (!theMail.setSubject(subject))
			return false;
		if (!theMail.setBody(content))
			return false;
		if (!theMail.setTo(to))
			return false;
		if (!theMail.setCopyTo(copyto))
			return false;
		if (!theMail.setFrom(from))
			return false;
		theMail.setNamePass(username, password);
		if (!theMail.sendOut())
			return false;
		return true;
	}
	
	/**
	 * 发送邮件
	 * @param account 发邮件使用的账号
	 * @param info 发送的邮件内容
	 * @return
	 */
	public static boolean send(MailAccount account, MailInfo info) {
		if(info == null || account == null) return false;
		String from = account.getMailAddr();
		String smtp = null;
		if(from.endsWith("@gmail.com")) {
			return sendFromGMailTLS(account, info);
		} else {
			if(from.endsWith("@qq.com")) smtp = "smtp.qq.com";
			else if(from.endsWith("@126.com")) smtp = "smtp.126.com";
			else if(from.endsWith("@163.com")) smtp = "smtp.163.com";
			else {
				System.err.println("不支持：" + from);
				return false;
			}
			
			String username = from;
			String password = account.getPassword();
			String to = StringUtils.join(info.getTo(), ",");
			String copyto = "";
			
			String subject = info.getSubject();
			String content = info.getBody();
			return sendAndCc(smtp, from, to, copyto, subject, content, username, password);
		}
	}
	
	public static void main(String[] args) {
////		String smtp = "smtp.126.com";// smtp服务器
////		String from = "workemail2009@126.com";// 邮件显示名称
////		String to = "yang.rangerwolf@gmail.com";// 收件人的邮件地址，必须是真实地址
		String copyto = "";// 抄送人邮件地址
		String subject = "测试邮件";// 邮件标题
		String content = "你好！";// 邮件内容
////		String username = "workemail2009@126.com";// 发件人真实的账户名
////		String password = "lplplplp";// 发件人密码
//		
////		String smtp = "smtp.163.com";
////		String from = "wolf198688@163.com";
////		String to = "yang.rangerwolf@gmail.com";
////		String username = from;
////		String password = "1qaz2wsx3edc";
//		
//		
////		String smtp = "smtp.qq.com";
////		String from = "526047326@qq.com";
////		String to = "yang.rangerwolf@gmail.com";
////		String username = from;
////		String password = "1qaz@WSX";
		
		String smtp = "smtp.gmail.com";
		String to = "yang.rangerwolf@gmail.com";
		String from = "improve.apk.rating01@gmail.com";
		String username = from;
		String password = "service=mail";
		
		
		MailAccount account = new MailAccount();
		account.setMailAddr(from);
		account.setPassword(password);
		account.setUsername(username);
		
		MailInfo info = new MailInfo();
		info.setTo(to);
		info.setBody("test");
		info.setSubject("test subject");
//		EMailUtils.sendFromGMailTLS(account, info);
		
//		EMailUtils.sendAndCc(smtp, from, to, copyto, subject, content, username, password);
	}
}