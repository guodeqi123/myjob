package w2020.mail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

public class MTest {

	public static void main(String[] args) {

	}

	public void readMail() throws MessagingException {
        long startTime = System.currentTimeMillis();
        MailUtil mailUtil = new MailUtil();
        Message[] messages = mailUtil.getPopMessages();

        for (Message message : messages) {
            String subject = mailUtil.getSubject(message);
            try {
                    List<MailAttachment> mailAttachments = new ArrayList<>();
                    mailUtil.getAttachment(message, mailAttachments);
                    saveAttachment(mailAttachments , "D:/123/" , "TTT");
            } catch (Exception ignore) {
            	
            }
        }
        mailUtil.close();
        System.out.println("解析邮件共耗时：" + (System.currentTimeMillis() - startTime) + "毫秒"   );
    }

	public static void saveAttachment(List<MailAttachment> list , String baseDir , String key ) throws IOException {
        for (MailAttachment mailAttachment : list) {
            InputStream inputStream = mailAttachment.getInputStream();
            
            File dirFile = new File(baseDir);
            if(!dirFile.exists()){
            	dirFile.mkdirs();
            }
            
            String ffpath = baseDir + "/" + mailAttachment.getName();
            File file = new File(  ffpath );
            if( file.exists() ){
            	file.delete();
            }
            FileOutputStream outputStream = new FileOutputStream( file);
            int len;
            byte[] bytes = new byte[1024 * 1024];
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            inputStream.close();
            outputStream.close();
            
            
            System.out.println( key + "  保存附件:::: " +   mailAttachment.getName()  );
        }
    }
}
