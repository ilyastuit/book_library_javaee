package ru.ilyastuit.training.web.mail;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@RequestScoped
@ManagedBean
public class MailSender {

    private final String MAIL_SUPPORT_PARAM = "mail.SUPPORT";
    private String message = "test message";
    private String subject = "test subject";

//    @Resource(name = "mail/GMAIL")
//    private Session mailSession;
    
    public void sendMessage() {
        try {

            String mailRecipient = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(MAIL_SUPPORT_PARAM);

            Session session = getSession();
            
            Message msg = new MimeMessage(session);
            
            InternetAddress address = new InternetAddress(mailRecipient);

            msg.setSubject(subject);
            msg.setRecipient(RecipientType.TO, address);
            msg.setText(message);
            Transport.send(msg);

        } catch (NamingException ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException me) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, me);
        }
    }

    private Session getSession() throws NamingException {
        InitialContext ic = new InitialContext();      
        return (javax.mail.Session) ic.lookup("mail/GMAIL");
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static void sendMail() {
        try {
            String SMTP_AUTH_USER = "testlibrary22@gmail.com";
            String SMTP_AUTH_PWD = "library123456";

            Properties props = new Properties();

            props.put("mail.transport.protocol", "smtps");
            props.put("mail.smtps.auth", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);
            Transport transport = session.getTransport();
            transport.connect("smtp.gmail.com", 465, SMTP_AUTH_USER, SMTP_AUTH_PWD);

            MimeMessage message = new MimeMessage(session);
            message.setSubject("test subject");
            message.setText("test text");
            
            InternetAddress address = new InternetAddress("testlibrary22@gmail.com");
            
            message.addRecipient(Message.RecipientType.TO, address);
            message.setSentDate(new Date());

            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            
        } catch (MessagingException ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        sendMail();
    }


}
