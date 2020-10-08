package org.xas.uned.camip.scheduled.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xas.uned.camip.facade.DeviceFacade;
import org.xas.uned.camip.scheduled.AbstractTask;
import org.xas.uned.camip.util.TaskConstants;

@Component
public class DevicesTask extends AbstractTask {

	@Autowired
	private DeviceFacade deviceFacade;

	public DevicesTask() {
		initialize("DevicesTask", TaskConstants.TASK_UPDATE_DEVICE_LIST_PERIOD_MINS * 60, TimeUnit.SECONDS);
	}

	@Override
	public void executeInternal() {
		deviceFacade.updateCurrentConnectedList();
	}
}
