package uk.co.ceva24.emailsender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;

public class EmailDriver
{
    private static final String SMTP_HOST = "";
    private static final String EMAIL_ADDRESS = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public static void main(String[] args)
    {
        final Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        final EmailSender sender = new EmailSender(EMAIL_ADDRESS, props, USERNAME, PASSWORD);

        try
        {
            sender.sendEmail(EMAIL_ADDRESS, "Java Email Test", "Hello World!");

            final List<EmailSender.Attachment> attachments = new ArrayList<>();
            EmailSender.Attachment item = new EmailSender.Attachment("myzip.zip", "C:\\azip.zip", "application/zip");
            attachments.add(item);

            sender.sendEmailWithAttachments(EMAIL_ADDRESS, "Java Email with Attachment Test", "Hello World 2!", attachments);
        }
        catch (MessagingException e)
        {
            System.err.println("Failed to send email: " + e.getMessage());
        }
        catch (IOException e)
        {
            System.err.println("Failed to create attachment: " + e.getMessage());
        }
    }
}