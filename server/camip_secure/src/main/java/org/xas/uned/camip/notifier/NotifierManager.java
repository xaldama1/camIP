package org.xas.uned.camip.notifier;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.DeviceCheckData;

@Service
public class NotifierManager {

	private List<Notifier> notifiers = null;

	private List<String> dest = null;

	@PostConstruct
	public void init() {
		notifiers = new ArrayList<>();
		notifiers.add(new FCMNotifier());
		notifiers.add(new EmailNotifier());
	}

	public void doSend(String title, List<String> message) {

		for (Notifier notifier : notifiers) {
			notifier.doSend(title, message, dest);
		}
	}

	public void doSend(DeviceCheckData data) {
		for (Notifier notifier : notifiers) {
			notifier.doSend(data, dest);
		}
	}

}
