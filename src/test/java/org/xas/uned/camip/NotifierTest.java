package org.xas.uned.camip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.xas.uned.camip.bean.DeviceCheckData;
import org.xas.uned.camip.bean.Devicetoken;
import org.xas.uned.camip.notifier.FCMNotifier;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = AnnotationConfigContextLoader.class)
public class NotifierTest {

	@Autowired
	private FCMNotifier fcmNotifier;

	@Test
	public void sendNotificationTest() {

		fcmNotifier.doSend("title", Arrays.asList("message"), null);

	}

	@Test
	public void sendDataNotificationTest() {

		Devicetoken token = new Devicetoken();
		token.setId(1);
		token.setToken("testdevicetoken");
		List<Devicetoken> tokens = new ArrayList<>();
		tokens.add(token);
		tokens.add(token);

		DeviceCheckData deviceCheckData = new DeviceCheckData("ip", "fromIp", "mac", "name", null, token, false, null);
		fcmNotifier.doSend(deviceCheckData, null);
	}

}
