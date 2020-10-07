package org.xas.uned.camip.checker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.TrafficData;
import org.xas.uned.camip.repository.DeviceCheckerRepository;

@Service
public class DDoSChecker extends AbstractChecker {

	private static final int MAX_TTL_IN_LIST = 5 * 60 * 1000;

	private static final long WINDOW_DURATION = 1 * 60 * 1000;

	private static final int MAX_REQUEST_PER_WINDOW = 1000;

	private static final Map<String, Long> DDOSIPLIST = new ConcurrentHashMap<>();

	private static final Map<String, Integer> IP_REQUEST_LIST = new ConcurrentHashMap<>();

	private long windowStartTime = System.currentTimeMillis();

	@Autowired
	private DeviceCheckerRepository deviceCheckerRepository;

	private void cleanIfNeedeList() {
		long current = System.currentTimeMillis() - MAX_TTL_IN_LIST;

		DDOSIPLIST.entrySet().removeIf(entry -> entry.getValue() < current);

	}

	private void processHost(String fromHost) {

		long current = System.currentTimeMillis();

		if (windowStartTime + WINDOW_DURATION < current) {
			windowStartTime = current;
			IP_REQUEST_LIST.clear();
		}

		Integer count = IP_REQUEST_LIST.get(fromHost);
		if (count == null) {
			IP_REQUEST_LIST.put(fromHost, 1);
		} else {
			++count;
			IP_REQUEST_LIST.put(fromHost, count);
			if (count >= MAX_REQUEST_PER_WINDOW) {
				DDOSIPLIST.put(fromHost, current);
			}
		}
	}

//	public void resetList() {
//		DDOSIPLIST.clear();
//
//	}

	@Override
	protected boolean doInternalCheck(boolean incoming, TrafficData trafficData) {
		String host;
		if (incoming) {
			host = trafficData.getToHost();
		} else {
			host = trafficData.getFromHost();
		}
		cleanIfNeedeList();
		processHost(host);
		return !DDOSIPLIST.containsKey(host);
	}

	@Override
	protected String getMessage() {
		return "DDoS detected";
	}

	@Override
	protected Checker getNext(CheckResult result) {
		return next;
	}

	@Override
	protected Integer bannedSeconds() {
		return MAX_TTL_IN_LIST;
	}

	@Override
	protected DeviceCheckerRepository getRepository() {
		return deviceCheckerRepository;
	}

	@Override
	protected String getCheckerName() {
		return "DDoSChecker";
	}

}
