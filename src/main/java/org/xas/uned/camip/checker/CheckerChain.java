package org.xas.uned.camip.checker;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.TrafficData;

@Service
public class CheckerChain {

	@Autowired
	private DDoSChecker dDoSChecker;

	@Autowired
	private DoubleCheckChecker doubleCheckChecker;

	@Autowired
	private MaliciousIpChecker maliciousIpChecker;

	@Autowired
	private WhiteListChecker whiteListChecker;

	private Checker init = whiteListChecker;

	@PostConstruct
	public void setup() {
		dDoSChecker.setNext(doubleCheckChecker);
		maliciousIpChecker.setNext(dDoSChecker);
		dDoSChecker.setNext(maliciousIpChecker);
		whiteListChecker.setNext(dDoSChecker);
	}

	public CheckResult doCheck(boolean incoming, TrafficData trafficData, Device device) {

		if (incoming) {
			return init.doCheck(incoming, trafficData, device);
		} else {
			return whiteListChecker.doCheck(incoming, trafficData, device);
		}
	}

}
