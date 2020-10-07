package org.xas.uned.camip.scheduled.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xas.uned.camip.scheduled.AbstractTask;
import org.xas.uned.camip.service.NetworkMonitoringService;
import org.xas.uned.camip.util.TaskConstants;

@Component
public class MonitoringTask extends AbstractTask {

	@Autowired
	private NetworkMonitoringService networkMonitoringService;

	public MonitoringTask() {
		initialize("MonitoringTask", TaskConstants.TASK_UPDATE_IP_LIST_PERIOD_HOURS, TimeUnit.HOURS);
	}

	@Override
	public void executeInternal() {
		networkMonitoringService.freeProcess();
	}
}
