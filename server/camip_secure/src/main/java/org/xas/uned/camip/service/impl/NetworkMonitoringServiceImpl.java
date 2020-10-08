package org.xas.uned.camip.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xas.uned.camip.service.NetworkMonitoringService;
import org.xas.uned.camip.service.TrafficCheckerService;
import org.xas.uned.camip.util.NetworkFlowListener;
import org.xas.uned.camip.util.Utilities;

@Service
public class NetworkMonitoringServiceImpl implements NetworkMonitoringService, NetworkFlowListener {

	@Autowired
	private TrafficCheckerService trafficCheckerService;

	@PostConstruct
	private void init() {
		// Starts process
		MonitoringProcessImpl.getInstance(this);
	}

	@Override
	public void freeProcess() {

		try {
			MonitoringProcessImpl.getInstance(this).freeProcess();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTraffic(String traffic) {
		try {
			trafficCheckerService.check(Utilities.convertTrafficInfo(traffic));
		} catch (Exception e) {

		}
	}

}
