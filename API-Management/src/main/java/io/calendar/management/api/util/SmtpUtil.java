package io.calendar.management.api.util;


import java.util.Properties;
import javax.mail.Session;

public class SmtpUtil {


    private static final String SMTP_SERVER = "mock";

    private static final String EMAIL_FROM = "support@api-management.com";

    private static final String EMAIL_SUBJECT = "Reset password link";



    public static void sendEmail(String emailTo, String content) {
        try{

            Properties props = System.getProperties();

            props.put("mail.smtp.host", SMTP_SERVER);

            Session session = Session.getInstance(props, null);

            EmailUtil.sendEmail(session, emailTo ,EMAIL_SUBJECT, content);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
