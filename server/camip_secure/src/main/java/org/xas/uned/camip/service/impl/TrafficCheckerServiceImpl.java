package org.xas.uned.camip.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.bean.Action;
import org.xas.uned.camip.bean.CheckResult;
import org.xas.uned.camip.bean.Device;
import org.xas.uned.camip.bean.TrafficData;
import org.xas.uned.camip.checker.CheckerChain;
import org.xas.uned.camip.notifier.NotifierManager;
import org.xas.uned.camip.service.FirewallService;
import org.xas.uned.camip.service.TrafficCheckerService;

@Service
public class TrafficCheckerServiceImpl implements TrafficCheckerService, Observer {

	@Autowired
	private CheckerChain checkerChain;

	@Autowired
	private NotifierManager notifierManager;

	@Autowired
	private FirewallService firewallService;

	private Map<String, Device> devicesIPs = new HashMap<>();

	@Override
	public void check(TrafficData trafficData) {
		Device device = getDeviceFromHost(trafficData.getToHost());
		if (device != null) {
			checkIncoming(trafficData, device);
		} else {
			device = getDeviceFromHost(trafficData.getFromHost());
			if (device != null) {
				checkOutgoing(trafficData, device);
			}
		}
	}

	private Device getDeviceFromHost(String host) {
		return devicesIPs.get(host);
	}

	private void checkIncoming(TrafficData requestData, Device device) {

		CheckResult result = checkerChain.doCheck(true, requestData, device);

		if (!result.isValid()) {
			for (String msg : result.getMsg()) {
				System.err.println(msg);
			}
			// Notify by email
			try {
				notifierManager.doSend("Incoming Access Alert", result.getMsg());
			} catch (Exception e) {
				System.err.println("Email notification error with cause:" + e.getCause().getMessage());
			}
			if (result.getAction() == Action.BLOCK) {
				firewallService.block2Ip(requestData.getFromHost(), null);
				firewallService.blockFromIp(requestData.getFromHost(), null);
			}
		}

		// Log all incoming requests

	}

	private void checkOutgoing(TrafficData requestData, Device device) {

		CheckResult result = checkerChain.doCheck(true, requestData, device);
		if (!result.isValid()) {
			System.err.println("Request to not known host");
			// Notify by email
			try {
				notifierManager.doSend("Outgoing data", Arrays.asList("Request to not known host"));
			} catch (Exception e) {
				System.err.println("Email notification error with cause:" + e.getCause().getMessage());
			}
			if (result.getAction() == Action.BLOCK) {
				firewallService.block2Ip(requestData.getToHost(), null);
				firewallService.blockFromIp(requestData.getToHost(), null);
			}
		} else {
			System.out.println("White Listed Host");
		}

		// Log all incoming requests
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable o, Object deviceList) {
		if (deviceList instanceof List) {
			synchronized (devicesIPs) {
				devicesIPs.clear();
				for (Device device : (List<Device>) deviceList) {
					devicesIPs.put(device.getIp(), device);
				}
			}
		}
	}

}
