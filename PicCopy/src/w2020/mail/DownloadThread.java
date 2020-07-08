package w2020.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class DownloadThread extends Thread{

	private List<Message> messages = null;
	private String key = null;
	
	public DownloadThread(List<Message> messages , String key ) {
		this.messages = messages;
		this.key = key;
	}
	
	@Override
	public void run() {
		
		
		try {
			parseMessage(messages);
		} catch ( Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	
	public void parseMessage( List<Message>  messages) throws MessagingException, IOException {
		if (messages == null || messages.size() < 1) 
			throw new MessagingException("未找到要解析的邮件!");
		String subject = "";
		// 解析所有邮件
		for (int i = 0, count = messages.size() ; i < count; i++) {
			try {
				
				MimeMessage msg = (MimeMessage) messages.get(i);
	//			System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
				subject = POP3ReceiveMail.getSubject(msg).trim();
	//			System.out.println("主题: " + subject);
	//			System.out.println("发件人: " + getFrom(msg));
	//			System.out.println("收件人：" + getReceiveAddress(msg, null));
	//			System.out.println("发送时间：" + getSentDate(msg, null));
	//			System.out.println("是否已读：" + isSeen(msg));
	//			System.out.println("邮件优先级：" + getPriority(msg));
	//			System.out.println("是否需要回执：" + isReplySign(msg));
	//			System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
				boolean isContainerAttachment =POP3ReceiveMail.isContainAttachment(msg);
//				System.out.println("是否包含附件：" + isContainerAttachment);
				if (isContainerAttachment) {
					//保存附件
	//				saveAttachment(  msg,  dstDir ); 
					List<MailAttachment> attas = new ArrayList<MailAttachment>();
					MailUtil.getAttachment(msg,attas );
					MTest.saveAttachment( attas , POP3ReceiveMail.dstDir + "/" +key + "/" + subject.trim() , key );  //   + "/"   + (i+1) 
				} 
				
	//			StringBuffer content = new StringBuffer(30);
	//			getMailTextContent(msg, content);
	//			System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0,100) + "..." : content));
	//			System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
	//			System.out.println();
			} 
			catch (Exception e) {
				System.err.println( subject + " maybe error : "); 
				e.printStackTrace();
			}
			
		}
	}
	
	
	
}
