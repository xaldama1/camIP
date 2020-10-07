package org.xas.uned.camip.checker;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.TrafficData;
import org.xas.uned.camip.repository.DeviceCheckerRepository;

@Service
public class WhiteListChecker extends AbstractChecker implements Observer {

	private static final long LEARN_TIME = 7 * 24 * 60 * 60 * 1000;

	private static final Set<String> ACCEPTED = new HashSet<>();

	private static final Map<String, Device> DEVICEMAP = new ConcurrentHashMap<>();

	private boolean isSecurityEnabled = Boolean.valueOf(System.getProperty("cu.ioy.wl.enabled", "false"));

	@Autowired
	private DeviceCheckerRepository deviceCheckerRepository;

	public void setEnabled(boolean isSecurityEnabled) {
		this.isSecurityEnabled = isSecurityEnabled;
	}

	public boolean getEnabled() {
		return isSecurityEnabled;
	}

	public void addAccepted(String host) {
		ACCEPTED.add(host);
	}

	private boolean isAccepted(String host) {
		return ACCEPTED.contains(host);
	}

	@Override
	protected boolean doInternalCheck(boolean incoming, TrafficData trafficData) {

		long current = System.currentTimeMillis();

		String host;
		if (incoming) {
			host = trafficData.getToHost();
		} else {
			host = trafficData.getFromHost();
		}

		Device device = DEVICEMAP.get(host);

		if (device.getDetected().getMillis() + LEARN_TIME > current) {
			// Learning period
			addAccepted(host);
			return true;
		} else {
			return isAccepted(host);
		}

	}

	@Override
	protected String getMessage() {
		return null;
	}

	@Override
	protected Checker getNext(CheckResult result) {
		if (result.isValid()) {
			return null;
		}
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
		return "WhiteListChecker";
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
}
