package org.xas.uned.camip;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.xas.uned.camip.bean.CheckerStatus;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.Devicechecker;
import org.xas.uned.camip.bean.Devicetoken;
import org.xas.uned.camip.bean.TrafficData;
import org.xas.uned.camip.checker.DoubleCheckChecker;
import org.xas.uned.camip.repository.DeviceCheckerRepository;
import org.xas.uned.camip.repository.DeviceRepository;
import org.xas.uned.camip.service.NetworkMonitoringService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class, loader = AnnotationConfigContextLoader.class)
public class NetworkMonitoringTest {

	@Autowired
	private NetworkMonitoringService networkMonitoringService;

	@Autowired
	private DoubleCheckChecker doubleCheckChecker;

	@Autowired
	private DeviceCheckerRepository checkRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Test
	public void getDevidesTest() {

		networkMonitoringService.freeProcess();

	}

	@Test
	public void getDevidesInfoConverterTest() {
		String info = "******23:48:22.852425 IP 192.168.0.21.50206 > ec2-18-196-104-43.eu-central-1.compute.amazonaws.com.https: Flags [P.], seq 1872:1944, ack 5453, win 4096, options [nop,nop,TS val 523953927 ecr 389150034], length 72";
		info = info.substring(6);
		String[] infoParts = info.split(" ");
		TrafficData data = new TrafficData();
		// for (String part : infoParts) {
		String from = infoParts[2];
		String to = infoParts[4];
		data.setFromHost(from.substring(0, from.lastIndexOf(".")));
		data.setFromPort(from.substring(from.lastIndexOf(".") + 1));
		data.setToHost(to.substring(0, to.lastIndexOf(".")));
		data.setToPort(to.substring(to.lastIndexOf(".") + 1));
		data.setTime(new DateTime());

		System.out.println(data);
		// }
	}

	@Test
	public void doubleCheckTest() throws Exception {

		TrafficData data = new TrafficData();
		// for (String part : infoParts) {
		String from = "1.1.1.1";
		String to = "2.2.2.2";
		data.setFromHost(from.substring(0, from.lastIndexOf(".")));
		data.setFromPort(from.substring(from.lastIndexOf(".") + 1));
		data.setToHost(to.substring(0, to.lastIndexOf(".")));
		data.setToPort(to.substring(to.lastIndexOf(".") + 1));
		data.setTime(new DateTime());

		Device device = new Device();
		// device.setId(1);
		device.setMac("mac");
		device.setIp("ip");
		device.setName("name");

		Devicetoken token = new Devicetoken();
		token.setId(1);
		token.setToken("testdevicetoken");
		List<Devicetoken> tokens = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			tokens.add(token);
		}

		device = deviceRepository.save(device);

		device.setTokens(tokens);

		Devicechecker deviceChecker = new Devicechecker();
		deviceChecker.setDevice(device);
		deviceChecker.setChecker("DoubleCheckChecker");
		deviceChecker.setStatus(CheckerStatus.BLOCKING);
		checkRepository.save(deviceChecker);

		Field field = DoubleCheckChecker.class.getDeclaredField("DEVICEMAP");
		field.setAccessible(true);

		Map<String, Device> fieldValue = (Map<String, Device>) field.get(null);
		fieldValue.put(data.getToHost(), device);

		System.out.println("************Start:" + System.currentTimeMillis());
		doubleCheckChecker.doCheck(true, data, device);
	}
}
