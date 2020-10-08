package org.xas.uned.camip.checker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.DeviceCheckData;
import org.xas.uned.camip.bean.Devicetoken;
import org.xas.uned.camip.bean.TrafficData;
import org.xas.uned.camip.notifier.NotifierManager;
import org.xas.uned.camip.repository.DeviceCheckerRepository;
import org.xas.uned.camip.service.FirewallService;
import org.xas.uned.camip.util.TaskConstants;

@Service
public class DoubleCheckChecker extends AbstractChecker implements Observer {

	private static final int MAX_TIME_WAITING_SEC = 15;

	private static final int MAX_CACHED_TIME_MIN = 15;

	private static final Map<String, Device> DEVICEMAP = new ConcurrentHashMap<>();

	private static final Map<String, DeviceCheckData> WAITING_LIST = new ConcurrentHashMap<>();

	@Autowired
	private FirewallService firewallService;

	@Autowired
	private NotifierManager notifierManager;

	@Autowired
	private DeviceCheckerRepository deviceCheckerRepository;

	private boolean requestForToken(TrafficData requestData) {

		boolean isValid = true;

		if (!WAITING_LIST.containsKey(requestData.getToHost())) {

			int tokenIndex = new Random().nextInt(TaskConstants.MAX_TOKENS + 1);

			Device deviceInfo = DEVICEMAP.get(requestData.getToHost());

			List<Devicetoken> tokens = null;

			if (deviceInfo != null) {
				tokens = deviceInfo.getTokens();
			}

			if (tokens != null) {

				Devicetoken token = tokens.get(tokenIndex);

				DeviceCheckData deviceCheckData = new DeviceCheckData(deviceInfo.getIp(), requestData.getFromHost(),
						deviceInfo.getMac(), deviceInfo.getName(), new DateTime(), token, false, null);

				WAITING_LIST.put(requestData.getToHost(), deviceCheckData);

				notifierManager.doSend(deviceCheckData);

			}
		}

		return isValid;
	}

	public void checkWaitingList() {
		DateTime current = new DateTime();
		for (Entry<String, DeviceCheckData> waiting : WAITING_LIST.entrySet()) {
			DeviceCheckData deviceCheckData = waiting.getValue();
			if (deviceCheckData.isReceived()) {
				if (deviceCheckData.getRequestTime().plusMinutes(MAX_CACHED_TIME_MIN).isAfter(current)) {
					WAITING_LIST.remove(waiting.getKey());
				}
			} else if (deviceCheckData.getRequestTime().plusSeconds(MAX_TIME_WAITING_SEC).isAfter(current)) {
				// Notify
				notifierManager.doSend("Posible attack", Arrays.asList("Non validated request detected to "
						+ (deviceCheckData.getName() != null ? deviceCheckData.getName() : deviceCheckData.getMac())));
				firewallService.blockFromIp(deviceCheckData.getFromIp(), MAX_CACHED_TIME_MIN);
				firewallService.block2Ip(deviceCheckData.getFromIp(), MAX_CACHED_TIME_MIN);
				WAITING_LIST.remove(waiting.getKey());
			}
		}

	}

	public void onTokenResponse(DeviceCheckData deviceCheckData) {
		DeviceCheckData cachedDeviceCheckData = WAITING_LIST.get(deviceCheckData.getIp());

		if (cachedDeviceCheckData == null) {
			// Late response. Lets open network rules
			firewallService.allowFromIp(deviceCheckData.getFromIp());
			firewallService.allow2Ip(deviceCheckData.getFromIp());
			deviceCheckData.setReceived(true);
			deviceCheckData.setRequestTime(new DateTime());
			WAITING_LIST.put(deviceCheckData.getIp(), deviceCheckData);
		} else {
			cachedDeviceCheckData.setReceived(true);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object deviceList) {
		if (deviceList instanceof List) {
			DEVICEMAP.clear();
			for (Device device : (List<Device>) deviceList) {
				DEVICEMAP.put(device.getIp(), device);
			}
		}
	}

	@Override
	protected boolean doInternalCheck(boolean incoming, TrafficData trafficData) {
		return requestForToken(trafficData);
	}

	@Override
	protected String getMessage() {
		return "Unauthorized request detected";
	}

	@Override
	protected Checker getNext(CheckResult result) {
		return next;
	}

	@Override
	protected Integer bannedSeconds() {
		return null;
	}

	@Override
	protected DeviceCheckerRepository getRepository() {
		return deviceCheckerRepository;
	}

	@Override
	protected String getCheckerName() {
		return "DoubleCheckChecker";
	}

}
