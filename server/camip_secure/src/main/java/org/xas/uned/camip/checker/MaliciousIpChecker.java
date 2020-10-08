package org.xas.uned.camip.checker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.TrafficData;
import org.xas.uned.camip.repository.DeviceCheckerRepository;

@Service
public class MaliciousIpChecker extends AbstractChecker {

	private static final Set<String> MALICIOUS_IP_LIST = new HashSet<>();

	@Autowired
	private DeviceCheckerRepository deviceCheckerRepository;

	public void addMaliciousIp(List<String> ipList) {

		MALICIOUS_IP_LIST.addAll(ipList);
	}

	public void cleanMaliciousIp() {

		MALICIOUS_IP_LIST.clear();
	}

	@Override
	protected boolean doInternalCheck(boolean incoming, TrafficData trafficData) {

		String host;
		if (incoming) {
			host = trafficData.getToHost();
		} else {
			host = trafficData.getFromHost();
		}

		return !MALICIOUS_IP_LIST.contains(host);
	}

	@Override
	protected String getMessage() {
		return "Malicious IP detected";
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
		return "MaliciousIpChecker";
	}

}
