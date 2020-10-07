package org.xas.uned.camip.notifier;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.xas.uned.camip.bean.DeviceCheckData;

public class EmailNotifier implements Notifier {

	private static final String SUBJECT = "Security fail detected";

	private static final String fromAddr = "the_addr";
	private static final String fromPss = "the_pass";

	@Override
	public void doSend(String title, List<String> message, List<String> dest) {
		try {

			StringBuilder sb = new StringBuilder();
			for (String msg : message) {
				sb.append(msg).append(". ");
			}

			Session mailSession = Session.getInstance(getProperties(), new PasswordAuthenticator(fromAddr, fromPss));

			MimeMessage msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress(fromAddr));

			msg.setHeader("Content-Transfer-Encoding", "base64");
			msg.setSubject(SUBJECT);
			msg.setContent(message, "text/html");
			for (String toAddr : dest) {

				try {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
					Transport.send(msg);
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {

		}

	}

	public void doSend(DeviceCheckData data, List<String> dest) {

	}

	// This class Authnticates the password
	private class PasswordAuthenticator extends Authenticator {

		String from;

		String password;

		public PasswordAuthenticator(String from, String password) {
			this.from = from;
			this.password = password;
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(from, password);
		}
	}

	// This method will return properties appropriate for the email account
	private Properties getProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		return props;
	}
}
