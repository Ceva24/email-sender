package uk.co.ceva24.emailsender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailSender
{
    final String fromAddress;
    final Session session;

    public EmailSender(String fromAddress, Properties props, final String user, final String pass)
    {
        this.fromAddress = fromAddress;

        // Create a new session with the user and pass.
        session = Session.getInstance(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(user, pass);
            }
        });
    }

    public void sendEmail(String toAddress, String subject, String body) throws AddressException, MessagingException
    {
        final Message message = new MimeMessage(session);

        // Set the email properties.
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        message.setFrom(new InternetAddress(fromAddress));
        message.setSubject(subject);
        message.setText(body);

        // Send the email.
        Transport.send(message);
    }

    public void sendEmailWithAttachments(String toAddress, String subject, String body, List<Attachment> attachments)  throws AddressException, MessagingException
    {
        // Construct the email as a mime multipart - body text and attachment.
        final Message message = new MimeMessage(session);
        final Multipart content = new MimeMultipart();

        // Set the email properties.
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
        message.setFrom(new InternetAddress(fromAddress));
        message.setSubject(subject);

        // Create the body text part.
        final BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(body);
        content.addBodyPart(bodyPart);

        for (Attachment item : attachments)
        {
            // Create the attachment part.
            final BodyPart attachment = new MimeBodyPart();

            final DataSource source = new ByteArrayDataSource(item.getByteArray(), item.getMediaType());

            attachment.setDataHandler(new DataHandler(source));
            attachment.setFileName(item.getName());

            content.addBodyPart(attachment);
        }

        message.setContent(content);

        // Send the email.
        Transport.send(message);
    }
    
    public static final class Attachment
    {
        final String name;
        final String mediaType;
        final byte[] file;

        public Attachment(String name, String filePath, String mediaType) throws IOException
        {
            this.name = name;
            this.file = Files.readAllBytes(Paths.get(filePath));
            this.mediaType = mediaType;
        }

        public String getName()
        {
            return name;
        }

        public byte[] getByteArray()
        {
            return file;
        }

        public String getMediaType()
        {
            return mediaType;
        }
    }
}