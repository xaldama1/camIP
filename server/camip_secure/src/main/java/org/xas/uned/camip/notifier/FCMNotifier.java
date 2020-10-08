package org.xas.uned.camip.notifier;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.xas.uned.camip.bean.DeviceCheckData;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

public class FCMNotifier implements Notifier {

	private static final String TOPIC = "camip";

	public FCMNotifier() {
		try {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(new ClassPathResource("fcm.json").getInputStream()))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
		} catch (IOException e) {

		}
	}

	@Override
	public void doSend(String title, List<String> message, List<String> dest) {
		try {

			StringBuilder sb = new StringBuilder();
			for (String msg : message) {
				sb.append(msg).append(". ");
			}
			Message fcmMessage = Message.builder().setNotification(new Notification(title, sb.toString()))
					.setTopic(TOPIC).build();

			FirebaseMessaging.getInstance().send(fcmMessage);

		} catch (Exception e) {

		}

	}

	@Override
	public void doSend(DeviceCheckData data, List<String> dest) {
		try {

			Message fcmMessage = Message.builder().putData("type", "1").putData("fromIP", data.getFromIp())
					.putData("toIP", data.getIp()).putData("mac", String.valueOf(data.getMac()))
					.putData("index", String.valueOf(data.getRequestToken().getId()))
					.putData("ip", Inet4Address.getLocalHost().getHostAddress()).setTopic(TOPIC).build();

			FirebaseMessaging.getInstance().send(fcmMessage);

		} catch (Exception e) {

		}

	}

}
