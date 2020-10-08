package org.xas.uned.camip.notifier;

import java.util.List;

import org.xas.uned.camip.bean.DeviceCheckData;

public interface Notifier {

	public void doSend(String title, List<String> message, List<String> dest);

	public void doSend(DeviceCheckData data, List<String> dest);

}
