package io.calendar.management.api.util;

import com.sun.mail.smtp.SMTPTransport;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class SmtpUtil {
    // for example, smtp.mailgun.org
    private static final String SMTP_SERVER = "mock";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    private static final String EMAIL_FROM = "support@api-management.com";

    private static final String EMAIL_SUBJECT = "Reset password link";



    public static void sendEmail(String emailTo, String content) {
        try{
            System.out.println("SimpleEmail Start");


            String emailID = "email_me@example.com";

            Properties props = System.getProperties();

            props.put("mail.smtp.host", SMTP_SERVER);

            Session session = Session.getInstance(props, null);

            EmailUtil.sendEmail(session, emailTo ,EMAIL_FROM, content);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
